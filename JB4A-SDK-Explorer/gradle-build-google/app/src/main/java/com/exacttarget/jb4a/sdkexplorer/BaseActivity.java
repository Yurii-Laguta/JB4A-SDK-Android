package com.exacttarget.jb4a.sdkexplorer;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.exacttarget.etpushsdk.ETPush;
import com.exacttarget.jb4a.sdkexplorer.utils.Utils;

/**
 * Created by pvandyk on 10/23/14.
 */
public class BaseActivity extends FragmentActivity {

    private static final String TAG = Utils.formatTag(BaseActivity.class.getSimpleName());

    @Override
    protected void onPause() {
        super.onPause();

        try {
            // Let JB4A SDK know when each activity paused
            ETPush.activityPaused(this);
        } catch (Exception e) {
            if (ETPush.getLogLevel() <= Log.ERROR) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            // Let JB4A SDK know when each activity is resumed
            ETPush.activityResumed(this);
        } catch (Exception e) {
            if (ETPush.getLogLevel() <= Log.ERROR) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        if (SDK_ExplorerApp.getQuitAppNow()) {
            finish();
        }
    }
}
