package me.solymi.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.solymi.utilities.SongInfo;
import net.dv8tion.jda.api.entities.User;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private BlockingDeque<SongInfo> queue = new LinkedBlockingDeque<>();
    private User nowPlayingUser = null;
    private boolean isLooping = false;


    public TrackScheduler(AudioPlayer player) {
        this.player = player;
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        super.onPlayerPause(player);
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        super.onPlayerResume(player);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        super.onTrackStart(player, track);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (!queue.isEmpty()){
            SongInfo q = queue.poll();
            player.startTrack(q.getTrack(), false);
            nowPlayingUser = q.getRequester();
            if (isLooping) {
                queue.offer(q);
            }
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        super.onTrackException(player, track, exception);
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        super.onTrackStuck(player, track, thresholdMs);
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs, StackTraceElement[] stackTrace) {
        super.onTrackStuck(player, track, thresholdMs, stackTrace);
    }

    @Override
    public void onEvent(AudioEvent event) {
        super.onEvent(event);
    }

    public void queue(AudioTrack track, User user, boolean priority) {
        if (!player.startTrack(track, true)) {
            if (priority) queue.offerFirst(new SongInfo(track, user));
            else queue.offer(new SongInfo(track, user));
        } else {
            nowPlayingUser = user;
        }
    }

    public BlockingQueue<SongInfo> getQueue() {
        return queue;
    }

    public AudioPlayer getPlayer() {
        return player;
    }
}
