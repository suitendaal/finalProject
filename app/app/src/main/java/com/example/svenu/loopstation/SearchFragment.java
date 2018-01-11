package com.example.svenu.loopstation;


import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.RequestQueue;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends ListFragment {
    private Context context;
    private EditText editText;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        activity = getActivity();
        context = activity.getApplicationContext();
        editText = rootView.findViewById(R.id.editText);

        editText.addTextChangedListener(new GoEditTextListener());

        // Inflate the layout for this fragment
        return rootView;
    }

    private class GoEditTextListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String searchText = charSequence.toString();
            SongGetter songGetter = new SongGetter(searchText);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private class SongGetter {
        private String searchText;

        public SongGetter(String aSearchText) {
            searchText = aSearchText;
        }

    }
}
