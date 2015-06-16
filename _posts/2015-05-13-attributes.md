---
layout: page
title: "Attributes"
subtitle: "Using Attributes"
category: features
date: 2015-05-14 12:00:00
order: 2
---
To implement segmentation by attributes, include code to reference attributes in the app. Any attributes you save with the SDK must be added to your Marketing Cloud Contact record in advance so that the Marketing Cloud can connect the values sent by the SDK to the correct Contact fields.

This example (taken from the <a href="https://github.com/ExactTarget/JB4A-SDK-Android-Beta/tree/beta/JB4A-SDK-Explorer" target="_blank">Journey Builder for Apps SDK Explorer (Beta) for Android</a>) uses a PreferenceActivity to allow your users to enter or change attributes in their Contact record (that have been previously setup) for each user of your Mobile App:

The ETPush.getInstance().addAttribute() method will create a new registration record and send it to the Marketing Cloud.  It will take up to 15 minutes for this value to be recorded in the Contact record.  If the internet is not available when the update is made, the SDK will save the update and send whenever the network becomes available.

~~~ 
public class SettingsActivity extends PreferenceActivity {
    private static final String TAG = "SettingsActivity";
    …
    …
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    …
    …
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //
        // FIRST NAME PREFERENCE
        //
        final Preference fnPref = findPreference(CONSTS.KEY_PREF_FIRST_NAME);
        if (!sp.getString(CONSTS.KEY_PREF_FIRST_NAME, "").isEmpty()) {
            fnPref.setSummary(sp.getString(CONSTS.KEY_PREF_FIRST_NAME, ""));
        }

        fnPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                PreferenceScreen prefSet = getPreferenceScreen();

                final EditTextPreference fnETP = (EditTextPreference) prefSet.findPreference(CONSTS.KEY_PREF_FIRST_NAME);

                final AlertDialog d = (AlertDialog) fnETP.getDialog();
                final EditText fnET = fnETP.getEditText();
                fnET.setText(sp.getString(CONSTS.KEY_PREF_FIRST_NAME, ""));

                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(android.view.View v) {
                        String newFirstName = fnET.getText().toString().trim();
                        if (newFirstName.isEmpty()) {
                            Utils.flashError(fnET, getString(R.string.error_cannot_be_blank));
                            return;
                        } else {

                            // save the preference to Shared Preferences
                            updatePreferencesForKey(CONSTS.KEY_PREF_FIRST_NAME, newFirstName);
                            fnPref.setSummary(newFirstName);

                            try {
                                ETPush.getInstance().addAttribute(CONSTS.KEY_ATTRIB_FIRST_NAME, newFirstName);
                            } catch (ETException e) {
                                if (ETPush.getLogLevel() <= Log.ERROR) {
                                    Log.e(TAG, e.getMessage(), e);
                                }
                            }
                        }

                        d.dismiss();
                    }
                });

                return true;
            }
        });
    }
    …
    …
}
~~~ 
