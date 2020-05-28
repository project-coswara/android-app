package com.project.coswara.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.coswara.R;
import com.project.coswara.activities.DetailsActivity;
import com.project.coswara.util.Utils;

import static com.project.coswara.util.Constants.COSWARA_TERMS_URL;

public class DisclaimerFragment extends Fragment {

    private final DetailsActivity.NavigateTabs callback;
    private Context context;

    public DisclaimerFragment(DetailsActivity.NavigateTabs callback) {
        this.callback = callback;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_disclaimer, container, false);

        Button button = (Button) view.findViewById(R.id.button_details_start);
        Button viewTermsBtn = (Button) view.findViewById(R.id.button_view_terms);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.changeTab(0, 1);
            }
        });

        viewTermsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openInBrowser(context, COSWARA_TERMS_URL);
            }
        });

        return view;
    }
}
