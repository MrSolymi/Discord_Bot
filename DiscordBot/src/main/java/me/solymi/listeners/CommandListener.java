package me.solymi.listeners;

import me.solymi.Bot;
import me.solymi.interfaces.ICommand;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CommandListener extends ListenerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(CommandListener.class);
    private final List<ICommand> commands = new ArrayList<>();

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        for (ICommand command : commands) {
            LOG.info(command.getName() + " command registered");
            event.getJDA()
                    .upsertCommand(command.getName(), command.getDescription())
                    .addOptions(command.getOptions())
                    .setDefaultPermissions(command.getPrivileges())
                    .queue();
        }

        var oldCommands = event.getJDA().retrieveCommands().complete();
        for (Command oldCommand : oldCommands) {
            LOG.info("Checking command: " + oldCommand.getName() + " " + oldCommand.getId());
            boolean found = false;
            for (ICommand iCommand : commands) {
                if (oldCommand.getName().equals(iCommand.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                LOG.info("Deleting command: " + oldCommand.getName());
                event.getJDA().deleteCommandById(oldCommand.getId()).queue();
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (ICommand command : commands) {
            if (event.getName().equals(command.getName())) {
                command.execute(event);
                return;
            }
        }
    }

    public void addCommand(ICommand command) {
        commands.add(command);
    }
}
