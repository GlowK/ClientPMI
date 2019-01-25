package com.glowinski;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Service extends Thread {

    private ObjectOutputStream outToServer;
    private ObjectInputStream inputObject;
    private Controller controller;

    Service(Controller controller, ObjectOutputStream outToServer,ObjectInputStream inputObject ) {
        this.controller = controller;
        this.outToServer = outToServer;
        this.inputObject = inputObject;
    }


    @Override
    public void run() {
        try {
            while (true) {
                Object obj;
                obj = (Object) inputObject.readObject();
                if (obj instanceof Question){
                    Question pytanko = (Question) obj;
                    //System.out.println(pytanko.toString());
                    System.out.println("Question received");
                    controller.presentQuestionToUser(pytanko);
                }else if (obj instanceof  Message){
                    Message mes = (Message)obj;
                    System.out.println("Message received");
                    controller.printToTextArea(mes.getMessage());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("Shutdown...");
            //try{
                //controller.getSocket().close();
                controller.onShutdownButtonClicked();
                // You cant throw exeptions if i kill you first :P
            //}catch (Exception e){
            //    e.printStackTrace();
            //}
        }
    }
}
