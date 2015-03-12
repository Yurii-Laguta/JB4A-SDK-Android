package com.exacttarget.jb4a.sdkexplorer;

import android.os.Bundle;
import android.view.MenuItem;

public class SDK_ExplorerViewFavoritesActivity extends BaseActivity {
    private static final String TAG = SDK_ExplorerViewFavoritesActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_favorites_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            Utils.setActivityTitle(this, "Favorite Items Activity");
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