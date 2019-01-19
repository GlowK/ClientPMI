package com.glowinski;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Controller {

    private Socket socket;

    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private ArrayList<Answer> answerArrayList = new ArrayList<>();
    private static final int NUMBER_OF_QUESTIONS = 10;
    private int questionNumber = 1;

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
    private TextArea messageTextField2;

    @FXML
    private Button getQuestion;
    @FXML
    private Button getNextQuestion;
    @FXML
    private Button endTest;

    @FXML
    private CheckBox cbA1;
    @FXML
    private CheckBox cbA2;
    @FXML
    private CheckBox cbA3;
    @FXML
    private CheckBox cbA4;
    @FXML
    private CheckBox cbA5;

    @FXML
    public void initialize() {
        try {
            System.out.println("Connecting to " + "localhost" + " on port " + 1099 + "...");
            socket = new Socket("localhost", 1099);
            outToServer = new ObjectOutputStream(socket.getOutputStream());
            inFromServer = new ObjectInputStream(socket.getInputStream());

            setStartingVisibility();
            messageTextField2.appendText("Welcome to PMI test. Click Start to begin.");

            Service service = new Service(this, outToServer, inFromServer);
            service.start();
        }
        catch(IOException exception) {
            printToTextArea("Client error: " + exception.getMessage());
        }
    }

    @FXML
    public void onGetNextQuestionButtonClick() {
        answerArrayList.add(checkAnswers());
        if(questionNumber < NUMBER_OF_QUESTIONS){
            Message mes = new Message("question++");
            sendMessageToServer(mes);
            questionNumber++;
            //System.out.println(questionNumber);
            if(questionNumber == NUMBER_OF_QUESTIONS){
                getNextQuestion.textProperty().set("Finish test");
            }
        }else{
            getNextQuestion.setVisible(false);
            endTest.setVisible(true);

        }
    }

    @FXML
    public void getQuestionClicked(){
        Message mes = new Message("question++");
        //System.out.println(mes.getMessage());
        sendMessageToServer(mes);
        getQuestion.setVisible(false);
        getNextQuestion.setVisible(true);
        messageTextField2.appendText("Question 1/" + NUMBER_OF_QUESTIONS + " presented.");
    }


    @FXML
    public void onEndTestButtonClick() {
        clearfields();
        hideCBs();
        System.out.println("Drukuje przesylane odpowiedzi: ");
        for(Answer ans: answerArrayList){
            System.out.println(ans.toString());
            sendAnswerToServer(ans);
            try {
                Thread.sleep(100);
            }catch(Exception e){
                System.out.println(e);
            }
        }
        Message mes = new Message("check");
        sendMessageToServer(mes);
    }

    private  void clearfields(){
        questionTextArea.clear();
        answer1TextArea.clear();
        answer2TextArea.clear();
        answer3TextArea.clear();
        answer4TextArea.clear();
        answer5TextArea.clear();
        messageTextField2.clear();
    }

    private void sendMessageToServer(Message message){
        try {
            outToServer.writeObject(message);
            outToServer.reset();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private void sendAnswerToServer(Answer answer){
        try {
            outToServer.writeObject(answer);
            outToServer.reset();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private void setStartingVisibility(){
        getNextQuestion.setVisible(false);
        endTest.setVisible(false);
        cbA1.setVisible(false);
        cbA2.setVisible(false);
        cbA3.setVisible(false);
        cbA4.setVisible(false);
        cbA5.setVisible(false);

    }

    private void hideCBs(){
        cbA1.setVisible(false);
        cbA2.setVisible(false);
        cbA3.setVisible(false);
        cbA4.setVisible(false);
        cbA5.setVisible(false);
    }

    private void setCBvisibilityPerQuestion(Question q){
        setCBVisibility(q.getAnswer1(), cbA1);
        setCBVisibility(q.getAnswer2(), cbA2);
        setCBVisibility(q.getAnswer3(), cbA3);
        setCBVisibility(q.getAnswer4(), cbA4);
        setCBVisibility(q.getAnswer5(), cbA5);
    }

    void presentQuestionToUser(Question q){
        clearfields();
        messageTextField2.appendText("Question: " + questionNumber +"/" + NUMBER_OF_QUESTIONS + " presented.");
        setCBvisibilityPerQuestion(q);
        questionTextArea.appendText(q.getQuestion());
        answer1TextArea.appendText(q.getAnswer1());
        answer2TextArea.appendText(q.getAnswer2());
        answer3TextArea.appendText(q.getAnswer3());
        answer4TextArea.appendText(q.getAnswer4());
        answer5TextArea.appendText(q.getAnswer5());

    }

    void printToTextArea(String message) {
        messageTextField2.appendText(message + "\n");
    }

    private void setCBVisibility(String a, CheckBox cb){
        if(a.isEmpty()){
            cb.setVisible(false);
        }else{
            cb.setVisible(true);
        }
    }

    private Answer checkAnswers(){
        Answer answer = new Answer();
        answer.setA1(cbA1.isSelected());
        answer.setA2(cbA2.isSelected());
        answer.setA3(cbA3.isSelected());
        answer.setA4(cbA4.isSelected());
        answer.setA5(cbA5.isSelected());
        setCBtoFalse();
        System.out.println("Answer sent.");
        //System.out.println(answer.toString());
        return answer;
    }

    private void setCBtoFalse(){
        cbA1.setSelected(false);
        cbA2.setSelected(false);
        cbA3.setSelected(false);
        cbA4.setSelected(false);
        cbA5.setSelected(false);
    }

    public Socket getSocket() {
        return socket;
    }

}
