/**
 * Copyright (c) 2015 Salesforce Marketing Cloud.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.exacttarget.jb4a.sdkexplorer;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.exacttarget.jb4a.sdkexplorer.utils.Utils;

/**
 * SDK_ExplorerWebContentActivity is an activity that will display the URL sent with the payload
 * of the message sent from the Marketing Cloud.
 *
 * @author pvandyk
 */

public class SDK_ExplorerWebContentActivity extends BaseActivity {
    private static final String TAG = Utils.formatTag(SDK_ExplorerWebContentActivity.class.getSimpleName()) ;
    private int currentPage = CONSTS.VIEW_WEB_CONTENT_ACTIVITY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        prepareDisplay();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CONSTS.KEY_CURRENT_PAGE, currentPage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Boolean result = Utils.selectMenuItem(this, currentPage, item);
        return result != null ? result : super.onOptionsItemSelected(item);
    }

    private void prepareDisplay() {
        Utils.setActivityTitle(this, "Loading...");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ll.setGravity(Gravity.CENTER);

        String webPageUrl;
        if (this.getIntent().getExtras().containsKey("_x")) {
            webPageUrl = this.getIntent().getExtras().getString("_x");
        } else if (this.getIntent().getExtras().containsKey("_od")) {
            webPageUrl = this.getIntent().getExtras().getString("_od");
        } else {
            webPageUrl = null;
            setTitle("No website URL found in payload.");
        }

        WebView webView = new WebView(this);
        if (webPageUrl != null) {
            webView.loadUrl(webPageUrl);
        }
        webView.getSettings().setJavaScriptEnabled(true);
        ll.addView(webView);

        setContentView(ll);

        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                Utils.setActivityTitle(SDK_ExplorerWebContentActivity.this, view.getTitle());
            }
        });

    }
}
