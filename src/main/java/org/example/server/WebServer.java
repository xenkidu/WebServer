package org.example.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class WebServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public static final String RESPONSE_CODE_200 = "200";
    public static final String RESPONSE_CODE_404 = "404";

    public static final String RESPONSE_MESSAGE_200 = "OK";
    public static final String RESPONSE_MESSAGE_404 = "NOT FOUND";
    public static final String RESPONSE_FILE_404 = "404.html";


    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception ex) {
            System.out.println("Exception occurred: ");
            System.out.println(ex.getMessage());
            System.exit(1);
        }

        while (true) {
            try {
                clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String request = in.readLine();

                String urlPath = (request.split(" "))[1].substring(1);
                File file = new File(getFilePath(urlPath));
                if (file.exists()) {
                    out.println(getResponse(RESPONSE_CODE_200,
                            RESPONSE_MESSAGE_200,
                            fileToString(file)));
                } else {
                    System.out.println("Could not open file: " + getFilePath(urlPath));
                    out.println(getResponse(RESPONSE_CODE_404,
                            RESPONSE_MESSAGE_404,
                            fileToString(new File(getFilePath(RESPONSE_FILE_404)))));
                }
                clientSocket.close();
            } catch (Exception ex) {
                System.out.println("Exception occurred while reading: " + ex.getMessage());
                System.exit(1);
            }
        }
    }

    private String getResponse(String responseCode, String responseMessage, String body) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ");
        sb.append(responseCode + " ");
        sb.append(responseMessage + "\n");
        sb.append("Content-Type: text/html\n\n");
        if (!body.isEmpty()) {
            sb.append(body);
        }
        return sb.toString();
    }

    private String getResponse(String responseCode, String responseMessage) {
        return getResponse(responseCode, responseMessage, "");
    }

    private String getFilePath(String file) {
        String currentDirectory = System.getProperty("user.dir");
        return currentDirectory + "\\" + file;
    }

    private String fileToString(File file) {
        try {
            return Files.readString(Path.of(file.toString()));
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "";
        }
    }

}
