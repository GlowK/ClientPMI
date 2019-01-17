package com.glowinski;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;

//Klasa obsługująca GUI
public class Controller {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;

    @FXML
    private TextArea chatTextArea;

    @FXML
    private TextField messageTextField;


    @FXML
    public void initialize() {
        try {
            chatTextArea.appendText("Connecting to " + "localhost" + " on port " + 1099 + "...\n");
            socket = new Socket("localhost", 1099);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            outToServer = new ObjectOutputStream(socket.getOutputStream());
            inFromServer = new ObjectInputStream(socket.getInputStream());

            Service service = new Service(this, input, inFromServer);
            service.start();
        }
        catch(IOException exception) {
            printToTextArea("Client error: " + exception.getMessage());
        }
    }

    @FXML
    void focusOnMessageTextField() {
        messageTextField.requestFocus();
    }


    @FXML
    public void onSendMessageButtonClick() {
        if(!messageTextField.getText().isBlank())
            sendMessageToServer();

        messageTextField.clear();
    }

    @FXML
    public void getQuestionClicked(){
        sendMessageToServer("question");
    }


    @FXML
    public void onClearChatButtonClick() {
        chatTextArea.clear();
    }

    private void sendMessageToServer() {
        output.println(messageTextField.getText());
    }
    private void sendMessageToServer(String message){
            output.println(message);
    }

    void printToTextArea(String message) {
        chatTextArea.appendText(message + "\n");
    }
}
