package me.solymi.commands;

import dev.arbjerg.lavalink.client.LavalinkClient;
import me.solymi.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Node implements ICommand {
    private final String name = "node";
    private final String description = "What node am I on?";
    private final LavalinkClient client;

    public Node(LavalinkClient client) {
        this.client = client;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();

        if (guild == null) return;

        var link = this.client.getLinkIfCached(guild.getIdLong());

        if (link == null) {
            event.reply("No link for this guild").queue();
            return;
        }

        event.reply("Connected to: " + link.getNode().getName()).queue();
    }
}
