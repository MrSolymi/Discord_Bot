package me.solymi.utilities;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.solymi.music.GuildMusicManager;
import me.solymi.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VoiceLogic {
    public static MessageEmbed createSongAddedEmbed(AudioTrackInfo info, User user, Guild guild) {
        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(guild);
        List<SongInfo> queue = new ArrayList<>(guildMusicManager.getTrackScheduler().getQueue());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(queue.isEmpty()? "Song started playing" : "Song added to queue");
        long hours = TimeUnit.MILLISECONDS.toHours(info.length);
        SimpleDateFormat sdf = hours > 0 ? new SimpleDateFormat("hh:mm:ss") : new SimpleDateFormat("mm:ss");
        String formattedLength = sdf.format(new Date(info.length));
        embedBuilder.setDescription("**Name:** `" + info.title + "`\n**Author:** `" + info.author + "`\n**Duration:** `" + formattedLength + "`");
        embedBuilder.setThumbnail("https://img.youtube.com/vi/" + info.identifier + "/0.jpg");
        embedBuilder.setFooter("Requested by " + user.getEffectiveName(), user.getAvatarUrl());
        embedBuilder.setColor(0x008200);
        return embedBuilder.build();
    }
}
