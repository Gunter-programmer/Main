package Server;

import javafx.scene.control.MenuItem;
import org.example.semestr4newlaba.Pets;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.example.semestr4newlaba.HelloApplication.controller;

public class TCPServer {
    public static Map<Socket, ObjectOutputStream> clientsoutputs = new HashMap<>();
    public static Map<Socket, ObjectInputStream> clientsinputs = new HashMap<>();
    public static Map<Socket, ArrayList<Pets>> changeDogsMap = new HashMap<>();
    public static void main(String[] args) {
        // стартуем сервер на порту 3345 и инициализируем переменную для обработки консольных команд с самого сервера
        try (ServerSocket serverSocket = new ServerSocket(3345)) {
            System.out.println("Server socket created, command console reader for listen to server commands");
            // стартуем цикл при условии что серверный сокет не закрыт
            while (true) {
                // проверяем поступившие комманды из консоли сервера если такие были
                Socket clientSocket = serverSocket.accept();
                System.out.println("Main Server get new client." + clientSocket);
                clientsoutputs.put(clientSocket, new ObjectOutputStream(clientSocket.getOutputStream()));
                clientsinputs.put(clientSocket, new ObjectInputStream(clientSocket.getInputStream()));
                new ServerThread(clientSocket).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class ServerThread extends Thread {
        public Socket clientSocket;
        public Socket changeSocket;
        public ServerThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            updateList();
            while (true) {
                try {
                    Object data = clientsinputs.get(clientSocket).readObject();
                    if(data instanceof String && !data.equals("sendDog")){
                        String changePort = (String) data; //порт клиента которому нужно отправить
                        ArrayList<Pets> catsList;
                       if((catsList = (ArrayList<Pets>)clientsinputs.get(clientSocket).readObject()) != null){ //Получение кошек от клиента
                           System.out.println("Порт "+changePort +" Кошки:" + catsList);
                           for(Map.Entry<Socket,ObjectOutputStream> entry: clientsoutputs.entrySet()){
                               if(changePort.equals(entry.getKey().getPort() + "")){ //Поиск в словаре сокета клиента которому нужно отправить кошек
                                   changeSocket = entry.getKey();
                                   break;
                               }
                           }
                           clientsoutputs.get(changeSocket).writeObject("SendCat"); //Флаг на то, что клиенту нужно принять кошек
                           clientsoutputs.get(changeSocket).writeObject(catsList);
                           while(true){
                               if(changeDogsMap.containsKey(changeSocket)){
                                   clientsoutputs.get(clientSocket).writeObject("sendDogClient");
                                   clientsoutputs.get(clientSocket).writeObject(changeDogsMap.get(changeSocket));
                                   changeDogsMap.remove(changeSocket);
                                   changeSocket = null;
                                   break;
                               }
                           }
                       }
                    }
                    if(data instanceof String && data.equals("sendDog")){
                        ArrayList<Pets> dogsList;
                        if((dogsList = (ArrayList<Pets>)clientsinputs.get(clientSocket).readObject()) != null){
                            System.out.println("Собаки:" + dogsList);
                            changeDogsMap.put(clientSocket, dogsList);
                        }
                    }
                } catch(SocketException e) { //Если клиент отключился, удаляем его из всех словарей
                    clientsoutputs.remove(clientSocket);
                    clientsinputs.remove(clientSocket);
                    System.out.println("Клиент завершил работу с портом - " + clientSocket.getPort());
                    updateList();
                    break;
                } catch (EOFException e) {
                    return;
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public static void updateList() { //Когда создаётся новый клиент или удаляется
            LinkedList<String> ports = new LinkedList<>();
            clientsoutputs.forEach((socket, stream) -> {
                ports.add(String.valueOf(socket.getPort()));
            });
            clientsoutputs.forEach((socket, stream) -> {
                try {
                    stream.writeObject(ports);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}