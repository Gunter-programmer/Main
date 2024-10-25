package org.example.semestr4newlaba;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.example.semestr4newlaba.ActionSimulation.*;
import static org.example.semestr4newlaba.Habitat.*;
import static org.example.semestr4newlaba.HelloApplication.elapsedTime;

public class TCPClient extends Thread {
    @Override
    public void run(){
        try(Socket socket = new Socket(InetAddress.getLocalHost(),3345)) {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream((socket.getOutputStream()));
            clientPort = String.valueOf(socket.getLocalPort());
            System.out.println("Порт клиента - " + clientPort);
            while(true){
                try {
                    Object data = inputStream.readObject();
                    if(data instanceof LinkedList){ //обновляет список клиентов
                        clientList = (LinkedList<String>)data;
                        updateClient(); //Также есть action на отправку кошек на сервер
                    }
                    if(data instanceof String && ((String)data).equals("SendCat")){ //Принимает кошек с сервера
                        petsChanges = (ArrayList<Pets>)inputStream.readObject(); //считываем кошек с потока
                        outputStream.writeObject("sendDog");
                        outputStream.writeObject(getDogs());
                        System.out.println("cat" + petsChanges);
                    }
                    if(data instanceof String && ((String)data).equals("sendDogClient")){ //Принимает собак с сервера
                        petsChanges = (ArrayList<Pets>)inputStream.readObject(); //считываем собак с потока
                        System.out.println("sobak" + petsChanges);
                    }
                } catch (EOFException e) {
                    return;
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.err.println("Сервер не подключен");
        }
    }
}
