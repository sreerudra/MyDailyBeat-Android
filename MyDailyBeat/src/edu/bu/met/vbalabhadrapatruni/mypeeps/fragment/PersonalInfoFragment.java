package edu.bu.met.vbalabhadrapatruni.mypeeps.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import java.util.Date;
import java.text.SimpleDateFormat;

import edu.bu.met.vbalabhadrapatruni.mypeeps.R;

/**
 * Created by Virinchi on 4/10/2017.
 */



public class PersonalInfoFragment extends Fragment implements SignUpFragment {
    private EditText fNameField, lNameField, dobField, zipField;
    private Date selectedDate;
    public static final int OFFSET = -85;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.personal_info_fragment, container, false);
        fNameField = (EditText) root.findViewById(R.id.fNameField);
        lNameField = (EditText) root.findViewById(R.id.lNameField);
        dobField = (EditText) root.findViewById(R.id.dobField);
        zipField = (EditText) root.findViewById(R.id.zipField);
        dobField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis() - 1000);
                c.add(Calendar.YEAR, OFFSET);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker = new DatePickerDialog(PersonalInfoFragment.this.getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selected = Calendar.getInstance();
                        selected.set(Calendar.YEAR, year);
                        selected.set(Calendar.MONTH, month);
                        selected.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat();
                        String date = format.format(selected.getTime());
                        dobField.setText(date);
                        selectedDate = selected.getTime();
                    }
                }, year, month, day);
                mDatePicker.setTitle("Select Date of Birth");
                Calendar min = Calendar.getInstance();
                min.setTimeInMillis(System.currentTimeMillis() - 1000);
                min.add(Calendar.YEAR, -120);
                Calendar max = Calendar.getInstance();
                max.setTimeInMillis(System.currentTimeMillis() - 1000);
                max.add(Calendar.YEAR, -21);
                mDatePicker.getDatePicker().setMinDate(min.getTime().getTime());
                mDatePicker.getDatePicker().setMaxDate(max.getTime().getTime());
                mDatePicker.show();
            }
        });
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(fNameField, InputMethodManager.SHOW_IMPLICIT);
        View.OnClickListener fieldListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftKeyboard(v);
            }
        };
        fNameField.setOnClickListener(fieldListener);
        lNameField.setOnClickListener(fieldListener);
        zipField.setOnClickListener(fieldListener);
        return root;
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public Object[] getData() {
        Object[] data = new Object[4];
        data[0] = fNameField.getText().toString();
        data[1] = lNameField.getText().toString();
        data[2] = selectedDate;
        data[3] = zipField.getText().toString();
        return data;
    }

    @Override
    public int getStep() {
        return 1;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
