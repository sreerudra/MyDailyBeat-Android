package edu.bu.met.vbalabhadrapatruni.mypeeps.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import edu.bu.met.vbalabhadrapatruni.mypeeps.R;

/**
 * Created by Virinchi on 4/10/2017.
 */

public class EmailMobileFragment extends Fragment implements SignUpFragment {
    private EditText emailField, mobileField;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.email_mobile_fragment, container, false);
        emailField = (EditText) root.findViewById(R.id.emailField);
        mobileField = (EditText) root.findViewById(R.id.mobileField);
        return root;
    }

    @Override
    public Object[] getData() {
        Object[] data = {emailField.getText().toString(), mobileField.getText().toString()};
        return data;
    }

    @Override
    public int getStep() {
        return 3;
    }

    @Override
    public boolean isValid() {
        boolean isEmailValid = Patterns.EMAIL_ADDRESS.matcher(emailField.getText().toString()).matches();
        boolean isPhoneValid = Patterns.PHONE.matcher(mobileField.getText().toString()).matches();
        return (isEmailValid && isPhoneValid);

    }
}
