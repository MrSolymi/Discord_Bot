package me.solymi.music;

import dev.arbjerg.lavalink.client.AbstractAudioLoadResultHandler;
import dev.arbjerg.lavalink.client.player.*;
import me.solymi.UserData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AudioLoader extends AbstractAudioLoadResultHandler {
    private final SlashCommandInteractionEvent event;
    private final MusicManager musicManager;

    public AudioLoader(SlashCommandInteractionEvent event, MusicManager musicManager) {
        this.event = event;
        this.musicManager = musicManager;
    }
    @Override
    public void loadFailed(@NotNull LoadFailed result) {
        event.getHook().sendMessage("Failed to load track! " + result.getException().getMessage()).queue();
    }

    @Override
    public void noMatches() {
        event.getHook().sendMessage("No matches found for your input!").queue();
    }

    @Override
    public void onPlaylistLoaded(@NotNull PlaylistLoaded playlistLoaded) {
        final int trackCount = playlistLoaded.getTracks().size();

        event.getHook()
                .sendMessage("Added " + trackCount + " tracks to the queue from " + playlistLoaded.getInfo().getName() + "!")
                .queue();

        this.musicManager.scheduler.enqueuePlaylist(playlistLoaded.getTracks());
    }

    @Override
    public void onSearchResultLoaded(@NotNull SearchResult searchResult) {
        final List<Track> tracks = searchResult.getTracks();

        if (tracks.isEmpty()) {
            event.getHook().sendMessage("No tracks found!").queue();
            return;
        }

        final Track firstTrack = tracks.getFirst();

        event.getHook().sendMessage("Adding to queue: " + firstTrack.getInfo().getTitle()).queue();

        this.musicManager.scheduler.enqueue(firstTrack);
    }

    @Override
    public void ontrackLoaded(@NotNull TrackLoaded trackLoaded) {
        final Track track = trackLoaded.getTrack();

        var userData = new UserData(event.getUser().getIdLong());

        track.setUserData(userData);

        this.musicManager.scheduler.enqueue(track);

        final var trackTitle = track.getInfo().getTitle();

        event.getHook().sendMessage("Added to queue: " + trackTitle + "\nRequested by: <@" + userData.requester() + '>').queue();
    }
}
