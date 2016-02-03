package pl.merskip.ogamemobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment
        extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private SharedPreferences pref;

    private ListPreference serverHostPref;
    private CheckBoxPreference saveLoginPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        serverHostPref = (ListPreference) findPreference("login.serverHost");
        serverHostPref.setOnPreferenceChangeListener(this);

        saveLoginPref = (CheckBoxPreference) findPreference("login.saveLoginAndUniversum");
        saveLoginPref.setOnPreferenceChangeListener(this);

        updateServerHost();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == serverHostPref) {
            serverHostPref.setValue(newValue.toString());
            updateServerHost();
        } else if (preference == saveLoginPref) {
            boolean isEnable = (Boolean) newValue;
            if (!isEnable)
                clearLastLoginAndUniversum();
        }

        return true;
    }

    private void updateServerHost() {
        String language = serverHostPref.getEntry().toString();
        String host = serverHostPref.getValue();
        String text = String.format("%s - %s", language, host);
        serverHostPref.setSummary(text);
    }

    private void clearLastLoginAndUniversum() {
        pref.edit()
                .putString("last_login.login", null)
                .putString("last_login.uniId", null)
                .apply();
    }
}
