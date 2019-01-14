package com.google.android.exoplayer2.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by kjeyapal on 10/10/17.
 */

public class URLInputActivity extends Activity {
  Button mPlayButton;
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.hls_dash_playout);

    mPlayButton = (Button) findViewById(R.id.playButton);
    mPlayButton.setOnClickListener(new OnStartButtonClick(this));


    ImageButton settings = (ImageButton) findViewById(R.id.settings);
    settings.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent modifySettings=new Intent(URLInputActivity.this, SettingsActivity.class);
        startActivity(modifySettings);
      }
    });
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
  }
  private class OnStartButtonClick implements View.OnClickListener {
    Context mContext;
    OnStartButtonClick(Context context) {
      mContext = context;
    }
    @Override
    public void onClick(View v) {
      EditText urlText = (EditText) findViewById(R.id.urlInput);
      String playout_url = urlText.getText().toString();
      Intent intent = new Intent(mContext, PlayerActivity.class);
      intent.setData(Uri.parse(playout_url));
      intent.setAction(PlayerActivity.ACTION_VIEW);
      startActivity(intent);
    }
  }
}