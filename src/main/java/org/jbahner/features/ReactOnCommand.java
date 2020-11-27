package org.jbahner.features;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.tmi.TwitchMessagingInterface;
import org.jbahner.features.commands.Command;
import org.jbahner.features.commands.Responses;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReactOnCommand {

    private final TwitchMessagingInterface twitchMessagingInterface;

    private Responses responses;

    private Instant poloTimestamp;
    private int poloCounter = 0;

    /**
     * Register events of this class with the EventManager/EventHandler
     *
     * @param eventHandler SimpleEventHandler
     */
    public ReactOnCommand(SimpleEventHandler eventHandler, TwitchMessagingInterface msgInterface) {
        this.twitchMessagingInterface = msgInterface;
        loadResponses();
        poloTimestamp = Instant.now();
        eventHandler.onEvent(ChannelMessageEvent.class, this::onMessage);
    }

    private void loadResponses() {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("responses/command_responses.yaml");

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            responses = mapper.readValue(is, Responses.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Unable to load Responses ... Exiting.");
            System.exit(1);
        }
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
        switch(Command.valueOf(event.getMessage().substring(1).split("\\s")[0].toUpperCase())) {
            case GITHUB:
                message = responses.getGithub();
                break;
            case CHATBOT:
                message = responses.getChatbot();
                break;
            case KAMPF:
                message = getFightMessage(event.getUser().getName(), event.getChannel().getName());
                break;
            case SCHLONG:
                message = String.format(responses.getSchlong(), event.getUser().getName(), new Random().nextInt(35));
                break;
            case MARCO:
                Instant instant = Instant.now();
                if(Duration.between(poloTimestamp, instant).getSeconds() > 60) {
                    poloCounter = 0;
                    poloTimestamp = instant;
                }
                message = responses.getMarco() +
                        IntStream.range(0, poloCounter).mapToObj(i -> "o").collect(Collectors.joining(""));
                poloCounter++;
                break;
        }
        if(!message.isEmpty()) {
            event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
        }
    }

    private String getFightMessage(String user, String channel) {
        Random r = new Random();
        List<String> viewers = twitchMessagingInterface.getChatters(channel).execute().getAllViewers();
        viewers.remove(user);
        String opponent = viewers.get(r.nextInt(viewers.size()));
        String fightMessage = responses.getKampfMsg().get(r.nextInt(responses.getKampfMsg().size()));
        return String.format(responses.getKampf(), user, opponent) + fightMessage;
    }
}
