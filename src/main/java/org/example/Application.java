package org.example;

import org.example.server.WebServer;

public class Application {
    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        webServer.start(8080);
    }
}
