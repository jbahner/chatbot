package org.jbahner.features;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.chat.events.channel.FollowEvent;

public class GreetOnFollow {
    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     */
    public GreetOnFollow(SimpleEventHandler eventHandler) {
        eventHandler.onEvent(FollowEvent.class, this::onFollow);
    }

    /**
     * Subscribe to the Message Event
     */
    public void onFollow(FollowEvent event) {
        String message = String.format("Thanks for the follow %s!", event.getUser().getName());
        event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
    }
}
