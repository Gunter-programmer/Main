package org.example.semestr4newlaba;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import static org.example.semestr4newlaba.ActionSimulation.*;
import static org.example.semestr4newlaba.ActionsKeyBoard.ActionsKeyBoard;
import static org.example.semestr4newlaba.Habitat.*;
import static org.example.semestr4newlaba.Pets.*;
import static org.example.semestr4newlaba.ThreadFile.loadConfigFile;
import static org.example.semestr4newlaba.TimerSimulation.TimerSimulationLabel;
import static org.example.semestr4newlaba.TimerSimulation.time;
import java.sql.*;
import java.io.IOException;

public class HelloApplication extends Application {
    public static Scene scene;
    public static boolean isSimulationRunning;
    public static long currentTime = 0, elapsedTime = 0;
    public static HelloController controller;
    public static AnimationTimer animationTimer;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Generator pets");
        stage.setScene(scene);
        stage.show();
        new TCPClient().start();
        controller = fxmlLoader.getController();
        ActionsKeyBoard(); //Действия на клавиатуре
        loadConfigFile(); //Подгрузка из файла конфига
        autoWriter(); //Заполнение данных в текстовые поля
        updateClient();
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (isSimulationRunning) {
                    currentTime = System.currentTimeMillis();
                    elapsedTime = currentTime - StartSimulationTime; // Текущее время для проекта
                    TimerSimulationLabel();
                    controller.SimulationTimeLabel.setText("Time " + time);
                    controller.DogCountLabel.setText(String.valueOf("Dog count: " + countDog));
                    controller.CatCountLabel.setText(String.valueOf("Cat count: " + countCat));
                    controller.SimulationArea.getChildren().clear();
                    update();
                    for (Pets pets : petslist) {
                        if(pets.petsImage != null) {
                            pets.petsImage.setTranslateX(pets.x);
                            pets.petsImage.setTranslateY(pets.y);
                            controller.SimulationArea.getChildren().add(pets.petsImage);
                        }
                    }
                }
            }
        };
    }
    public static void main(String[] args) {
        launch();
    }
}