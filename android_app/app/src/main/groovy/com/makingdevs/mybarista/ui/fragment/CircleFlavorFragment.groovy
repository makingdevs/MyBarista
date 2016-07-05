package com.makingdevs.mybarista.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.makingdevs.mybarista.R
import com.makingdevs.mybarista.model.Checkin
import com.makingdevs.mybarista.model.command.CircleFlavorCommand
import com.makingdevs.mybarista.service.CheckinManager
import com.makingdevs.mybarista.service.CheckingManagerImpl
import com.makingdevs.mybarista.ui.activity.ShowCheckinActivity
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

    CircleFlavorFragment() { super() }


    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        buttonCircleFlavor.setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                String id = getActivity().getIntent().getExtras().getString("checkingId")
                saveCircleFlavor(id, getCircleFlavor())
            }
        })

        contextView = getActivity().getApplicationContext()
        root
    }

    private void generedSeekSweetness() {
        seekProcessSweetness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekProcessSweetnessText.setText("Dulzura " + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekAcidity() {
        seekProcessAcidity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekProcessAcidityText.setText("Acidez " + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekFlowery() {
        seekProcessFlowery.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekProcessFloweryText.setText("Floral " + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekSpicy() {
        seekProcessSpicy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekProcessSpicyText.setText("Especiado  " + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekSalty() {
        seekProcessSalty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekProcessSaltyText.setText("Salado  " + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekBerries() {
        seekProcessBerries.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekProcessBerriesText.setText("Frutos Rojos  " + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekChocolate() {
        seekProcessChocolate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekProcessChocolateText.setText("Chocolate  " + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekCandy() {
        seekProcessCandy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekProcessCandyText.setText("Caramelo  " + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekBody() {
        seekProcessBody.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekProcessBodyText.setText("Cuerpo  " + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generedSeekCleaning() {
        seekProcessCleaning.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekProcessCleaningText.setText("Limpieza  " + progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void saveCircleFlavor(String id, CircleFlavorCommand circleFlavorCommand) {
        mCheckinManager.saveCircle(id, circleFlavorCommand, onSuccess(), onError())
    }

    private CircleFlavorCommand getCircleFlavor() {
        String sweetness = seekProcessSweetness.getProgress()
        String acidity = seekProcessAcidity.getProgress()
        String flowery = seekProcessFlowery.getProgress()
        String spicy = seekProcessSpicy.getProgress()
        String salty = seekProcessSalty.getProgress()
        String berries = seekProcessBerries.getProgress()
        String chocolate = seekProcessChocolate.getProgress()
        String candy = seekProcessChocolate.getProgress()
        String body = seekProcessBody.getProgress()
        String cleaning = seekProcessCleaning.getProgress()
        new CircleFlavorCommand(sweetness: sweetness, acidity: acidity, flowery: flowery, spicy: spicy, salty: salty, berries: berries, chocolate: chocolate, candy: candy, body: body, cleaning: cleaning)
    }

    private Closure onSuccess() {
        { Call<Checkin> call, Response<Checkin> response ->
            if (response.code() == 201) {
                showCheckin(response.body())
            } else {
                Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Closure onError() {
        { Call<Checkin> call, Throwable t ->
            Toast.makeText(contextView, R.string.toastCheckinFail, Toast.LENGTH_SHORT).show();
        }
    }

    private void showCheckin(Checkin checkin) {
        Intent intent = ShowCheckinActivity.newIntentWithContext(getContext(), checkin.id, checkin.circle_flavor_id)
        intent.putExtra("circle_flavor_id", checkin.circle_flavor_id)
        startActivity(intent)
        getActivity().finish()
    }
}