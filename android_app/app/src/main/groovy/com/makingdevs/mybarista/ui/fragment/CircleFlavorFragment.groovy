package com.makingdevs.mybarista.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.CircleFlavor
import com.makingdevs.mybarista.model.command.CircleFlavorCommand
import com.makingdevs.mybarista.service.CheckinManager
import com.makingdevs.mybarista.service.CheckingManagerImpl
import groovy.transform.CompileStatic
import retrofit2.Call
import retrofit2.Response

@CompileStatic
public class CircleFlavorFragment extends Fragment {

    private static final String TAG = "CircleFlavorFragment"
    private static Context contextView
    private SeekBar seekProcessSweetness
    private TextView seekProcessText
    private Button buttonCircleFlavor

    CheckinManager mCheckinManager = CheckingManagerImpl.instance

    CircleFlavorFragment() {}

    @Override
    View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_circle_flavor, container, false)
        seekProcessText = (TextView) root.findViewById(R.id.seekProcessSweetnessText)
        seekProcessSweetness = (SeekBar) root.findViewById(R.id.seekSweetness)
        seekProcessSweetness.setOnSeekBarChangeListener(new  SeekBar.OnSeekBarChangeListener() {
            public  void  onProgressChanged(SeekBar seekBar, int  progress,
                                            boolean  fromUser) {
                seekProcessText.setText("Sabor: "+progress);
            }

            public  void  onStartTrackingTouch(SeekBar seekBar) {}
            public  void  onStopTrackingTouch(SeekBar seekBar) {}
        });
        buttonCircleFlavor = (Button) root.findViewById(R.id.btnCircle)
        buttonCircleFlavor.setOnClickListener(new View.OnClickListener(){
            @Override
            void onClick(View v) {
                getCircleFlavor()
                //saveCircleFlavor(getCircleFlavor())
            }
        })

        contextView = getActivity().getApplicationContext()
        root
    }

    private void saveCircleFlavor(CircleFlavorCommand circleFlavorCommand) {
        mCheckinManager.saveCircle(circleFlavorCommand, onSuccess(), onError())
    }

    private CircleFlavorCommand getCircleFlavor() {
        String dulzura = seekProcessSweetness.getProgress()
        /*String acidez
        String floral
        String especiado
        String salado
        String frutosRojos
        String chocolate
        String caramelo
        String cuerpo
        String limpieza*/
        Log.d(TAG,dulzura)
        new CircleFlavorCommand()
    }

    private Closure onSuccess() {
        { Call<CircleFlavor> call, Response<CircleFlavor> response ->
            Log.d(TAG, response.dump().toString())
            if (response.code() == 201) {
                Toast.makeText(contextView, R.string.toastCheckinSuccess, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Closure onError() {
        { Call<CircleFlavor> call, Throwable t ->
            Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
        }
    }

}