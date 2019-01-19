package com.glowinski;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//Klasa obsługująca połączenie z serwerem
public class Service extends Thread {

    //private BufferedReader input;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inputObject;
    private Controller controller;

    //Konstruktor
    Service(Controller controller, ObjectOutputStream outToServer,ObjectInputStream inputObject ) {
        this.controller = controller;
        this.outToServer = outToServer;
        this.inputObject = inputObject;
    }

    //Glowne dzialanie klasy obslugujacej połączenie
    @Override
    public void run() {
        //W nieskonczonej petli sluchaj nadawanych wiadomości przez serwer i drukuj je w polu tektowych chatu.
        try {
            //TODO: Check if there's a better way of handling infinite loop
            while (true) {
                //String message = input.readLine();
                //controller.printToTextArea(message);
                try {
                    Object obj;
                    obj = (Object) inputObject.readObject();
                    if (obj instanceof Question){
                        Question pytanko = (Question) obj;
                        System.out.println(pytanko.toString());
                        System.out.println("Question received");
                        controller.addToQuestionsList(pytanko);
                    }else if (obj instanceof  Message){
                        Message mes = (Message)obj;
                        System.out.println("Message received");
                        controller.printToTextArea(mes.getMessage());
                    }
                } catch (NullPointerException e) {
                    System.out.println("NullPointer coz Question = null");
                } catch (ClassNotFoundException | IOException ee) {
                    System.out.println("Service error" + ee);
                }
            }
        }finally {
            System.out.println("Wstawic zamykanie");
        }
           //catch(IOException error) {
           // controller.printToTextArea("Client error: " + error.getMessage());
        //}
    }
}
