---
layout: page
title: "Inbox"
subtitle: "Rich Push Inbox"
category: rich-push
date: 2015-05-14 12:00:00
order: 3
---
For CloudPage-only Messages, the Journey Builder for Apps SDK will place all CloudPage messages downloaded from the Marketing Cloud in the CloudPageListAdapter class.  The SDK downloads new messages and adds them to this adapter each time your app comes into the foreground.  

In order to display these CloudPage-only messages, your app must create an Activity that uses the CloudPageListAdapter. Review an example of this technique within the Journey Builder for Apps SDK Explorer, which is shown below.

You can create an Inbox view with capabilities to delete messages from the Inbox as well as to set messages to read and unread.

Review the [Journey Builder for Apps SDK Explorer app](https://github.com/ExactTarget/JB4A-SDK-Android/tree/master/JB4A-SDK-Explorer) within the Journey Builder for Apps Android SDK public GitHub repository. The app provides a full sample of a CloudPage Inbox within the SDK\_ExplorerCloudPageInboxActivity. 

1. Implement an Inbox Activity in your app that uses the CloudPageListAdapter provided by the SDK.

~~~ 
    
    // SDK_ExplorerCloudPageInboxActivity is an activity that will 
    // display CloudPages that have been downloaded by the SDK.

    public class SDK_ExplorerCloudPageInboxActivity extends BaseActivity {
        private int currentPage = CONSTS.CLOUDPAGE_INBOX_ACTIVITY;
        private MyCloudPageListAdapter cloudPageListAdapter;
    
        private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MMM dd yyyy - hh:mm a");
    
        private static final String TAG = SDK_ExplorerCloudPageInboxActivity.class.getName();
    
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.cloudpage_inbox_layout);
            
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
            Utils.setActivityTitle(this, R.string.cloudpage_inbox_activity_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
            RadioGroup filterRadioGroup;
            ListView cloudPageListView;
        
            filterRadioGroup = (RadioGroup) findViewById(R.id.filterRadioGroup);
            cloudPageListView = (ListView) findViewById(R.id.cloudPageListView);
        
            filterRadioGroup.setOnCheckedChangeListener(radioChangedListener);
        
            cloudPageListView.setOnItemClickListener(cloudPageItemClickListener);
            cloudPageListView.setOnItemLongClickListener(cloudPageItemDeleteListener);
        
            cloudPageListAdapter = new MyCloudPageListAdapter(getApplicationContext());
            cloudPageListView.setAdapter(cloudPageListAdapter);
        
        }
    
        private AdapterView.OnItemClickListener cloudPageItemClickListener = new AdapterView.OnItemClickListener() {
        
            @Override
            public void onItemClick(AdapterView&lt;?&gt; parent, View view, int position, long id) {
                Log.i(TAG, "Position Clicked: " + position);
                CloudPageListAdapter adapter = (CloudPageListAdapter) parent.getAdapter();
                Message message = (Message) adapter.getItem(position);
            
                adapter.setMessageRead(message);
            
                Intent intent = new Intent(SDK_ExplorerCloudPageInboxActivity.this, SDK_ExplorerCloudPageActivity.class);
                intent.putExtra("_x", message.getUrl());
                startActivity(intent);
            }
        
        };
    
        private AdapterView.OnItemLongClickListener cloudPageItemDeleteListener = new AdapterView.OnItemLongClickListener() {
        
            @Override
            public boolean onItemLongClick(AdapterView<> parent, View view, int position, long id) {
                Log.i(TAG, "Position Deleted: " + position);
                CloudPageListAdapter adapter = (CloudPageListAdapter) parent.getAdapter();
                Message message = (Message) adapter.getItem(position);
            
                adapter.deleteMessage(message);
            
                return true;
            }
        
        };
    
        private RadioGroup.OnCheckedChangeListener radioChangedListener = new RadioGroup.OnCheckedChangeListener(){
        
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(R.id.filterAll == checkedId) {
                    cloudPageListAdapter.setDisplay(CloudPageListAdapter.DISPLAY_ALL);
                } else if(R.id.filterRead == checkedId) {
                    cloudPageListAdapter.setDisplay(CloudPageListAdapter.DISPLAY_READ);
                } else if(R.id.filterUnread == checkedId) {
                    cloudPageListAdapter.setDisplay(CloudPageListAdapter.DISPLAY_UNREAD);
                }
            }
        
        };
    
        private class MyCloudPageListAdapter extends CloudPageListAdapter {
        
            public MyCloudPageListAdapter(Context appContext) {
                super(appContext);
            }
        
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                ImageView icon;
                TextView subject;
                TextView time;
            
                LayoutInflater
                mInflater = (LayoutInflater) SDK_ExplorerCloudPageInboxActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
                if (convertView == null) {
                    view = mInflater.inflate(R.layout.cloudpage_list_item, parent, false);
                } else {
                    view = convertView;
                }
            
                subject = (TextView) view.findViewById(R.id.cloudpageSubject);
                icon = (ImageView) view.findViewById(R.id.readUnreadIcon);
                time = (TextView) view.findViewById(R.id.timeTextView);
            
                Message message = (Message) getItem(position);
            
                if(message.getRead()) {
                    icon.setImageResource(R.drawable.read);
                } else {
                    icon.setImageResource(R.drawable.unread);
                }
            
                subject.setText(message.getSubject());
            
                time.setText(android.text.format.DateFormat.format("MMM dd yyyy - hh:mm a", message.getStartDate()));
            
                return view;
            }
        }
    }
~~~ 
<br />    
2. Let the SDK know when your app comes into the foreground as new CloudPage Inbox messages are retrieved whenever your app comes into the foreground. 

> The SDK requires the following code only if your app targets API versions **earlier than Android API 14**. For apps targeting **Android 14 or later**, the SDK will implement these calls using registerActivityLifecycleCallbacks().

~~~ 
    @Override
    protected void onPause() {
        super.onPause();
        
        try {
            // Let JB4A SDK know when each activity paused
            ETPush.activityPaused(this);
        }
        catch (Exception e) {
            if (ETPush.getLogLevel() <= Log.ERROR) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            // Let JB4A SDK know when each activity resumed
            ETPush.activityResumed(this);
        }
        catch (ETException e) {
            if (ETPush.getLogLevel() <= Log.ERROR) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }
~~~ 
