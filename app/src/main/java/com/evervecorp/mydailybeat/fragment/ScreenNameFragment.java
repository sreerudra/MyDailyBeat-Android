package com.evervecorp.mydailybeat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.evervecorp.mydailybeat.R;

/**
 * Created by Virinchi on 4/10/2017.
 */

public class ScreenNameFragment extends Fragment implements SignUpFragment {
    private EditText scrField, passField, pass2Field;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.screenname_fragment, container, false);
        scrField = (EditText) root.findViewById(R.id.screenNameField);
        passField = (EditText) root.findViewById(R.id.passField);
        pass2Field = (EditText) root.findViewById(R.id.pass2Field);
        return root;
    }

    @Override
    public Object[] getData() {
        Object[] data = {scrField.getText().toString(), passField.getText().toString(), pass2Field.getText().toString()};
        return data;
    }

    @Override
    public int getStep() {
        return 2;
    }

    @Override
    public boolean isValid() {
        return passField.getText().toString().equalsIgnoreCase(pass2Field.getText().toString());
    }
}
