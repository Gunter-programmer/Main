package org.example.semestr4newlaba;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadPoolExecutor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import static org.example.semestr4newlaba.ActionSimulation.*;
import static org.example.semestr4newlaba.CatAI.moveCat;
import static org.example.semestr4newlaba.DogAI.moveDog;
import static org.example.semestr4newlaba.Habitat.*;
import static org.example.semestr4newlaba.HelloApplication.*;
import static org.example.semestr4newlaba.PetsSQL.*;
import static org.example.semestr4newlaba.ThreadFile.*;
import static org.example.semestr4newlaba.TimerSimulation.TimerSimulationLabel;
import static org.example.semestr4newlaba.TimerSimulation.time;

public class HelloController {

    @FXML
    public ResourceBundle resources;

    @FXML
    public URL location;

    @FXML
    public Button ButtonStart;

    @FXML
    public Button ButtonStop;

    @FXML
    public Label CatCountLabel;

    @FXML
    public TextField CatSpawnField;

    @FXML
    public ComboBox<Double> ComboBoxCat;

    @FXML
    public ComboBox<Double> ComboBoxDog;

    @FXML
    public Label DogCountLabel;

    @FXML
    public TextField DogSpawnField;

    @FXML
    public MenuItem MenuButtonStart;

    @FXML
    public MenuItem MenuButtonStop;

    @FXML
    public RadioMenuItem MenuRadioHideTime;

    @FXML
    public RadioMenuItem MenuRadioShowTime;

    @FXML
    public CheckMenuItem MenuShowInformationBox;

    @FXML
    public ToggleGroup RadioButton;

    @FXML
    public RadioButton RadioHideTime;

    @FXML
    public RadioButton RadioShowTime;

    @FXML
    public CheckBox ShowInformationBox;

    @FXML
    public Pane SimulationArea;

    @FXML
    public Label SimulationTimeLabel;

    @FXML
    public TextField TextFieldPeriodCat;

    @FXML
    public TextField TextFieldPeriodDog;
    @FXML
    public Button ShowPetsButton;
    @FXML
    public Button UpdateButton;
    @FXML
    public CheckBox CatAIBox;
    @FXML
    public CheckBox DogAIBox;
    @FXML
    public TextField CatSpeedField;
    @FXML
    public TextField DogSpeedField;
    @FXML
    public CheckMenuItem checkGenerationCat;

    @FXML
    public CheckMenuItem checkGenerationDog;
    @FXML
    public RadioMenuItem RBMainAverageprioritet;

    @FXML
    public RadioMenuItem RBMainMaxprioritet;

    @FXML
    public RadioMenuItem RBMainMinprioritet;

    @FXML
    public RadioMenuItem DogRBMaxPrioritet;

    @FXML
    public RadioMenuItem DogRBMinPrioritet;
    @FXML
    public RadioMenuItem DogRBAveragePrioritet;

    @FXML
    private MenuItem FileLoadButton;

    @FXML
    public Menu MenuClient;

    @FXML
    private MenuItem FileSaveButton;
    @FXML
    private MenuItem ConsoleButton;
    @FXML
    public Menu DataBases;
    @FXML
    public MenuItem LoadBDAll;

    @FXML
    public MenuItem LoadBDCat;

    @FXML
    public MenuItem LoadBDDog;
    @FXML
    private MenuItem ReadBDAll;

    @FXML
    private MenuItem ReadBDCat;

    @FXML
    private MenuItem ReadBDDog;
    @FXML
    void ButtonStart(ActionEvent event) {
        StartSimulation();
    }

    @FXML
    void ButtonStop(ActionEvent event) {
        StopSimulation();
    }
    @FXML
    void ShowPetsButton(ActionEvent event) {
        showPets();
    }
    @FXML
    void CatSpawnField(ActionEvent event) {

    }
    @FXML
    void ComboBoxCat(ActionEvent event) {
    }

    @FXML
    void ComboBoxDog(ActionEvent event) {

    }
    @FXML
    void DogSpawnField(ActionEvent event) {

    }

    @FXML
    void RadioButtonAction(ActionEvent event) {
        if (RadioShowTime.isSelected() || MenuRadioShowTime.isSelected()) {
            SimulationTimeLabel.setVisible(true);
            MenuRadioHideTime.setSelected(false);
            MenuRadioHideTime.setDisable(false);
            MenuRadioShowTime.setDisable(true);
            RadioShowTime.fire();
        } else if (RadioHideTime.isSelected() || MenuRadioHideTime.isSelected()) {
            SimulationTimeLabel.setVisible(false);
            MenuRadioShowTime.setSelected(false);
            MenuRadioHideTime.setDisable(true);
            MenuRadioShowTime.setDisable(false);
            RadioHideTime.fire();
        }
    }
    @FXML
    void ShowInformationBox(ActionEvent event) {
        if(ShowInformationBox.isSelected()){
            MenuShowInformationBox.setSelected(true);
        }
        else{
            MenuShowInformationBox.setSelected(false);
        }
    }
    @FXML
    void MenuShowInformationBox(ActionEvent event) {
        if(MenuShowInformationBox.isSelected()){
            ShowInformationBox.setSelected(true);
        }
        else{
            ShowInformationBox.setSelected(false);
        }
    }
    @FXML
    void TextFieldPeriodCat(ActionEvent event) {

    }

    @FXML
    void TextFieldPeriodDog(ActionEvent event) {

    }

