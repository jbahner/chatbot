package org.jbahner.features.commands;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Responses {

    private String github;

    private String chatbot;

    private String schlong;

    private String marco;

    private String kampf;

    private List<String> kampfMsg;

    public String getGithub() {
        return github;
    }

    public String getChatbot() {
        return chatbot;
    }

    public String getSchlong() {
        return schlong;
    }

    public String getMarco() {
        return marco;
    }

    public String getKampf() {
        return kampf;
    }

    public List<String> getKampfMsg() {
        return kampfMsg;
    }
}
