package com.glowinski;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Client extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));


        Controller controller = new Controller();
        loader.setController(controller);
        Parent root = loader.load();

        primaryStage.setTitle("PMI Test");
        primaryStage.setScene(new Scene(root, 450, 700));

        controller.focusOnMessageTextField();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
