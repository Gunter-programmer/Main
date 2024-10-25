package org.example.semestr4newlaba;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
import static org.example.semestr4newlaba.BaseAI.speedCat;
import static org.example.semestr4newlaba.BaseAI.speedDog;
import static org.example.semestr4newlaba.Habitat.*;
import static org.example.semestr4newlaba.HelloApplication.*;

public class ActionSimulation {
    public static long StartSimulationTime; //Начало симуляции исходя из 1970г
    static public boolean resetObject = true;
    public static ArrayList<Pets> petsChanges = new ArrayList<>();
    static void StartSimulation() {
        if (!isSimulationRunning) {
            elapsedTime = 0;
            controller.ButtonStop.setDisable(false);
            controller.MenuButtonStop.setDisable(false);
            controller.ButtonStart.setDisable(true);
            controller.MenuButtonStart.setDisable(true);
            timeCheckCat = timeSpawnEverytimeCat;
            timeCheckDog = timeSpawnEverytimeDog;
            countCat = 0;
            countDog = 0;
            for (Pets pets : petslist) {
                if(pets instanceof Cat) countCat++;
                if(pets instanceof Dog) countDog++;
            }
            ReadTextArea();
            isSimulationRunning = true;
            StartSimulationTime = System.currentTimeMillis();
            animationTimer.start();
            controller.DogCountLabel.setVisible(false);
            controller.CatCountLabel.setVisible(false);
        }
    }

