package me.solymi.commands;

import me.solymi.interfaces.ICommand;
import me.solymi.music.GuildMusicManager;
import me.solymi.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Skip implements ICommand {

    private final String name = "skip";
    private final String description = "Skip the current song";
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.INTEGER, "amount", "the amount of songs to skip (default = 1)")
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();
        if (!memberVoiceState.inAudioChannel()) {
            event.reply("You need to be in a voice channel to use this command").queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();
        if (!selfVoiceState.inAudioChannel()) {
            event.reply("Bot needs to be in a voice channel to use this command").queue();
            return;
        } else if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            event.reply("You need to be in the same voice channel as me to use this command").queue();
            return;
        }

        int amount = 1;
        try {
            amount = event.getOption("amount").getAsInt();
        } catch (NullPointerException ignored) {}


        if (amount < 1) {
            amount = 1;
            return;
        }


        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());

        for (var item : guildMusicManager.getTrackScheduler().getQueue())
            System.out.println(item.getTrack().getInfo().title);

        if (guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack() == null) {
            event.reply("There is no song playing").queue();
            return;
        } else if (guildMusicManager.getTrackScheduler().getQueue().isEmpty()) {
            guildMusicManager.getTrackScheduler().getPlayer().stopTrack();
            event.reply("Skipped the current song!").queue();
            return;
        } else

        if (amount > guildMusicManager.getTrackScheduler().getQueue().size()) {
            event.reply("There are not enough songs in the queue").queue();
            return;
        }

        if (guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack() == null) {
            event.reply("There is no song playing").queue();
            return;
        }

        int i = amount;

        while (i > 0) {
            guildMusicManager.getTrackScheduler().getPlayer().stopTrack();
            i--;
        }
        if (amount == 1) event.reply("Skipped the current song!").queue();
        else event.reply("Skipped " + amount + " songs!").queue();

    }


}
