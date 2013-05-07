package com.exacttarget.demo.etsdkdemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class OpenDirectDemo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_direct_demo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_open_direct_demo, menu);
		return true;
	}

}
