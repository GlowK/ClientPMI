package com.glowinski;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
   // private List<Question> questionList;
    private static final int NUMBER_OF_QUESTIONS = 10;
    private int questionNumber = 1;

    //public List<Question> getQuestionList() {
    //    return questionList;
    //}

    //public void setQuestionList(List<Question> questionList) {
    //    this.questionList = questionList;
    //



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
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            outToServer = new ObjectOutputStream(socket.getOutputStream());
            inFromServer = new ObjectInputStream(socket.getInputStream());

            setStartingVisibility();

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
    public void onGetNextQuestionButtonClick() {
        //if(!messageTextField.getText().isBlank())
        //    sendMessageToServer();
        checkAnswers();
        if(questionNumber < NUMBER_OF_QUESTIONS){
            sendMessageToServer("question++");
            questionNumber++;
            System.out.println(questionNumber);
        }else{
            getNextQuestion.setVisible(false);
            endTest.setVisible(true);

        }

        //messageTextField.clear();
    }

    @FXML
    public void getQuestionClicked(){
        sendMessageToServer("question++");
        getQuestion.setVisible(false);
        getNextQuestion.setVisible(true);
    }


    @FXML
    public void onEndTestButtonClick() {
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

    private void setStartingVisibility(){
        getNextQuestion.setVisible(false);
        endTest.setVisible(false);
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

    void addToQuestionsList(Question q){
        clearfields();
        setCBvisibilityPerQuestion(q);
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
        System.out.println(answer.toString());
        return answer;
    }

    private void setCBtoFalse(){
        cbA1.setSelected(false);
        cbA2.setSelected(false);
        cbA3.setSelected(false);
        cbA4.setSelected(false);
        cbA5.setSelected(false);
    }

}
