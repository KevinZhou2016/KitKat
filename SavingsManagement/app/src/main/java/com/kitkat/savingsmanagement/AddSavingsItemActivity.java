package com.kitkat.savingsmanagement;

import android.app.DatePickerDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AddSavingsItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private String TAG = "Add Savings";

    private EditText    startDateEdit;
    private EditText    endDateEdit;
    private EditText    amountEdit;
    private EditText    annualizedEdit;
    private EditText    expectedEdit;
    private Date        startDate;
    private Date        endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_savings_item);

        startDateEdit = (EditText)findViewById(R.id.edit_start_date);
        endDateEdit = (EditText)findViewById(R.id.edit_end_date);
        amountEdit = (EditText)findViewById(R.id.edit_amount);
        annualizedEdit = (EditText)findViewById(R.id.edit_annualized_yield);
        expectedEdit = (EditText)findViewById(R.id.edit_expected_interest);

//        startDateEdit.setOnClickListener(showDatePicker);
//        endDateEdit.setOnClickListener(showDatePicker);
        startDateEdit.setOnFocusChangeListener(focusChangeListener);
        endDateEdit.setOnFocusChangeListener(focusChangeListener);

        annualizedEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (startDate == null) {
                        startDateEdit.setFocusable(true);
                        startDateEdit.requestFocus();
                        return;
                    }

                    if (endDate == null || !endDate.after(startDate)) {
                        endDateEdit.setFocusable(true);
                        endDateEdit.requestFocus();
                        return;
                    }

                    if (amountEdit.getText().toString().isEmpty()) {
                        amountEdit.setFocusable(true);
                        amountEdit.requestFocus();
                        return;
                    }

                    double expexted = calculateExpectedInterest();
                    expectedEdit.setFocusable(true);
                    expectedEdit.requestFocus();
                    expectedEdit.setText(String.valueOf(expexted));

                }
            }
        });

        Button saveBtn = (Button) findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddSavingsItemActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        Button cancelBtn = (Button)findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private final View.OnClickListener showDatePicker = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDatePicker();
        }
    };

    private final View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                int vId = v.getId();
                if (vId == startDateEdit.getId() || vId == endDateEdit.getId() ) {
                    showDatePicker();
                }
            }
        }
    };

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        int startYear = c.get(Calendar.YEAR);
        int startMonth = c.get(Calendar.MONTH);
        int startDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddSavingsItemActivity.this,
                AddSavingsItemActivity.this,
                startYear, startMonth, startDay);

        datePickerDialog.show();
    }

    private double calculateExpectedInterest() {

        // amount * (enddate - startdate) / 365 * annualized_yield = expected interest

        double amount = Double.parseDouble(amountEdit.getText().toString());
        double annulized = Double.parseDouble(annualizedEdit.getText().toString());

        long diff = endDate.getTime() - startDate.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        return  amount * days / 365 * annulized;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String dateString = dayOfMonth + "-" + (month+1) + "-" + year;

        Log.d(TAG, "onDateSet: "+ dateString);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month+1, dayOfMonth);
        if (startDateEdit.isFocused()) {
            startDate = calendar.getTime();
            startDateEdit.setText(dateString);
        } else {
            endDate = calendar.getTime();
            endDateEdit.setText(dateString);
        }
    }
}
