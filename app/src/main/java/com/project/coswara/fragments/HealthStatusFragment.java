package com.project.coswara.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.coswara.R;
import com.project.coswara.Utils;
import com.project.coswara.activities.DetailsActivity;
import com.project.coswara.model.HealthData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.project.coswara.Constants.CONDITIONS;
import static com.project.coswara.Constants.HEALTH_STATUSES;

public class HealthStatusFragment extends Fragment {

    private final DetailsActivity.HealthDataUpdate callback;
    private Context context;
    private final HealthData healthData;
    private final DetailsActivity.NavigateTabs navigateTabsCallback;
    private Button submitBtn;
    private final DetailsActivity.SubmitForm submitFormCallback;
    private ProgressBar progressBar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public HealthStatusFragment(DetailsActivity.HealthDataUpdate callback,
                                DetailsActivity.NavigateTabs navigateTabsCallback,
                                HealthData healthData, DetailsActivity.SubmitForm submitFormCallback) {
        this.callback = callback;
        this.healthData = healthData;
        this.navigateTabsCallback = navigateTabsCallback;
        this.submitFormCallback = submitFormCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health_status, container, false);

        Button prevButton = (Button) view.findViewById(R.id.button_health_prev);
        submitBtn = (Button) view.findViewById(R.id.button_health_submit);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_submit);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTabsCallback.changeTab(2, -1);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                submitFormCallback.submit();
            }
        });

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group_health_status);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_health_status_0:
                        healthData.setCurrentStatus(HEALTH_STATUSES[0]);
                        break;
                    case R.id.radio_health_status_1:
                        healthData.setCurrentStatus(HEALTH_STATUSES[1]);
                        break;
                    case R.id.radio_health_status_2:
                        healthData.setCurrentStatus(HEALTH_STATUSES[2]);
                        break;
                    case R.id.radio_health_status_3:
                        healthData.setCurrentStatus(HEALTH_STATUSES[3]);
                        break;
                    case R.id.radio_health_status_4:
                        healthData.setCurrentStatus(HEALTH_STATUSES[4]);
                        break;
                    case R.id.radio_health_status_5:
                        healthData.setCurrentStatus(HEALTH_STATUSES[5]);
                        break;
                }

                update();
            }
        });

        List<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add((CheckBox) view.findViewById(R.id.checkbox_health_status_cold));
        checkBoxes.add((CheckBox) view.findViewById(R.id.checkbox_health_status_fever));
        checkBoxes.add((CheckBox) view.findViewById(R.id.checkbox_health_status_smoker));
        checkBoxes.add((CheckBox) view.findViewById(R.id.checkbox_health_status_asthma));
        checkBoxes.add((CheckBox) view.findViewById(R.id.checkbox_health_status_cld));
        checkBoxes.add((CheckBox) view.findViewById(R.id.checkbox_health_status_ht));
        checkBoxes.add((CheckBox) view.findViewById(R.id.checkbox_health_status_ihd));
        checkBoxes.add((CheckBox) view.findViewById(R.id.checkbox_health_status_diabetes));
        checkBoxes.add((CheckBox) view.findViewById(R.id.checkbox_health_status_cough));
        checkBoxes.add((CheckBox) view.findViewById(R.id.checkbox_health_status_pneumonia));

        for (int i = 0; i < checkBoxes.size(); i++) {
            final int iF = i;
            checkBoxes.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    healthData.updateMap(CONDITIONS[iF], isChecked);
                    update();
                }
            });
        }

        if (healthData != null) {
            String currStatus = healthData.getCurrentStatus();
            if (Utils.validString(currStatus)) {
                int pos = Utils.getArrayPos(HEALTH_STATUSES, currStatus, -1);
                if (pos != -1) ((RadioButton) radioGroup.getChildAt(pos)).setChecked(true);
            }

            for (Map.Entry mapElement : healthData.getHealthMap().entrySet()) {
                String key = (String) mapElement.getKey();
                Boolean val = (Boolean) mapElement.getValue();

                if (val) {
                    int pos = Utils.getArrayPos(CONDITIONS, key, -1);
                    if (pos != -1) checkBoxes.get(pos).setChecked(true);
                }
            }

            update();
        }

        return view;
    }

    private void update(){
        submitBtn.setVisibility(healthData.isComplete() ? View.VISIBLE : View.GONE);
        callback.updateHealthData(healthData);
    }
}
