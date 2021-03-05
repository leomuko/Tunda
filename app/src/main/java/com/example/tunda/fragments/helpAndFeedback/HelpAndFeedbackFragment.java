package com.example.tunda.fragments.helpAndFeedback;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tunda.R;
import com.example.tunda.helpers.drawerToggleHelper;

public class HelpAndFeedbackFragment extends Fragment {
    private Button mReportIssue;
    private Button mMakeAnInquiry;
    private Button mAirtelCall;
    private Button mMtnCall;
    drawerToggleHelper mDrawerToggleHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_help_and_feedback, container, false);
        mReportIssue = (Button) rootView.findViewById(R.id.report_button);
        mMakeAnInquiry = (Button) rootView.findViewById(R.id.inquire_button);
        mAirtelCall = (Button) rootView.findViewById(R.id.airtel_Call_button);
        mMtnCall = (Button) rootView.findViewById(R.id.mtn_call_button);

        mAirtelCall.setOnClickListener(v -> makeAirtelCall());
        mMtnCall.setOnClickListener(v -> makeMtnCall());
        mMakeAnInquiry.setOnClickListener(v -> inquireIssue());
        mReportIssue.setOnClickListener(v -> reportIssue());

        return rootView;
    }

    private void reportIssue() {
        String subject = "Report an issue";

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","email@email.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

    private void inquireIssue() {
        String subject = "Make an inquiry";

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","email@email.com", null));


        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

    private void makeMtnCall() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:+256788051148"));
        startActivity(callIntent);
    }

    private void makeAirtelCall() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:+256758901395"));
        startActivity(callIntent);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mDrawerToggleHelper = (drawerToggleHelper) context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ActionBar FragmentActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        FragmentActionBar.setTitle("Help And FeedBack");
        mDrawerToggleHelper.setDrawerState(false);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        mDrawerToggleHelper.setDrawerState(true);
        super.onDestroyView();
    }
}