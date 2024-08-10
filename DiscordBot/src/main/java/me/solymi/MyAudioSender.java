package me.solymi;

import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.nio.ByteBuffer;
import java.util.Objects;

public class MyAudioSender extends ListenerAdapter implements AudioSendHandler {
    @Override
    public boolean canProvide() {
        return false;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return null;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;

        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw();
        Guild guild = event.getGuild();
        Member member = event.getMember();


        if (message.equals("!join")) {
            if (member != null && member.getVoiceState() != null && member.getVoiceState().getChannel() != null) {

                VoiceChannel voiceChannel = Objects.requireNonNull(member.getVoiceState().getChannel()).asVoiceChannel();

                // join voice channel
                //guild.getAudioManager().openAudioConnection(voiceChannel);
                event.getJDA().getDirectAudioController().connect(voiceChannel);
            }
        }
        else if (message.equals("!leave")) {
            // leave voice channel
            event.getJDA().getDirectAudioController().disconnect(guild);
        }

    }
}
