package com.glowinski;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.util.List;

//Klasa obsługująca GUI
public class Controller {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private List<Question> questionList;

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }



    @FXML
    private TextField messageTextField;

    @FXML
    private TextArea questionTextArea;
    @FXML
    private TextArea answer1TextArea;
    @FXML
    private TextArea answer2TextArea;
    @FXML
    private TextArea answer3TextArea;
    @FXML
    private TextArea answer4TextArea;
    @FXML
    private TextArea answer5TextArea;

    @FXML
    private Button getQuestion;



    @FXML
    public void initialize() {
        try {
            System.out.println("Connecting to " + "localhost" + " on port " + 1099 + "...");
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
        sendMessageToServer("question++");
        getQuestion.setVisible(false);
    }


    @FXML
    public void onClearChatButtonClick() {
        clearfields();

    }

    private  void clearfields(){
        questionTextArea.clear();
        answer1TextArea.clear();
        answer2TextArea.clear();
        answer3TextArea.clear();
        answer4TextArea.clear();
        answer5TextArea.clear();
    }

    private void sendMessageToServer() {
        output.println(messageTextField.getText());
    }
    private void sendMessageToServer(String message){
            output.println(message);
    }

    void addToQuestionsList(Question q){
        clearfields();
        questionTextArea.appendText(q.getQuestion());
        answer1TextArea.appendText(q.getAnswer1());
        answer2TextArea.appendText(q.getAnswer2());
        answer3TextArea.appendText(q.getAnswer3());
        answer4TextArea.appendText(q.getAnswer4());
        answer5TextArea.appendText(q.getAnswer5());
    }

    void printToTextArea(String message) {
        answer4TextArea.appendText(message + "\n");
    }

}
