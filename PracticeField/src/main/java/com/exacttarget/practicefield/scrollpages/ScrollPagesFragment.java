package com.exacttarget.practicefield.scrollpages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.exacttarget.practicefield.PracticeFieldSendMessagesDialog;
import com.exacttarget.practicefield.Utils;

public final class ScrollPagesFragment extends Fragment {
	private static final String KEY_CONTENT = "ScrollPagesFragment:Content";
	private static final String KEY_SHOW_SEND_MESSAGE = "ScrollPagesFragment:ShowSendMessage";
	private String mContent = "???";
	private boolean mShowSendMessage = false;

	public static ScrollPagesFragment newInstance(String content, boolean showSendMessage) {
		ScrollPagesFragment fragment = new ScrollPagesFragment();

		fragment.mContent = content;
		fragment.mShowSendMessage = showSendMessage;

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getString(KEY_CONTENT);
			mShowSendMessage = savedInstanceState.getBoolean(KEY_SHOW_SEND_MESSAGE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout layout = new LinearLayout(getActivity());
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		layout.setOrientation(LinearLayout.VERTICAL);

		if (mShowSendMessage) {
			Button sendMessageButton = new Button(this.getActivity());
			sendMessageButton.setText("Send Message");
			sendMessageButton.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					PracticeFieldSendMessagesDialog smDialog = new PracticeFieldSendMessagesDialog(ScrollPagesFragment.this.getActivity());
					smDialog.setCancelable(true);
					smDialog.show();
				}
			});
			sendMessageButton.setLayoutParams(new LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT));
			layout.addView(sendMessageButton);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<html><body><font size=\"4\">");
		sb.append(mContent);
		sb.append("</font></body></html>");

		WebView webView = Utils.createWebView(this.getActivity(), sb);
		layout.addView(webView);

		return layout;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
		outState.putBoolean(KEY_SHOW_SEND_MESSAGE, mShowSendMessage);
	}
}

