package com.makingdevs.mybarista.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.CamaraUtil
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.command.UploadCommand
import com.makingdevs.mybarista.model.command.UserCommand
import com.makingdevs.mybarista.service.*
import com.makingdevs.mybarista.ui.activity.BaristaActivity
import com.makingdevs.mybarista.ui.activity.CircleFlavorActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class ShowCheckinFragment extends Fragment {

    CheckinManager mCheckinManager = CheckingManagerImpl.instance
    static final int REQUEST_TAKE_PHOTO = 0

    private static final String TAG = "ShowCheckinFragment"
    private static String ID_CHECKIN
    TextView mOrigin
    TextView mMethod
    TextView mPrice
    TextView mNote
    TextView mDateCreated
    Button mButtonCircleFlavor
    View itemView
    ImageButton mButtonCamera
    File photoFile
    ImageUtil mImageUtil
    SessionManager mSessionManager = SessionManagerImpl.instance
    User currentUser
    ImageView photoCheckinImageView
    CamaraUtil mCamaraUtil = new CamaraUtil()
    ImageUtil mImageUtil1 = new ImageUtil()
    Button mBarista

    UserManager mUserManager = UserManagerImpl.instance

    ShowCheckinFragment(String id){
        Bundle args = new Bundle()
        args.putSerializable(ID_CHECKIN, id)
        this.arguments = args
    }

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        if(!getArguments() || !getArguments()?.getSerializable(ID_CHECKIN))
            throw new IllegalArgumentException("No arguments $ID_CHECKIN")
        String checkinId = getArguments()?.getSerializable(ID_CHECKIN)
        mCheckinManager.show(checkinId,onSuccess(),onError())
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            //Log.d(TAG,"Resize img...")
            Bitmap bitmapResize = mCamaraUtil.resizeBitmapFromFilePath(photoFile.getPath(),1280,960)
            File photo = mCamaraUtil.saveBitmapToFile(bitmapResize,photoFile.getName())

            currentUser = mSessionManager.getUserSession(getContext())
            //Log.d(TAG,"Usuario..."+currentUser.dump().toString())

            //Log.d(TAG,"Enviando...")
            String checkinId = getArguments()?.getSerializable(ID_CHECKIN)
            mUserManager.upload(new UploadCommand(idCheckin: checkinId,idUser:currentUser.id,pathFile: photo.getPath()),onSuccessFile(),onError())

            //Log.d(TAG,"Galeria...")
            mImageUtil.addPictureToGallery(getContext(),photo.getPath())

        } else {
            Toast.makeText(getContext(), "Error al caputar la foto", Toast.LENGTH_SHORT).show()
        }
    }


    @Override
    View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_show_chek_in, container, false)
        itemView = root
        mButtonCircleFlavor = (Button) root.findViewById(R.id.btnCircle_flavor)

        String currentCheckin = getArguments()?.getSerializable(ID_CHECKIN)
        String photoURL = mUserManager.getPhoto(currentCheckin,onSuccessGetPhoto(),onError())

        mBarista = (Button) root.findViewById(R.id.btnBarista)
        photoCheckinImageView = (ImageView) root.findViewById(R.id.show_photo_checkin)
        mImageUtil1.setPhotoImageView(getContext(),photoURL,photoCheckinImageView)

        mBarista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkinId = getArguments()?.getSerializable(ID_CHECKIN)
                Intent intent = BaristaActivity.newIntentWithContext(getContext())
                intent.putExtra("checkingId",checkinId)
                startActivity(intent)
            }
        });

        mButtonCircleFlavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkinId = getArguments()?.getSerializable(ID_CHECKIN)
                Intent intent = CircleFlavorActivity.newIntentWithContext(getContext())
                intent.putExtra("checkingId",checkinId)
                startActivity(intent)
            }
        });
        root
    }

    private Closure onSuccess(){
        { Call<Checkin> call, Response<Checkin> response ->
            Log.d(TAG,response.body().toString())
            setCheckinInView(response.body())
        }
    }

    private Closure onError(){
        { Call<Checkin> call, Throwable t -> Log.d("ERRORZ", "el error "+t.message) }
    }

    private Closure onSuccessFile(){
        { Call<Checkin> call, Response<Checkin> response ->
            Log.d(TAG,response.dump().toString())
        }
    }

    private Closure onSuccessGetPhoto(){
        { Call<PhotoCheckin> call, Response<PhotoCheckin> response ->

            if(response.body()!=null){
                Log.d(TAG,"URL ARCHIVO SERVIDOR... "+response.body().dump().toString())
                String photoUrl = response.body().url_file
                mImageUtil1.setPhotoImageView(getContext(),photoUrl,photoCheckinImageView)
            }
            else{
                Log.d(TAG,"NO HAY FOTO")
            }
        }
    }

    private void setCheckinInView(Checkin checkin){
        mOrigin = (TextView) itemView.findViewById(R.id.origin_data)
        mMethod = (TextView) itemView.findViewById(R.id.method_data)
        mPrice = (TextView) itemView.findViewById(R.id.price_data)
        mNote = (TextView) itemView.findViewById(R.id.note_data)
        //mDateCreated  = (TextView) itemView.findViewById(R.id._data)
        mButtonCamera = (ImageButton) itemView.findViewById(R.id.button_camera)
        mButtonCamera.onClickListener = {
            dispatchTakePictureIntent()
        }

        mOrigin.text = checkin.origin
        mMethod.text = checkin.method
        mPrice.text = checkin.price
        mNote.text = checkin.note
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            try {
                photoFile = mCamaraUtil.createImageFile()
            } catch (IOException ex) {
                Log.d(TAG,"Error al crear la foto $ex")
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                Log.d(TAG,takePictureIntent.getProperties().toString())
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

}