    @FXML
    void UpdateButton(ActionEvent event) {
        ReadTextArea();
    }
    @FXML
    void CatAIAction(ActionEvent event) {
        if(CatAIBox.isSelected()){
            moveCat = true;
        }
        else{
            moveCat = false;
        }
    }
    @FXML
    void DogAIAction(ActionEvent event) {
        if(DogAIBox.isSelected()){
             moveDog = true;
        }
        else{
             moveDog = false;
        }
    }
    @FXML
    void CatSpeedField(ActionEvent event) {

    }
    @FXML
    void DogSpeedField(ActionEvent event) {

    }
    @FXML
    void checkGenerationCat(ActionEvent event) {
        if(checkGenerationCat.isSelected()){
            boolGenerationCat = true;
        }
        else{
             boolGenerationCat = false;
        }
    }
    @FXML
    void checkGenerationDog(ActionEvent event) {
        if(checkGenerationDog.isSelected()){
             boolGenerationDog = true;
        }
        else{
             boolGenerationDog = false;
        }
    }
    @FXML
    void MaxprioritetMainThread(ActionEvent event) {
        RBMainMaxprioritet.setDisable(true);
        RBMainAverageprioritet.setSelected(false);
        RBMainAverageprioritet.setDisable(false);
        RBMainMinprioritet.setSelected(false);
        RBMainMinprioritet.setDisable(false);
    }
    @FXML
    void AverageprioritetMainThread(ActionEvent event) {
        RBMainAverageprioritet.setDisable(true);
        RBMainMaxprioritet.setSelected(false);
        RBMainMaxprioritet.setDisable(false);
        RBMainMinprioritet.setSelected(false);
        RBMainMinprioritet.setDisable(false);
    }
    @FXML
    void MinprioritetMainThread(ActionEvent event) {
        RBMainMinprioritet.setDisable(true);
        RBMainAverageprioritet.setSelected(false);
        RBMainAverageprioritet.setDisable(false);
        RBMainMaxprioritet.setSelected(false);
        RBMainMaxprioritet.setDisable(false);
    }
    @FXML
    void DogRBMaxPrioritetAction(ActionEvent event) {
        DogRBMaxPrioritet.setDisable(true);
        DogRBAveragePrioritet.setSelected(false);
        DogRBAveragePrioritet.setDisable(false);
        DogRBMinPrioritet.setSelected(false);
        DogRBMinPrioritet.setDisable(false);
    }
    @FXML
    void DogRBAveragePrioritetAction(ActionEvent event) {
        DogRBAveragePrioritet.setDisable(true);
        DogRBMaxPrioritet.setSelected(false);
        DogRBMaxPrioritet.setDisable(false);
        DogRBMinPrioritet.setSelected(false);
        DogRBMinPrioritet.setDisable(false);
    }
    @FXML
    void DogRBMinPrioritetAction(ActionEvent event) {
        DogRBMinPrioritet.setDisable(true);
        DogRBMaxPrioritet.setSelected(false);
        DogRBMaxPrioritet.setDisable(false);
        DogRBAveragePrioritet.setSelected(false);
        DogRBAveragePrioritet.setDisable(false);
    }
    @FXML
    void FileLoadButton(ActionEvent event) {
        resetObject = false; //Флаг чтобы при нажатом ShowInformation в методе stop не вызвалось диалоговое окно
        StopSimulation(); //Останавливаем симуляцию
        elapsedTime = 0;
        TimerSimulationLabel();
        controller.SimulationTimeLabel.setText("Time " + time);
        controller.DogCountLabel.setVisible(false);
        controller.CatCountLabel.setVisible(false);
        loadFileObjects(); //Загружаем объекты из бинарного файла
        controller.SimulationArea.getChildren().clear(); //Очищаем область симуляции от старых картинок
        for (Pets pets : petslist) { //Выгружаем новые картинки в область симуляции
            pets.petsImage.setTranslateX(pets.x);
            pets.petsImage.setTranslateY(pets.y);
            controller.SimulationArea.getChildren().add(pets.petsImage);
        }
        resetObject = true;
    }
    @FXML
    void FileSaveButton(ActionEvent event) {
        saveFileObjects();
    }
    @FXML
    void ConsoleButton(ActionEvent event) {
        Console();
    }
    @FXML
    void DataBases(ActionEvent event) {

    }
    @FXML
    void LoadBDAll(ActionEvent event) {
        try {
            loadTableObjectsAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void LoadBDCat(ActionEvent event) {
        try {
            loadTableCat();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void LoadBDDog(ActionEvent event) {
        try {
            loadTableDog();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void ReadBDAll(ActionEvent event) {
        try {
            readTableAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void ReadBDCat(ActionEvent event) {
        try {
            readTableCat();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void ReadBDDog(ActionEvent event) {
        try {
            readTableDog();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void initialize() {
        CatAIBox.fire();
        DogAIBox.fire();
        ObservableList<Double> catValues = FXCollections.observableArrayList();
        ObservableList<Double> dogValues = FXCollections.observableArrayList();
        for (double i = 0.0; i <= 1.0; i += 0.1) {
            catValues.add(Math.round(i * 10.0) / 10.0);
            dogValues.add(Math.round(i * 10.0) / 10.0);
        }
        ComboBoxCat.setItems(catValues);
        ComboBoxDog.setItems(dogValues);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveConfigFile(); //Сохранение конфигурационного файла
        }));
    }
    @FXML
    void MenuClient(ActionEvent event) {

    }
}