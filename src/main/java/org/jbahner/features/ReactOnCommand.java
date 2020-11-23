package org.jbahner.features;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.tmi.TwitchMessagingInterface;
import org.jbahner.features.commands.Command;
import org.jbahner.features.commands.FightMessages;

import java.util.List;
import java.util.Random;

public class ReactOnCommand {

    private final TwitchMessagingInterface twitchMessagingInterface;
    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     */
    public ReactOnCommand(SimpleEventHandler eventHandler, TwitchMessagingInterface msgInterface) {
        this.twitchMessagingInterface = msgInterface;
        eventHandler.onEvent(ChannelMessageEvent.class, this::onMessage);
    }

    /**
     * Subscribe to the Message Event
     */
    public void onMessage(ChannelMessageEvent event) {
        System.out.println("[" + event.getChannel().getName() + "] " + event.getUser().getName() + ": " + event.getMessage());
        if(!event.getMessage().startsWith("!") || event.getMessage().length() <= 1) {
            return;
        }
        String message = "";
        switch(Command.valueOf(event.getMessage().toLowerCase().substring(1).split("\\s")[0].toUpperCase())) {
            case GITHUB:
                message = "Github: https://github.com/jbahner/";
                break;
            case FIGHT:
                message = getFightMessage(event.getUser().getName(), event.getChannel().getName());
                break;
        }
        if(!message.isEmpty()) {
            event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
        }
    }

    private String getFightMessage(String user, String channel) {
        Random r = new Random();
        List<String> viewers = twitchMessagingInterface.getChatters(channel).execute().getAllViewers();
        String opponent = viewers.get(r.nextInt(viewers.size()));
        String fightMessage = FightMessages.FIGHT_MESSAGES.get(r.nextInt(FightMessages.FIGHT_MESSAGES.size()));
        return String.format(FightMessages.FIGHTERS, user, opponent) + fightMessage;
    }
}
