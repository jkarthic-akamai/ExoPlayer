/*
 * Copyright (C) 2019 Akamai Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.exoplayer2.demo;

import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.util.EventLogger;

public class LatencyControl extends EventLogger {
  private static final String TAG = "LatencyControl";

  private Player mPlayer;
  private int mtargetBuffersize;

  public LatencyControl(@Nullable MappingTrackSelector trackSelector, int targetBuffersize) {
    super(trackSelector);
    mPlayer = null;
    mtargetBuffersize = targetBuffersize;
  }

  public void setPlayer(Player player) {
    mPlayer = player;
  }

  @Override
  public void onLoadCompleted(
      EventTime eventTime, MediaSourceEventListener.LoadEventInfo loadEventInfo,
      MediaSourceEventListener.MediaLoadData mediaLoadData) {
    if (mPlayer != null) {
      long bufferedPosition = mPlayer.getBufferedPosition();
      long currentPosition = mPlayer.getCurrentPosition();
      long bufferAvailable = (bufferedPosition - currentPosition);
      PlaybackParameters playbackParameters = mPlayer.getPlaybackParameters();
      Log.d(TAG, "BufferedPosition=" + bufferedPosition + " BufferedPercentage" + mPlayer.getBufferedPercentage() +
          " CurrentPosition=" + currentPosition + " bufferAvailable=" + bufferAvailable + " Speed=" + playbackParameters.speed);
      if (bufferAvailable >= mtargetBuffersize + 1000 && playbackParameters.speed == 1.0f) {
        mPlayer.setPlaybackParameters(new PlaybackParameters(1.1f, 1.0f));
      } else if (bufferAvailable <= mtargetBuffersize && playbackParameters.speed > 1.0f) {
        mPlayer.setPlaybackParameters(new PlaybackParameters(1.0f, 1.0f));
      }
    }
  }
}