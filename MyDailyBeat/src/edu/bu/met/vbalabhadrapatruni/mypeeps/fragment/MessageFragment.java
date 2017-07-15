package edu.bu.met.vbalabhadrapatruni.mypeeps.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import edu.bu.met.vbalabhadrapatruni.mypeeps.R;

/**
 * Created by Virinchi on 4/10/2017.
 */

public class MessageFragment extends Fragment implements SignUpFragment {
    private String message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.message_fragment, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.messageView);
        textView.setText(this.message);
        return rootView;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Object[] getData() {
        return new Object[1];
    }

    @Override
    public int getStep() {
        return 0;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
