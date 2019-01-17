package com.glowinski;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;

//Klasa obsługująca połączenie z serwerem
public class Service extends Thread {

    private BufferedReader input;
    private ObjectInputStream inputObject;
    private Controller controller;

    //Konstruktor
    Service(Controller controller, BufferedReader input,ObjectInputStream inputObject ) {
        this.controller = controller;
        this.input = input;
        this.inputObject = inputObject;
    }

    //Glowne dzialanie klasy obslugujacej połączenie
    @Override
    public void run() {
        //W nieskonczonej petli sluchaj nadawanych wiadomości przez serwer i drukuj je w polu tektowych chatu.
        //try {
            //TODO: Check if there's a better way of handling infinite loop
            while(true) {
                //String message = input.readLine();
                //controller.printToTextArea(message);
                try {
                    Question pytanko = (Question)inputObject.readObject();
                    System.out.println(pytanko.toString());
                    controller.addToQuestionsList(pytanko);
                }catch (Exception e){
                    System.out.println(e);
                }

            }
      //  }
        //catch(IOException error) {
           // controller.printToTextArea("Client error: " + error.getMessage());
        //}
    }
}
