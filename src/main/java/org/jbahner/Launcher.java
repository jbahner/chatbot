package org.jbahner;

public class Launcher {
    public static void main(String[] args) {
        Bot bot = new Bot();
        bot.registerFeatures();
        bot.start();
    }
}