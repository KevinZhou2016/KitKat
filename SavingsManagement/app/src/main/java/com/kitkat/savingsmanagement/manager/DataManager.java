package com.kitkat.savingsmanagement.manager;

import android.util.Log;

import com.kitkat.savingsmanagement.data.SavingsBean;
import com.kitkat.savingsmanagement.utils.Constants;
import com.kitkat.savingsmanagement.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/30 0030.
 */

public class DataManager {


    public static Date getNextDueSavingsItemDate(ArrayList<SavingsBean> list) {

        long due = Long.MAX_VALUE;
        long endTime;
        // small algorithm to get the due savings item in the future
        for (SavingsBean savings : list) {
            endTime = savings.getEndDate();
            if (endTime < due
                    && endTime > new Date().getTime()) {
                // when end date is before today, don't treat it as a due savings since it's a past one
                due = savings.getEndDate();
            }
        }

        if (due != Long.MAX_VALUE) {
            Log.d(Constants.LOG_TAG, "Next due savings item is: " + Utils.formatDate(due));
            return new Date(due);
        } else {
            Log.d(Constants.LOG_TAG, "No due savings item in the future");
            return null;
        }

    }
}
