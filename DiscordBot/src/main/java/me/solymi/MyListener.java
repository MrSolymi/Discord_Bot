package me.solymi;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MyListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        //String message = event.getMessage().getContentDisplay();
        String message = event.getMessage().getContentRaw();
        System.out.println(message);
        if (message.equals("!ping")) {
            //System.out.println("Ping received!");
            event.getChannel().sendMessage("Pong!").queue();
        }
    }
}
