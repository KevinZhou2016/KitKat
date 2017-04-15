package com.kitkat.savingsmanagement.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.kitkat.savingsmanagement.R;
import com.kitkat.savingsmanagement.utils.Constants;
import com.kitkat.savingsmanagement.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AddSavingsItemActivity extends AppCompatActivity {

    private String TAG = "Add Savings";

    private EditText mBankNameEdit;
    private EditText mStartDateEdit;
    private EditText mEndDateEdit;
    private EditText mAmountEdit;
    private EditText mAnnualizedEdit;
    private EditText mExpectedEdit;

    private Date mStartDate;
    private Date mEndDate;

    private Calendar mCalendar = Calendar.getInstance();
    private float   mAmount;
    private float   mAnnualizedYield;
    private float   mExpectedInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_savings_item);

        setupUI();
    }

    private void setupUI() {

        mBankNameEdit = (EditText)findViewById(R.id.edit_bank_name);
        mStartDateEdit = (EditText)findViewById(R.id.edit_start_date);
        mEndDateEdit = (EditText)findViewById(R.id.edit_end_date);
        mAmountEdit = (EditText)findViewById(R.id.edit_amount);
        mAnnualizedEdit = (EditText)findViewById(R.id.edit_annualized_yield);
        mExpectedEdit = (EditText)findViewById(R.id.edit_expected_interest);

        // set text watcher to update interest
        mBankNameEdit.addTextChangedListener(mInterestTextWatcher);
        mAmountEdit.addTextChangedListener(mInterestTextWatcher);
        mAnnualizedEdit.addTextChangedListener(mInterestTextWatcher);
        mAmountEdit.setOnFocusChangeListener(mOnFocusChangeListener);
        mAnnualizedEdit.setOnFocusChangeListener(mOnFocusChangeListener);

        // set data field listener
        mStartDateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker((EditText) v, true);
                }
            }
        });

        mStartDateEdit.setInputType(InputType.TYPE_NULL);
        mStartDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.hasFocus()) {
                    showDatePicker((EditText) v, true);
                }
            }
        });

        mEndDateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker((EditText) v, false);
                }
            }
        });
        mEndDateEdit.setInputType(InputType.TYPE_NULL);
        mEndDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.hasFocus()) {
                    showDatePicker((EditText) v, false);
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

    private TextWatcher mInterestTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            updateInterest();
        }
    };

    /**
     *  For Amount and Yield field use
     */
    private final View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                EditText edit = (EditText) v;
                String amountString = edit.getText().toString();
                if (!Utils.isNullOrEmpty(amountString)) {
                    edit.setText(Utils.getFloat(amountString));
                }
            }
        }
    };

    private void showDatePicker(final EditText edit, final boolean isStartDate) {

        // hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(edit.getWindowToken(), 0);

        // show date picker
        DatePickerDialog picker = new DatePickerDialog(AddSavingsItemActivity.this,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                Date date = new Date(calendar.getTimeInMillis());
                edit.setText(Utils.formatDate(date, Constants.FORMAT_DATE_YEAR_MONTH_DAY));

                if (isStartDate) {
                    mStartDate = new Date(date.getTime());
                } else {
                    mEndDate = new Date(date.getTime());
                }

                updateInterest();
            }
        },
        mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        picker.show();
    }

    private void updateInterest() {

        String amountStr = mAmountEdit.getText().toString();
        String yieldStr = mAnnualizedEdit.getText().toString();
        String bankStr = mBankNameEdit.getText().toString();

        if (mStartDate != null && mEndDate != null
                && !Utils.isNullOrEmpty(amountStr)
                && !Utils.isNullOrEmpty(yieldStr)
                && !Utils.isNullOrEmpty(bankStr)) {

            float days = Utils.getDiffDays(mStartDate, mEndDate);
            mAmount = Float.valueOf(amountStr);
            mAnnualizedYield = Float.valueOf(yieldStr);

            mExpectedInterest = mAmount * (mAnnualizedYield / 100) * (days / Constants.DAYS_OF_ONE_YEAR);
            mExpectedInterest = Utils.roundFloat(mExpectedInterest);

            mExpectedEdit.setText(Utils.formatFloat(mExpectedInterest));

            Log.d(TAG, "updateInterest: start = " + mStartDate.toString() + "\nend = " + mEndDate.toString()
                    + "\ndays = " + days + "\namount = " + mAmount + "\nyield = " + mAnnualizedYield + "\nexpectedInterest = " + mExpectedInterest );

        }

    }
}
