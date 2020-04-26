package com.project.coswara.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.coswara.R;
import com.project.coswara.Utils;
import com.project.coswara.activities.DetailsActivity;
import com.project.coswara.LoadCountryStateData;
import com.project.coswara.adapters.CustomSpinnerAdapter;
import com.project.coswara.model.Metadata;

import java.util.ArrayList;
import java.util.List;

import static com.project.coswara.Constants.COUNTRIES;
import static com.project.coswara.Constants.GENDERS;

public class MetadataFragment extends Fragment {

    private Context context;
    private String selectedCountry = COUNTRIES[0], selectedState = "";
    private List<String> states = new ArrayList<>();
    private Spinner stateSpinner;
    private final DetailsActivity.MetadataUpdate callback;
    private final DetailsActivity.NavigateTabs navigateTabsCallback;
    private final Metadata metadata;
    private Button nextBtn;

    public MetadataFragment(DetailsActivity.MetadataUpdate callback,
                            DetailsActivity.NavigateTabs navigateTabsCallback,
                            Metadata metadata) {
        this.callback = callback;
        this.navigateTabsCallback = navigateTabsCallback;
        this.metadata = metadata;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_metadata, container, false);

        nextBtn = (Button) view.findViewById(R.id.button_metadata_next);
        Button prevBtn = (Button) view.findViewById(R.id.button_metadata_prev);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTabsCallback.changeTab(1, 1);
            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTabsCallback.changeTab(1, -1);
            }
        });

        RadioGroup englishGroup = (RadioGroup) view.findViewById(R.id.input_english_proficient);
        englishGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.input_english_radio_yes:
                        metadata.setEnglishProficient(1);
                        break;
                    case R.id.input_english_radio_no:
                        metadata.setEnglishProficient(0);
                        break;
                }
                update();
            }
        });

        RadioGroup returningUserGroup = (RadioGroup) view.findViewById(R.id.radio_group_returning_user);
        returningUserGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio_returning_user_yes:
                        metadata.setReturningUser(1);
                        break;
                    case R.id.radio_returning_user_no:
                        metadata.setReturningUser(0);
                        break;
                }
                update();
            }
        });

        final EditText ageEdit = (EditText) view.findViewById(R.id.input_age);
        final TextView ageError = (TextView) view.findViewById(R.id.age_error_text);
        ageEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if(s == null || s.length() == 0) metadata.setAge(0);
                    else{
                        int age = Integer.parseInt(s.toString());
                        if(age > 0 && age <= 140) {
                            metadata.setAge(age);
                            ageError.setVisibility(View.GONE);
                        }else{
                            metadata.setAge(0);
                            ageError.setVisibility(View.VISIBLE);
                        }
                    }
                    update();
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final EditText localityEdit = (EditText) view.findViewById(R.id.input_locality);
        localityEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == null || s.length() == 0) metadata.setLocality("");
                else metadata.setLocality(s.toString());
                update();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Spinner genderSpinner = (Spinner) view.findViewById(R.id.input_gender);
        ArrayAdapter<String> genderAdapter = new CustomSpinnerAdapter(context,
                android.R.layout.simple_spinner_item, GENDERS);

        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String gender = GENDERS[position];
                if(gender != null) {
                    metadata.setGender(gender.toLowerCase());
                    update();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner countrySpinner = (Spinner) view.findViewById(R.id.input_country);
        ArrayAdapter<String> countryAdapter = new CustomSpinnerAdapter(context, android.R.layout.simple_spinner_item,
                COUNTRIES);

        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);

        stateSpinner = (Spinner) view.findViewById(R.id.input_state);
        updateStateSpinner();

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = COUNTRIES[position];
                metadata.setCountry(selectedCountry);
                update();
                updateStateSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = states.get(position);
                metadata.setState(selectedState);
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //init previously chosen values
        if(metadata != null){
            if(metadata.getAge() != 0) ageEdit.setText(String.valueOf(metadata.getAge()));
            genderSpinner.setSelection(Utils.getArrayPos(GENDERS, metadata.getGender(), 0));

            int engProf = metadata.getEnglishProficient();
            if(engProf == 1) englishGroup.check(R.id.input_english_radio_yes);
            else englishGroup.check(R.id.input_english_radio_no);

            int returnUser = metadata.getReturningUser();
            if(returnUser == 1) returningUserGroup.check(R.id.radio_returning_user_yes);
            else returningUserGroup.check(R.id.radio_returning_user_no);

            selectedCountry = metadata.getCountry();
            countrySpinner.setSelection(Utils.getArrayPos(COUNTRIES, selectedCountry, 0));

            selectedState = metadata.getState();
            updateStateSpinner();

            String loc = metadata.getLocality();
            if(loc != null) localityEdit.setText(loc);
        }

        return view;
    }

    private void update(){
        nextBtn.setVisibility(metadata.isComplete() ? View.VISIBLE : View.GONE);
        callback.updateMetadata(metadata);
    }

    private void updateStateSpinner(){
        states = LoadCountryStateData.getCities(selectedCountry);
        if(states == null) return;

        ArrayAdapter<String> stateAdapter = new CustomSpinnerAdapter(context, android.R.layout.simple_spinner_item, states);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);

        if(selectedState != null && !selectedState.isEmpty()) stateSpinner.setSelection(states.indexOf(selectedState));

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = states.get(position);
                metadata.setState(selectedState);
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
