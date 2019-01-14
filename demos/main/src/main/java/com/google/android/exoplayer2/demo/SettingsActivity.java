package com.google.android.exoplayer2.demo;

import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Map;

public class SettingsActivity extends PreferenceActivity {
  private String summary_prefix;
  private EditTextPreference mtargetBuffersize;

  private void setSummary(Preference preference, Object newValue) {
    preference.setSummary(summary_prefix + newValue.toString());
  }

  private class checkBounds implements Preference.OnPreferenceChangeListener {
    protected int min_value;
    protected int max_value;
    checkBounds(int min_id, int max_id) {
      min_value = Integer.parseInt(getResources().getString(min_id));
      max_value = Integer.parseInt(getResources().getString(max_id));
    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
      float val;
      try {
        val = Integer.parseInt(newValue.toString());
        if ((val >= min_value) && (val <= max_value)) {
          setSummary(preference, newValue);
          return true;
        }
      } catch (NumberFormatException e) {
        // Let's throw the out of bounds error
      }
      // invalid you can show invalid message
      Toast.makeText(getApplicationContext(), "Input out of bounds. Enter value between " + min_value + " and " + max_value, Toast.LENGTH_LONG).show();
      return false;

    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preferences);
    summary_prefix = getResources().getString(R.string.summary_prefix);
    Map<String,?> keys = PreferenceManager.getDefaultSharedPreferences(this).getAll();

    for(Map.Entry<String,?> entry : keys.entrySet()) {
      final Preference pref = findPreference(entry.getKey());
      setSummary(pref, entry.getValue());
    }

    mtargetBuffersize = (EditTextPreference)findPreference(getResources().getString(R.string.key_target_buffersize));
    mtargetBuffersize.setOnPreferenceChangeListener(new checkBounds(R.string.min_target_buffersize, R.string.max_target_buffersize));
  }
}