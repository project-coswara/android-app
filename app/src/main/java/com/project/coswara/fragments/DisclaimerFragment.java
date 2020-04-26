package com.project.coswara.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.coswara.R;
import com.project.coswara.Utils;
import com.project.coswara.activities.DetailsActivity;

import static com.project.coswara.Constants.BODY_1;
import static com.project.coswara.Constants.BODY_2;
import static com.project.coswara.Constants.BODY_3;
import static com.project.coswara.Constants.BODY_4;

public class DisclaimerFragment extends Fragment {

    private final DetailsActivity.NavigateTabs callback;

    public DisclaimerFragment(DetailsActivity.NavigateTabs callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_disclaimer, container, false);

        String content = BODY_1 + BODY_2 + BODY_3;
        TextView textView = (TextView) view.findViewById(R.id.text_disclaimer);
        Utils.enableLinksInText(textView, content);

        TextView textView1 = (TextView) view.findViewById(R.id.text_disclaimer_1);
        Utils.enableLinksInText(textView1, BODY_4);

        Button button = (Button) view.findViewById(R.id.button_details_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.changeTab(0, 1);
            }
        });

        return view;
    }
}
