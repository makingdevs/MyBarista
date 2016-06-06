package com.makingdevs.mybarista.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.service.CheckinManager
import com.makingdevs.mybarista.service.CheckingManagerImpl
import com.makingdevs.mybarista.ui.activity.CircleFlavorActivity
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class ShowCheckinFragment extends Fragment {

    CheckinManager mCheckinManager = CheckingManagerImpl.instance

    private static final String TAG = "ShowCheckinFragment"
    private static String ID_CHECKIN
    TextView mOrigin
    TextView mMethod
    TextView mPrice
    TextView mNote
    TextView mDateCreated
    Button mButtonCircleFlavor
    View itemView

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
    View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_show_chek_in, container, false)
        itemView = root
        mButtonCircleFlavor = (Button) root.findViewById(R.id.btnCircle_flavor);
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
        { Call<Checkin> call, Throwable t -> Log.d("ERRORZ", "el error") }
    }

    private void setCheckinInView(Checkin checkin){
        mOrigin = (TextView) itemView.findViewById(R.id.origin_data)
        mMethod = (TextView) itemView.findViewById(R.id.method_data)
        mPrice = (TextView) itemView.findViewById(R.id.price_data)
        mNote = (TextView) itemView.findViewById(R.id.note_data)
        //mDateCreated  = (TextView) itemView.findViewById(R.id._data)

        mOrigin.text = checkin.origin
        mMethod.text = checkin.method
        mPrice.text = checkin.price
        mNote.text = checkin.note
    }

}