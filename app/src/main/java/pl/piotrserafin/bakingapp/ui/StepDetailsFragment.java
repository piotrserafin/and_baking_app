package pl.piotrserafin.bakingapp.ui;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.piotrserafin.bakingapp.R;
import pl.piotrserafin.bakingapp.model.Step;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailsFragment extends Fragment implements Player.EventListener {

    private static final String TAG = StepDetailsFragment.class.getSimpleName();

    @BindView(R.id.step_player)
    PlayerView playerView;

    @BindView(R.id.step_thumbnail)
    ImageView stepThumbnail;

    @BindView(R.id.step_description)
    TextView stepDescription;

    private Step step;

    private SimpleExoPlayer player;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder playbackStateBuilder;

    private long playerCurrentPosition = 0;
    private boolean playerPlayWhenReady = true;

    private Unbinder unbinder;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(getString(R.string.step_key))) {
            step = getArguments().getParcelable(getString(R.string.step_key));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.player_position_key))) {
            playerCurrentPosition = savedInstanceState.getLong(getString(R.string.player_position_key));
            playerPlayWhenReady = savedInstanceState.getBoolean(getString(R.string.player_play_when_ready_key));
        }

        unbinder = ButterKnife.bind(this, rootView);

        if (step != null) {
            stepDescription.setText(step.getDescription());

            if(!step.getThumbnailURL().isEmpty()) {
                Picasso.get()
                        .load(step.getThumbnailURL())
                        .placeholder(R.drawable.ic_baseline_room_service_black)
                        .into(stepThumbnail);

                stepThumbnail.setVisibility(View.VISIBLE);
            }
        }

        if(!TextUtils.isEmpty(step.getVideoURL())) {
            initializeMediaSession();
            initializePlayer(Uri.parse(step.getVideoURL()));
            playerView.setVisibility(View.VISIBLE);
        } else {
            playerView.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(getString(R.string.player_position_key), playerCurrentPosition);
        outState.putBoolean(getString(R.string.player_play_when_ready_key), playerPlayWhenReady);
    }

    private void initializeMediaSession() {

        mediaSession = new MediaSessionCompat(getContext(), TAG);

        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);

        playbackStateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(playbackStateBuilder.build());
        mediaSession.setCallback(new StepPlayerSessionCallback());

        mediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (player == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();

            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            playerView.setPlayer(player);

            player.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultDataSourceFactory(getContext(), userAgent)).createMediaSource(mediaUri);

            player.prepare(mediaSource);
            // onRestore
            if (playerCurrentPosition != 0)
                player.seekTo(playerCurrentPosition);

            player.setPlayWhenReady(playerPlayWhenReady);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playerCurrentPosition = player.getCurrentPosition();
            playerPlayWhenReady = player.getPlayWhenReady();

            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == Player.STATE_READY) && playWhenReady){
            playbackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    player.getCurrentPosition(), 1f);
        } else if((playbackState == Player.STATE_READY)){
            playbackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    player.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(playbackStateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    private class StepPlayerSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            player.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            player.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            player.seekTo(0);
        }
    }
}
