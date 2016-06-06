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
    private TextView seekProcessSweetnessText
    private SeekBar seekProcessAcidity
    private TextView seekProcessAcidityText
    private SeekBar seekProcessFlowery
    private TextView seekProcessFloweryText
    private SeekBar seekProcessSpicy
    private TextView seekProcessSpicyText
    private SeekBar seekProcessSalty
    private TextView seekProcessSaltyText
    private SeekBar seekProcessBerries
    private TextView seekProcessBerriesText
    private SeekBar seekProcessChocolate
    private TextView seekProcessChocolateText
    private SeekBar seekProcessCandy
    private TextView seekProcessCandyText
    private SeekBar seekProcessBody
    private TextView seekProcessBodyText
    private SeekBar seekProcessCleaning
    private TextView seekProcessCleaningText
    private Button buttonCircleFlavor


    CheckinManager mCheckinManager = CheckingManagerImpl.instance

    CircleFlavorFragment() {}

    @Override
    View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_circle_flavor, container, false)
        seekProcessSweetnessText = (TextView) root.findViewById(R.id.seekProcessSweetnessText)
        seekProcessSweetness = (SeekBar) root.findViewById(R.id.seekSweetness)
        seekProcessAcidityText = (TextView) root.findViewById(R.id.seekProcessAcidityText)
        seekProcessAcidity = (SeekBar) root.findViewById(R.id.seekAcidity)
        seekProcessFloweryText = (TextView) root.findViewById(R.id.seekProcessFloweryText)
        seekProcessFlowery = (SeekBar) root.findViewById(R.id.seekFlowery)
        seekProcessSpicyText = (TextView) root.findViewById(R.id.seekProcessSpacyText)
        seekProcessSpicy = (SeekBar) root.findViewById(R.id.seekSpicy)
        seekProcessSaltyText = (TextView) root.findViewById(R.id.seekProcessSaltyText)
        seekProcessSalty = (SeekBar) root.findViewById(R.id.seekSalty)
        seekProcessBerriesText = (TextView) root.findViewById(R.id.seekProcessBerriesText)
        seekProcessBerries = (SeekBar) root.findViewById(R.id.seekBerries)
        seekProcessChocolateText = (TextView) root.findViewById(R.id.seekProcessChocolateText)
        seekProcessChocolate = (SeekBar) root.findViewById(R.id.seekChocolate)
        seekProcessCandyText = (TextView) root.findViewById(R.id.seekProcessCandyText)
        seekProcessCandy = (SeekBar) root.findViewById(R.id.seekCandy)
        seekProcessBodyText = (TextView) root.findViewById(R.id.seekProcessBodyText)
        seekProcessBody = (SeekBar) root.findViewById(R.id.seekBody)
        seekProcessCleaningText = (TextView) root.findViewById(R.id.seekProcessCleaningText)
        seekProcessCleaning = (SeekBar) root.findViewById(R.id.seekCleaning)
        generedSeekSweetness()
        generedSeekAcidity()
        generedSeekFlowery()
        generedSeekSpicy()
        generedSeekSalty()
        generedSeekBerries()
        generedSeekChocolate()
        generedSeekCandy()
        generedSeekBody()
        generedSeekCleaning()
        buttonCircleFlavor = (Button) root.findViewById(R.id.btnCircle)
        buttonCircleFlavor.setOnClickListener(new View.OnClickListener(){
            @Override
            void onClick(View v) {
                saveCircleFlavor(getCircleFlavor())
            }
        })

        contextView = getActivity().getApplicationContext()
        root
    }

    private void generedSeekSweetness() {
        seekProcessSweetness.setOnSeekBarChangeListener(new  SeekBar.OnSeekBarChangeListener() {
            public  void  onProgressChanged(SeekBar seekBar, int  progress,
                                            boolean  fromUser) {
                seekProcessSweetnessText.setText("Dulzura "+progress);
            }

            public  void  onStartTrackingTouch(SeekBar seekBar) {}
            public  void  onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekAcidity() {
        seekProcessAcidity.setOnSeekBarChangeListener(new  SeekBar.OnSeekBarChangeListener() {
            public  void  onProgressChanged(SeekBar seekBar, int  progress,
                                            boolean  fromUser) {
                seekProcessAcidityText.setText("Acidez "+progress);
            }

            public  void  onStartTrackingTouch(SeekBar seekBar) {}
            public  void  onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekFlowery() {
        seekProcessFlowery.setOnSeekBarChangeListener(new  SeekBar.OnSeekBarChangeListener() {
            public  void  onProgressChanged(SeekBar seekBar, int  progress,
                                            boolean  fromUser) {
                seekProcessFloweryText.setText("Floral "+progress);
            }

            public  void  onStartTrackingTouch(SeekBar seekBar) {}
            public  void  onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekSpicy() {
        seekProcessSpicy.setOnSeekBarChangeListener(new  SeekBar.OnSeekBarChangeListener() {
            public  void  onProgressChanged(SeekBar seekBar, int  progress,
                                            boolean  fromUser) {
                seekProcessSpicyText.setText("Especiado  "+progress);
            }

            public  void  onStartTrackingTouch(SeekBar seekBar) {}
            public  void  onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekSalty() {
        seekProcessSalty.setOnSeekBarChangeListener(new  SeekBar.OnSeekBarChangeListener() {
            public  void  onProgressChanged(SeekBar seekBar, int  progress,
                                            boolean  fromUser) {
                seekProcessSaltyText.setText("Salado  "+progress);
            }

            public  void  onStartTrackingTouch(SeekBar seekBar) {}
            public  void  onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekBerries() {
        seekProcessBerries.setOnSeekBarChangeListener(new  SeekBar.OnSeekBarChangeListener() {
            public  void  onProgressChanged(SeekBar seekBar, int  progress,
                                            boolean  fromUser) {
                seekProcessBerriesText.setText("Frutos Rojos  "+progress);
            }

            public  void  onStartTrackingTouch(SeekBar seekBar) {}
            public  void  onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekChocolate() {
        seekProcessChocolate.setOnSeekBarChangeListener(new  SeekBar.OnSeekBarChangeListener() {
            public  void  onProgressChanged(SeekBar seekBar, int  progress,
                                            boolean  fromUser) {
                seekProcessChocolateText.setText("Chocolate  "+progress);
            }

            public  void  onStartTrackingTouch(SeekBar seekBar) {}
            public  void  onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekCandy() {
        seekProcessCandy.setOnSeekBarChangeListener(new  SeekBar.OnSeekBarChangeListener() {
            public  void  onProgressChanged(SeekBar seekBar, int  progress,
                                            boolean  fromUser) {
                seekProcessCandyText.setText("Caramelo  "+progress);
            }

            public  void  onStartTrackingTouch(SeekBar seekBar) {}
            public  void  onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekBody() {
        seekProcessBody.setOnSeekBarChangeListener(new  SeekBar.OnSeekBarChangeListener() {
            public  void  onProgressChanged(SeekBar seekBar, int  progress,
                                            boolean  fromUser) {
                seekProcessBodyText.setText("Cuerpo  "+progress);
            }

            public  void  onStartTrackingTouch(SeekBar seekBar) {}
            public  void  onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekCleaning() {
        seekProcessCleaning.setOnSeekBarChangeListener(new  SeekBar.OnSeekBarChangeListener() {
            public  void  onProgressChanged(SeekBar seekBar, int  progress,
                                            boolean  fromUser) {
                seekProcessCleaningText.setText("Limpieza  "+progress);
            }

            public  void  onStartTrackingTouch(SeekBar seekBar) {}
            public  void  onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void saveCircleFlavor(CircleFlavorCommand circleFlavorCommand) {
        mCheckinManager.saveCircle(circleFlavorCommand, onSuccess(), onError())
    }

    private CircleFlavorCommand getCircleFlavor() {
        String dulzura = seekProcessSweetness.getProgress()
        String acidez = seekProcessAcidity.getProgress()
        String floral = seekProcessFlowery.getProgress()
        String especiado = seekProcessSpicy.getProgress()
        String salado = seekProcessSalty.getProgress()
        String frutosRojos = seekProcessBerries.getProgress()
        String chocolate = seekProcessChocolate.getProgress()
        String caramelo = seekProcessChocolate.getProgress()
        String cuerpo = seekProcessBody.getProgress()
        String limpieza = seekProcessCleaning.getProgress()
        new CircleFlavorCommand(dulzura:dulzura,acidez:acidez,floral:floral,especiado:especiado,salado:salado,frutosRojos:frutosRojos,chocolate:chocolate,caramelo:caramelo,cuerpo:cuerpo,limpieza:limpieza)
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