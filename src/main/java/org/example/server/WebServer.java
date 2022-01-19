package org.example.server;

import org.example.util.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
    public static final String RESPONSE_FILE_INDEX = "index.html";


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
                File file = new File(Utils.getFilePathFromUrlPath(urlPath));
                outputResponse(file, out);
                clientSocket.close();
            } catch (Exception ex) {
                System.out.println("Exception occurred while reading: " + ex.getMessage());
                System.exit(1);
            }
        }
    }

    private void outputResponse(File file, PrintWriter out) {
        if (file.toString().isBlank()) {
            file = new File(Utils.getFilePathFromUrlPath(RESPONSE_FILE_INDEX));
        }

        if (file.exists()) {
            String okResponse = getResponse(RESPONSE_CODE_200,
                    RESPONSE_MESSAGE_200,
                    Utils.fileToString(file));
            out.println(okResponse);
        } else {
            System.out.println("Could not open file: " + Utils.getFilePathFromUrlPath(file.toString()));
            String notFoundResponse = getResponse(RESPONSE_CODE_404,
                    RESPONSE_MESSAGE_404,
                    Utils.fileToString(new File(Utils.getFilePathFromUrlPath(RESPONSE_FILE_404))));
            out.println(notFoundResponse);
        }

    }

    private String getResponse(String responseCode, String responseMessage, String body) {
        StringBuilder sb = new StringBuilder();
        sb.append(getRequestLine(responseCode, responseMessage));
        sb.append("Content-Type: text/html\n\n");
        if (!body.isEmpty()) {
            sb.append(body);
        }
        return sb.toString();
    }

    private String getRequestLine(String code, String message) {
        return "HTTP/1.1 " +
                code + " " +
                message + "\n";
    }

    private String getResponse(String responseCode, String responseMessage) {
        return getResponse(responseCode, responseMessage, "");
    }
}
