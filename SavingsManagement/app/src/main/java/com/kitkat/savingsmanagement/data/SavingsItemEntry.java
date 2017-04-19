package com.kitkat.savingsmanagement.data;

import android.provider.BaseColumns;

/**
 * Created by Lin on 15/04/2017.
 */

public class SavingsItemEntry implements BaseColumns {

    public static final String TABLE_NAME = "savings_entries";
    public static final String COLUME_NAME_BANK_NAME = "bank_name";
    public static final String COLUME_NAME_START_DATE = "start_date";
    public static final String COLUME_NAME_END_DATE = "end_date";
    public static final String COLUME_NAME_AMOUNT = "amount";
    public static final String COLUME_NAME_YIELD = "yield";
    public static final String COLUME_NAME_INTEREST = "interest";
}