    static void StopSimulation() {
        if (isSimulationRunning) {
            threadPool.shutdown();
            animationTimer.stop();
            isSimulationRunning = false;
            if(controller.ShowInformationBox.isSelected() && resetObject){
                long SimulationStopTime = System.currentTimeMillis();
                // Создаем диалоговое окно
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Simulation information");
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                TextArea textArea = new TextArea();
                textArea.setEditable(false);
                textArea.setPrefSize(300, 200);
                // Выводим информацию о количестве и типе сгенерированных объектов и времени симуляции
                StringBuilder info = new StringBuilder();
                info.append("Number of cats: ").append(countCat).append("\n");
                info.append("Number of dogs: ").append(countDog).append("\n");
                info.append("Simulation time: ").append(controller.SimulationTimeLabel.getText());
                textArea.setText(info.toString());
                // Создаем кнопки "OK" и "Отмена"
                Button okButton = new Button("Продолжить");
                Button cancelButton = new Button("Остановить");
                // Создаем layout для кнопок
                HBox buttonBox = new HBox(10, okButton, cancelButton);
                buttonBox.setAlignment(Pos.CENTER);
                // Создаем layout для диалогового окна
                VBox dialogLayout = new VBox(10, textArea, buttonBox);
                dialogLayout.setAlignment(Pos.CENTER);
                // Добавляем layout в сцену и показываем диалоговое окно
                Scene dialogScene = new Scene(dialogLayout);
                dialogStage.setScene(dialogScene);
                dialogStage.show();
                okButton.setOnAction(event1 -> { //Продолжение симуляции
                    dialogStage.close();
                    animationTimer.start();
                    isSimulationRunning = true;
                    currentTime = System.currentTimeMillis();
                    StartSimulationTime = StartSimulationTime + (currentTime - SimulationStopTime);
                });
                cancelButton.setOnAction(event1 -> { //Остановка симуляции
                    dialogStage.close();
                    controller.ButtonStop.setDisable(true);
                    controller.MenuButtonStop.setDisable(true);
                    controller.ButtonStart.setDisable(false);
                    controller.MenuButtonStart.setDisable(false);
                    controller.DogCountLabel.setVisible(true);
                    controller.CatCountLabel.setVisible(true);
                    petslist.clear();
                    TreeSet.clear();
                    HashMap.clear();
                });
            }
            else {
                controller.ButtonStop.setDisable(true);
                controller.MenuButtonStop.setDisable(true);
                controller.ButtonStart.setDisable(false);
                controller.MenuButtonStart.setDisable(false);
                controller.DogCountLabel.setVisible(true);
                controller.CatCountLabel.setVisible(true);
                petslist.clear();
                TreeSet.clear();
                HashMap.clear();
            }
        }
    }
    static void autoWriter(){
        controller.TextFieldPeriodCat.setText(String.valueOf(timeSpawnEverytimeCat));
        controller.TextFieldPeriodDog.setText(String.valueOf(timeSpawnEverytimeDog));
        controller.CatSpawnField.setText(String.valueOf(timeLifeCat));
        controller.DogSpawnField.setText(String.valueOf(timeLifeDog));
        controller.CatSpeedField.setText(String.valueOf(speedCat));
        controller.DogSpeedField.setText(String.valueOf(speedDog));
        controller.ComboBoxCat.getSelectionModel().select(chanceSpawnCat);
        controller.ComboBoxDog.getSelectionModel().select(chanceSpawnDog);
    }
    static void ContinueSimulation(){
        isSimulationRunning = true;
        long TimeEndExpectation = System.currentTimeMillis();
        animationTimer.start();
    }
    static void ReadTextArea() { //Чтение полей TextField и ComboBox
        String PeriodCatStr = controller.TextFieldPeriodCat.getText();
        String PeriodDogStr = controller.TextFieldPeriodDog.getText();
        String TimeLifeCatStr = controller.CatSpawnField.getText();
        String TimeLifeDogStr = controller.DogSpawnField.getText();
        String SpeedCatStr = controller.CatSpeedField.getText();
        String SpeedDogStr = controller.DogSpeedField.getText();
        int temp;
        double tempDouble;
        long temp1;
        if (!PeriodCatStr.isEmpty()) {
            try {
                temp = Integer.parseInt(PeriodCatStr);
                if(temp > 0) {
                    timeSpawnEverytimeCat = temp;
                    timeCheckCat = elapsedTime + timeSpawnEverytimeCat;
                }
                else{
                    exception();
                }
            } catch (NumberFormatException ex) {
                exception();
            }
        }
        if (!PeriodDogStr.isEmpty()) {
            try {
                temp = Integer.parseInt(PeriodDogStr);
                if(temp > 0) {
                    timeSpawnEverytimeDog = temp;
                    timeCheckDog = elapsedTime + timeSpawnEverytimeDog;
                }
                else {
                    exception();
                }
            } catch (NumberFormatException ex) {
                exception();
            }
        }
        if(!controller.ComboBoxCat.getSelectionModel().isEmpty()){
            chanceSpawnCat = controller.ComboBoxCat.getSelectionModel().getSelectedItem();
        }
        if(!controller.ComboBoxDog.getSelectionModel().isEmpty()){
            chanceSpawnDog = controller.ComboBoxDog.getSelectionModel().getSelectedItem();
        }
        if (!TimeLifeCatStr.isEmpty()) {
            try {
                temp1 = Long.parseLong(TimeLifeCatStr);
                if(temp1>0){
                    timeLifeCat = temp1;
                }
                else{
                    exception();
                }
            } catch (NumberFormatException ex) {
                exception();
            }
        }
        if (!TimeLifeDogStr.isEmpty()) {
            try {
                temp1 = Long.parseLong(TimeLifeDogStr);
                if(temp1>0){
                    timeLifeDog = temp1;
                }
                else{
                    exception();
                }
            } catch (NumberFormatException ex) {
                exception();
            }
        }
        if (!SpeedCatStr.isEmpty()) {
            try {
                tempDouble = Double.parseDouble(SpeedCatStr);
                if(tempDouble > 0) {
                    speedCat = tempDouble;
                }
                else {
                    exception();
                }
            } catch (NumberFormatException ex) {
                exception();
            }
        }
        if (!SpeedDogStr.isEmpty()) {
            try {
                tempDouble = Double.parseDouble(SpeedDogStr);
                if(tempDouble > 0) {
                    speedDog = tempDouble;
                }
                else {
                    exception();
                }
            } catch (NumberFormatException ex) {
                exception();
            }
        }
    }
    static void exception() {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Simulation information");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefSize(200, 150);
        // Выводим информацию о количестве и типе сгенерированных объектов и времени симуляции
        StringBuilder info = new StringBuilder();
        info.append("Ошибка!");
        Font font = Font.font("Arial", FontWeight.BOLD, 25);
        textArea.setFont(font);
        textArea.setText(info.toString());
        Button okButton = new Button("ОК");
        // Создаем layout для кнопок
        HBox buttonBox = new HBox(10, okButton);
        buttonBox.setAlignment(Pos.CENTER);
        // Создаем layout для диалогового окна
        VBox dialogLayout = new VBox(10, textArea, buttonBox);
        dialogLayout.setAlignment(Pos.CENTER);
        // Добавляем layout в сцену и показываем диалоговое окно
        Scene dialogScene = new Scene(dialogLayout);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
        okButton.setOnAction(event ->{
            dialogStage.close();
        }
        );
    }
    static void showPets(){
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Текущие объекты");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefSize(400, 400);
        StringBuilder info = new StringBuilder();
        int tempNumber = 0;
        String animalType = "";
        for (Pets pets : petslist) {
            tempNumber++;
            if (pets instanceof Cat) {
                animalType = "Кошка";
            } else if (pets instanceof Dog) {
                animalType = "Собака";
            }
            info.append(tempNumber + ") Животное = " + animalType + ", Время рождения = " +
                    HashMap.get(pets.PetsId) + ", Id = " + pets.PetsId + "\n");
        }
        Font font = Font.font("Arial", FontWeight.BOLD, 12);
        textArea.setFont(font);
        textArea.setText(info.toString());
        Button okButton = new Button("ОК");
        // Создаем layout для кнопок
        HBox buttonBox = new HBox(10, okButton);
        buttonBox.setAlignment(Pos.CENTER);
        // Создаем layout для диалогового окна
        VBox dialogLayout = new VBox(10, textArea, buttonBox);
        dialogLayout.setAlignment(Pos.CENTER);
        // Добавляем layout в сцену и показываем диалоговое окно
        Scene dialogScene = new Scene(dialogLayout);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
        okButton.setOnAction(event ->{
                    dialogStage.close();
                }
        );
    }
    static void exceptionConsole(String a) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Крах!");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefSize(300, 120);
        // Выводим информацию о количестве и типе сгенерированных объектов и времени симуляции
        StringBuilder info = new StringBuilder();
        info.append(a);
        Font font = Font.font("Arial", FontWeight.BOLD, 15);
        textArea.setFont(font);
        textArea.setText(info.toString());
        Button okButton = new Button("ОК");
        // Создаем layout для кнопок
        HBox buttonBox = new HBox(10, okButton);
        buttonBox.setAlignment(Pos.CENTER);
        // Создаем layout для диалогового окна
        VBox dialogLayout = new VBox(10, textArea, buttonBox);
        dialogLayout.setAlignment(Pos.CENTER);
        // Добавляем layout в сцену и показываем диалоговое окно
        Scene dialogScene = new Scene(dialogLayout);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
        okButton.setOnAction(event ->{
                    dialogStage.close();
                }
        );
    }
    static public void updateClient(){
        controller.MenuClient.getItems().clear();
        if(clientList.isEmpty()){
            return;
        }
        System.out.println(clientList);
        MenuItem menuItem = new MenuItem("Мой клиент");
        menuItem.setDisable(true);
        controller.MenuClient.getItems().add(menuItem);
        clientList.forEach(client -> {
            if(!client.equals(clientPort)){
                MenuItem clientItem = new MenuItem(client);
                clientItem.setOnAction(event -> { //Отправка кошек нажатому клиенту
                    try {
                        outputStream.writeObject(client);
                        outputStream.writeObject(getCats());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                controller.MenuClient.getItems().add(clientItem);
            }
        });
    }
    public static ArrayList<Pets> getCats(){
        return (ArrayList<Pets>) petslist.stream().filter(pets -> {
            if(pets instanceof Cat){
                return true;
            }
            else return false;
        }).collect(Collectors.toList());
    }
    public static ArrayList<Pets> getDogs(){
        return (ArrayList<Pets>) petslist.stream().filter(pets -> {
            if(pets instanceof Dog){
                return true;
            }
            else return false;
        }).collect(Collectors.toList());
    }
}