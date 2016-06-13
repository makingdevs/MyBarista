package com.makingdevs.mybarista.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.common.ImageUtil
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.PhotoCheckin
import com.makingdevs.mybarista.model.User
import com.makingdevs.mybarista.model.command.UserCommand
import com.makingdevs.mybarista.service.*
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

            Log.d(TAG,"Resize img...")
            Bitmap bitmapResize = resizeBitmapFromFilePath(photoFile.getPath(),1280,960)
            File photo = saveBitmapToFile(bitmapResize,photoFile.getName())

            currentUser = mSessionManager.getUserSession(getContext())
            Log.d(TAG,"Usuario..."+currentUser.dump().toString())

            Log.d(TAG,"Enviando...")
            mUserManager.upload(new UserCommand(id:currentUser.id),photo.getPath(),onSuccessFile(),onError())

            Log.d(TAG,"Galeria...")
            mImageUtil.addPictureToGallery(getContext(),photo.getPath())

        } else {
            Toast.makeText(getContext(), "Error al caputar la foto", Toast.LENGTH_SHORT).show()
        }
    }

    Bitmap resizeBitmapFromFilePath(String pathPhoto,Integer width,Integer height){
        BitmapFactory.Options bmOptions = new BitmapFactory.Options()
        Bitmap bitmap = BitmapFactory.decodeFile(pathPhoto,bmOptions)
        bitmap = Bitmap.createScaledBitmap(bitmap,width,height,true)
    }

    private File saveBitmapToFile(Bitmap bitmap,String photoName){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + File.separator + photoName)
        FileOutputStream fileOutputStream
        try {
            file.createNewFile()
            fileOutputStream = new FileOutputStream(file)
            fileOutputStream.write(bytes.toByteArray())
            fileOutputStream.close()
        }catch (Exception e){
            Log.d(TAG,"Error... "+e.message)
        }
        file
    }

    @Override
    View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_show_chek_in, container, false)
        itemView = root
        mButtonCircleFlavor = (Button) root.findViewById(R.id.btnCircle_flavor)

        currentUser = mSessionManager.getUserSession(getContext())
        String photoURL = mUserManager.getPhoto(currentUser.id,onSuccessGetPhoto(),onError())

        photoCheckinImageView = (ImageView) root.findViewById(R.id.show_photo_checkin)
        setPhotoImageView(getContext(),photoURL,photoCheckinImageView)

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
            Log.d(TAG,"URL foto..."+response.body().dump().toString())
            String photoUrl = response.body().url_file
            setPhotoImageView(getContext(),photoUrl,photoCheckinImageView)
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
                photoFile = createImageFile()
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

    private File createImageFile(){
        String timeStamp = new Date().format("yyyyMMdd_HHmmss")
        String imageFileName = "Checkin_$timeStamp"
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        File image = File.createTempFile(imageFileName,".jpg", storageDir)
    }

    void setPhotoImageView(Context context,String photoUrl,ImageView imageViewPhoto){
        Glide.with(context).load(photoUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(400, 350)
                .into(imageViewPhoto)
    }



}