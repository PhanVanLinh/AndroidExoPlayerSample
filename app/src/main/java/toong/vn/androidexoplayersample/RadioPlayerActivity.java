package toong.vn.androidexoplayersample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class RadioPlayerActivity extends AppCompatActivity {
    private Button buttonStart;
    private Button buttonStop;
    private ProgressBar progressLoading;

    private SimpleExoPlayer player;
    private PlaybackControlView playbackControlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_player);
        buttonStart = findViewById(R.id.buttonStart);
        buttonStop = findViewById(R.id.buttonStop);
        progressLoading = findViewById(R.id.progressLoading);
        playbackControlView = findViewById(R.id.btnPlayBackControl);

        setupPlayer();
        setupPlayBackControllerView();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.setPlayWhenReady(true);
            }
        });
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.setPlayWhenReady(false);
            }
        });
    }

    private void setupPlayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        TrackSelection.Factory trackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);

        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"),
                        defaultBandwidthMeter);

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(
                "https://ia800405.us.archive.org/34/items/retroistbuckaroobanzai/Retroist-200-Buckaroo-Banzai.ogg"),
                dataSourceFactory, extractorsFactory, null, null);
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        player.prepare(mediaSource);
        player.addListener(new State() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                super.onPlayerStateChanged(playWhenReady, playbackState);
//                if (playbackState == Player.STATE_READY) {
//                    progressLoading.setVisibility(View.GONE);
//                } else {
//                    progressLoading.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                super.onLoadingChanged(isLoading);
                if (!isLoading) {
                    progressLoading.setVisibility(View.GONE);
                } else {
                    progressLoading.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setupPlayBackControllerView() {
        playbackControlView.setPlayer(player);
        playbackControlView.setShowTimeoutMs(
                0); // make playback controller always visible https://github.com/google/ExoPlayer/issues/2501
        playbackControlView.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.setPlayWhenReady(false);
    }

    // create that interface for reduce listener
    abstract class State implements Player.EventListener {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups,
                TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity() {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }
    }
}