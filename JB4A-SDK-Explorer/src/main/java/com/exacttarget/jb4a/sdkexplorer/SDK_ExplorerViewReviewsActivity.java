package com.exacttarget.jb4a.sdkexplorer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import com.exacttarget.etpushsdk.ETException;
import com.exacttarget.etpushsdk.ETPush;

public class SDK_ExplorerViewReviewsActivity extends BaseActivity {
    private static final String TAG = SDK_ExplorerViewReviewsActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_reviews_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            Utils.setActivityTitle(this, "Reviews Activity");
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}