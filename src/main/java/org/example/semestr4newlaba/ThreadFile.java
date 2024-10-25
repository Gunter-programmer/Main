package org.example.semestr4newlaba;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;

import static org.example.semestr4newlaba.ActionSimulation.exceptionConsole;
import static org.example.semestr4newlaba.BaseAI.speedCat;
import static org.example.semestr4newlaba.BaseAI.speedDog;
import static org.example.semestr4newlaba.CatAI.moveCat;
import static org.example.semestr4newlaba.DogAI.moveDog;
import static org.example.semestr4newlaba.Habitat.*;
import static org.example.semestr4newlaba.HelloApplication.*;

public class ThreadFile {
    static boolean Timer;
    static boolean ShowInf;
    static int prioritetCat = 5;
    static int prioritetDog = 5;
    public static PipedOutputStream outputStream;
    public static PipedInputStream inputStream;
    public static void loadConfigFile() {
        File file = new File("config.txt");
        if (!file.exists()) //Если файла не существует - выходим
        {
            return;
        }
        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) { //Создаём входной поток из файла
            Timer = dis.readBoolean(); //Флаг таймера
            if(Timer) {
                controller.SimulationTimeLabel.setVisible(true);
                controller.MenuRadioHideTime.setSelected(false);
                controller.MenuRadioHideTime.setDisable(false);
                controller.MenuRadioShowTime.setDisable(true);
                controller.RadioShowTime.fire();
            }
            else{
                controller.SimulationTimeLabel.setVisible(false);
                controller.MenuRadioShowTime.setSelected(false);
                controller.MenuRadioHideTime.setDisable(true);
                controller.MenuRadioShowTime.setDisable(false);
                controller.RadioHideTime.fire();
            }
            ShowInf = dis.readBoolean(); //Флаг вызова диалогового окна при остановке
            if(ShowInf) {
                controller.ShowInformationBox.setSelected(true);
                controller.MenuShowInformationBox.setSelected(true);
            }
            else{
                controller.ShowInformationBox.setSelected(false);
                controller.MenuShowInformationBox.setSelected(false);
            }
            timeSpawnEverytimeCat = dis.readInt(); //Период спавна кошек
            timeCheckCat = elapsedTime + timeSpawnEverytimeCat;
            timeSpawnEverytimeDog = dis.readInt(); //Период спавна собак
            timeCheckDog = elapsedTime + timeSpawnEverytimeDog;
            chanceSpawnCat = dis.readDouble(); //Шанс спавна кошек
            chanceSpawnDog = dis.readDouble(); //Шанс спавна собак
            timeLifeCat = dis.readLong(); //Время жизни кошек
            timeLifeDog = dis.readLong(); //Время жизни собак
            speedCat = dis.readDouble(); //Скорость движения кошек
            speedDog = dis.readDouble(); //Скорость движения собак
            moveCat = dis.readBoolean(); //Активирован ли поток движения кошек CatAI
            if(moveCat) controller.CatAIBox.setSelected(true);
            else controller.CatAIBox.setSelected(false);
            moveDog = dis.readBoolean(); //Тоже самое но с собаками
            if(moveDog) controller.DogAIBox.setSelected(true);
            else controller.DogAIBox.setSelected(false);
            boolGenerationCat = dis.readBoolean(); //Флаг генерации кошек
            if(boolGenerationCat) controller.checkGenerationCat.setSelected(true);
            else controller.checkGenerationCat.setSelected(false);
            boolGenerationDog = dis.readBoolean(); //Флаг генерации собак
            if(boolGenerationDog) controller.checkGenerationDog.setSelected(true);
            else controller.checkGenerationDog.setSelected(false);
            prioritetCat = dis.readInt();
            if(prioritetCat == 1) controller.MaxprioritetMainThread(new ActionEvent()); //Приоритет потока генерации кошек
            else if(prioritetCat == 5) controller.AverageprioritetMainThread(new ActionEvent());
            else controller.MinprioritetMainThread(new ActionEvent());
            prioritetDog = dis.readInt();
            if(prioritetDog == 1) controller.DogRBMaxPrioritetAction(new ActionEvent()); //Приоритет потока генерации кошек
            else if(prioritetDog == 5) controller.DogRBAveragePrioritetAction(new ActionEvent());
            else controller.DogRBMinPrioritetAction(new ActionEvent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveConfigFile() {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("config.txt"))) { //Создаём выходной поток в файл
            Timer = controller.SimulationTimeLabel.isVisible();
            dos.writeBoolean(Timer);
            ShowInf = controller.ShowInformationBox.isSelected();
            dos.writeBoolean(ShowInf);
            dos.writeInt(timeSpawnEverytimeCat);
            dos.writeInt(timeSpawnEverytimeDog);
            dos.writeDouble(chanceSpawnCat);
            dos.writeDouble(chanceSpawnDog);
            dos.writeLong(timeLifeCat);
            dos.writeLong(timeLifeDog);
            dos.writeDouble(speedCat);
            dos.writeDouble(speedDog);
            dos.writeBoolean(moveCat);
            dos.writeBoolean(moveDog);
            dos.writeBoolean(boolGenerationCat);
            dos.writeBoolean(boolGenerationDog);
            if(controller.RBMainMaxprioritet.isSelected()) prioritetCat = 1; //Приоритет потока генерации кошек
            else if(controller.RBMainAverageprioritet.isSelected()) prioritetCat = 5;
            else prioritetCat = 10;
            dos.writeInt(prioritetCat);
            if(controller.DogRBMaxPrioritet.isSelected()) prioritetDog = 1; //Приоритет потока генерации собак
            else if(controller.DogRBAveragePrioritet.isSelected()) prioritetDog = 5;
            else prioritetDog = 10;
            dos.writeInt(prioritetDog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveFileObjects(){ //Сохранение объектов в бинарный файл, сохранение коллекций
        try(ObjectOutputStream binFile = new ObjectOutputStream(new FileOutputStream("objects.bin"))){
            for (Pets pets : petslist) {
                binFile.writeObject(pets);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadFileObjects(){ //Загрузка бинарного файла в котором храняться объекты
        File file = new File("objects.bin");
        if (!file.exists()) //Если файла не существует - выходим
        {
            return;
        }
        try(ObjectInputStream binfile = new ObjectInputStream(new FileInputStream(file))){
            while (true) {
                try {
                    Pets pets = (Pets) binfile.readObject();
                    petslist.add(pets);
                    if(pets instanceof Cat){
                        Image catImage = new Image(ThreadFile.class.getResourceAsStream("/image/cat.png"));
                        pets.petsImage = new ImageView(catImage);
                    }
                    else {
                        Image dogImage = new Image(ThreadFile.class.getResourceAsStream("/image/dog.png"));
                        pets.petsImage = new ImageView(dogImage);
                    }
                    TreeSet.add(pets.PetsId);
                    HashMap.put(pets.PetsId, Long.valueOf(1));
                } catch (EOFException e) {
                    break; // достигнут конец файла
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
//    static public void Console(){
//
//    }
    static public void Console(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.initModality(Modality.NONE); //Делаем модальное диалоговое окно - не модальным
        dialog.setTitle("Консоль");
        dialog.setHeaderText("Введите текст:");
        TextArea textArea = new TextArea();  // Создаем многострочное текстовое поле
        textArea.setPrefSize(300, 200);
        GridPane grid = new GridPane();
        grid.add(textArea, 0, 1);
        dialog.getDialogPane().setContent(grid);
        outputStream = new PipedOutputStream(); //Создаём потоки ввода вывода
        inputStream = new PipedInputStream();
        try {
            inputStream.connect(outputStream); //Соединяем потоки между собой
        } catch (IOException e) {
            e.printStackTrace();
        }
        textArea.setOnKeyPressed(event -> { //При нажатии на Enter
            if (event.getCode() == KeyCode.ENTER) {
                String[] str = textArea.getText().split("\n"); //Разбиваем textArea на массив строк по \n
                if(str.length == 0){
                    return;
                }
                String lastStr = str[str.length - 1].toLowerCase(); //lastStr - последняя строка с нижними регистрами
                try {
                    byte[] buffer1 = lastStr.getBytes();
                    outputStream.write(buffer1); //Записываем строку в поток
                    outputStream.flush(); //Очищаем поток
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    byte[] buffer = new byte[100];
                    int temp = inputStream.read(buffer, 0, 100); //Считывает данные из потока
                    if (temp == -1) return;
                    String strTemp1 = new String(buffer, 0, temp); //Преобразование строки в String
                    if(strTemp1.equals("остановить генерацию кошек") || strTemp1.equals("остановить генерацию котов")){
                        controller.checkGenerationCat.setSelected(false);
                        controller.checkGenerationCat(new ActionEvent());
                    }
                    else if(strTemp1.equals("остановить генерацию собак")){
                        controller.checkGenerationDog.setSelected(false);
                        controller.checkGenerationDog(new ActionEvent());
                    }
                    else if(strTemp1.equals("остановить генерацию всех")){
                        controller.checkGenerationCat.setSelected(false);
                        controller.checkGenerationCat(new ActionEvent());
                        controller.checkGenerationDog.setSelected(false);
                        controller.checkGenerationDog(new ActionEvent());
                    }
                    else if(strTemp1.equals("восстановить генерацию кошек")){
                        controller.checkGenerationCat.setSelected(true);
                        controller.checkGenerationCat(new ActionEvent());
                    }
                    else if(strTemp1.equals("восстановить генерацию собак")){
                        controller.checkGenerationDog.setSelected(true);
                        controller.checkGenerationDog(new ActionEvent());
                    }
                    else if(strTemp1.equals("восстановить генерацию всех")){
                        controller.checkGenerationCat.setSelected(true);
                        controller.checkGenerationCat(new ActionEvent());
                        controller.checkGenerationDog.setSelected(true);
                        controller.checkGenerationDog(new ActionEvent());
                        exceptionConsole("\n                     Успешно восстановлено!\n\n");
                    }
                    else{
                        exceptionConsole("\n                        Ошибка! \n\n    Такой команды не существует!");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        dialog.addEventHandler(DialogEvent.ANY, event -> {
            if (event.getEventType() == DialogEvent.DIALOG_CLOSE_REQUEST) {
                try {
                    inputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        dialog.showAndWait();
    }
}
