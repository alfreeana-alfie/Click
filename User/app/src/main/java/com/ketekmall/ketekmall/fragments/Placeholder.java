package com.ketekmall.ketekmall.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ketekmall.ketekmall.R;

public class Placeholder extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public Placeholder() {
    }

    public static Placeholder newInstance(int sectionNumber) {
        Placeholder fragment = new Placeholder();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.empty, container, false);
        return rootView;
    }
}