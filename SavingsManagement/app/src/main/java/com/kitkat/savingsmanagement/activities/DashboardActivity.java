package com.kitkat.savingsmanagement.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kitkat.savingsmanagement.R;
import com.kitkat.savingsmanagement.data.SavingsBean;
import com.kitkat.savingsmanagement.data.SavingsContentProvider;
import com.kitkat.savingsmanagement.data.SavingsItemEntry;
import com.kitkat.savingsmanagement.manager.AlarmsManager;
import com.kitkat.savingsmanagement.manager.DataManager;
import com.kitkat.savingsmanagement.utils.Constants;
import com.kitkat.savingsmanagement.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
//kevin for test
    private ListView mSavingsItemListView;
    private SavingsItemListAdapter mListAdapter;

    ArrayList<SavingsBean> mSavingsBeanList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private Date mNextDueSavingsDate;
    private TextView mTotalTextView;
    private float totalInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddSavingsItemScreen();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initViews();

    }

    private void startEditSavingsItemScreen(SavingsBean savings) {
        Intent intent = new Intent(this, AddSavingsItemActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_SAVINGS_ITEM_PARCEL, savings);
        startActivity(intent);
    }


    private void initViews() {

        mSavingsItemListView = (ListView) findViewById(R.id.lv_savings);
        mProgressBar = (ProgressBar) findViewById(R.id.pb);
        mSavingsItemListView.setEmptyView(mProgressBar);
        mListAdapter = new SavingsItemListAdapter();
        mTotalTextView = (TextView) findViewById(R.id.txt_total_interest_value);

        mSavingsItemListView.setAdapter(mListAdapter);
        mSavingsItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SavingsBean savings = mSavingsBeanList.get(position);
                startEditSavingsItemScreen(savings);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addItem) {
            startAddSavingsItemScreen();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Start add savings item screen
     */
    private void startAddSavingsItemScreen() {
        Intent intent = new Intent(this, AddSavingsItemActivity.class);
        startActivity(intent);
    }

    private void initData() {

        if (mSavingsBeanList != null) {
            mSavingsBeanList.clear();
        }

        // load from database and add into list
        Cursor cursor = getContentResolver().query(SavingsContentProvider.CONTENT_URI, null, null, null, "_id asc", null);
        totalInterest = 0;
        while (cursor != null && cursor.moveToNext()) {
            SavingsBean savingsBean = new SavingsBean();
            long id = cursor.getLong(cursor.getColumnIndex(SavingsItemEntry._ID));
            String bankName = cursor.getString(cursor.getColumnIndex(SavingsItemEntry.COLUME_NAME_BANK_NAME));
            long startDate = cursor.getLong(cursor.getColumnIndex(SavingsItemEntry.COLUME_NAME_START_DATE));
            long endDate = cursor.getLong(cursor.getColumnIndex(SavingsItemEntry.COLUME_NAME_END_DATE));
            float amount = cursor.getFloat(cursor.getColumnIndex(SavingsItemEntry.COLUME_NAME_AMOUNT));
            float yield = cursor.getFloat(cursor.getColumnIndex(SavingsItemEntry.COLUME_NAME_YIELD));
            float interest = cursor.getFloat(cursor.getColumnIndex(SavingsItemEntry.COLUME_NAME_INTEREST));
            savingsBean.setId(id);
            savingsBean.setBankName(bankName);
            savingsBean.setStartDate(startDate);
            savingsBean.setEndDate(endDate);
            savingsBean.setAmount(amount);
            savingsBean.setYield(yield);
            savingsBean.setInterest(interest);
            totalInterest +=  interest;
            mSavingsBeanList.add(savingsBean);
        }
        if (cursor != null) {
            cursor.close();
        }

        mListAdapter.notifyDataSetChanged();
        mTotalTextView.setText(String.valueOf(totalInterest));
        if (!Utils.isNullOrEmpty(mSavingsBeanList)) {
            mNextDueSavingsDate = DataManager.getNextDueSavingsItemDate(mSavingsBeanList);
            if (mNextDueSavingsDate != null) {
                // schedule an alarm
                AlarmsManager.scheduleAlarm(this, mNextDueSavingsDate);
            }
        }
    }


    private class SavingsItemListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSavingsBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return mSavingsBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_savings_bean, null);
                viewHolder = new ViewHolder();
                viewHolder.bankName = (TextView) convertView.findViewById(R.id.tv_bank_name);
                viewHolder.amount = (TextView) convertView.findViewById(R.id.tv_account_amount);
                viewHolder.startTime = (TextView) convertView.findViewById(R.id.tv_start_time);
                viewHolder.endTime = (TextView) convertView.findViewById(R.id.tv_end_time);
                viewHolder.yield = (TextView) convertView.findViewById(R.id.tv_yield);
                viewHolder.interest = (TextView) convertView.findViewById(R.id.tv_interest);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            SavingsBean savingsBean = mSavingsBeanList.get(position);
            viewHolder.bankName.setText(savingsBean.getBankName());
            viewHolder.amount.setText(Utils.formatMoney(savingsBean.getAmount()));
            viewHolder.startTime.setText(Utils.formatDate(savingsBean.getStartDate()));
            viewHolder.endTime.setText(Utils.formatDate(savingsBean.getEndDate()));
            viewHolder.yield.setText(getString(R.string.formatted_yield, savingsBean.getYield()));
            viewHolder.interest.setText(Utils.formatMoney(savingsBean.getInterest()));

            // Date color
            if (Utils.isToday(savingsBean.getEndDate())) {
                // Is today
                viewHolder.endTime.setTextColor(Color.BLUE);
            } else if (savingsBean.getEndDate() < new Date().getTime()) {
                // Before today
                viewHolder.endTime.setTextColor(Color.LTGRAY);
            } else if (mNextDueSavingsDate != null
                    && savingsBean.getEndDate() == mNextDueSavingsDate.getTime()) {
                // Next due date
                viewHolder.endTime.setTextColor(Color.RED);
            }

            return convertView;
        }

        class ViewHolder {
            TextView bankName;
            TextView amount;
            TextView startTime;
            TextView endTime;
            TextView yield;
            TextView interest;
        }
    }
}

