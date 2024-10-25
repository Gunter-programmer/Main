package org.example.semestr4newlaba;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.example.semestr4newlaba.ActionSimulation.petsChanges;
import static org.example.semestr4newlaba.CatAI.moveCat;
import static org.example.semestr4newlaba.DogAI.moveDog;
import static org.example.semestr4newlaba.HelloApplication.*;

public class Habitat {
    public static double x, y, chanceSpawnCat = 1, chanceSpawnDog = 1;
    public static int countCat = 0, countDog = 0, timeSpawnEverytimeCat = 1000, timeSpawnEverytimeDog = 2000, id;
    public static long timeCheckCat = timeSpawnEverytimeCat, timeCheckDog = timeSpawnEverytimeDog, timeLifeCat = 10000, timeLifeDog = 10000;
    public static ArrayList<Pets> petslist = new ArrayList<>();
    public static TreeSet<Integer> TreeSet = new TreeSet<>();
    public static HashMap<Integer, Long> HashMap = new HashMap<>();
    public static Random random = new Random();
    public static double workWidth = controller.SimulationArea.getWidth() * 0.86;
    public static double workHeight = controller.SimulationArea.getHeight()*0.86;
    public static ExecutorService threadPool = Executors.newCachedThreadPool();
    public static boolean boolGenerationCat = true;
    public static boolean boolGenerationDog = true;
    public static ObjectInputStream inputStream;
    public static ObjectOutputStream outputStream;
    public static String clientPort;
    public static LinkedList<String> clientList = new LinkedList<>();
    public static void update(){
        if(!petsChanges.isEmpty()){ //Удаление питомцев после работы с сервером
            if(petsChanges.get(0) instanceof Cat){ //Если пришли кошки -> удаляем собак
                petslist.removeIf(pets -> {
                    if(pets instanceof Dog){
                        TreeSet.remove(pets.PetsId);
                        HashMap.remove(pets.PetsId);
                        return true;
                    }
                    return false;
                });
            }
            else{ //Иначе пришли собаки -> удаляем кошек
                petslist.removeIf(pets -> {
                    if(pets instanceof Cat){
                        TreeSet.remove(pets.PetsId);
                        HashMap.remove(pets.PetsId);
                        return true;
                    }
                    return false;
                });
            }
            petsChanges.forEach(pets -> { //Коллекцию буфер(животные полученные с сервера) закидываем в текущую коллекцию клиента
                if(pets instanceof Cat) {
                    Image catImage = new Image(Habitat.class.getResourceAsStream("/image/cat.png"));
                    pets.petsImage = new ImageView(catImage);
                }
                else{
                    Image dogImage = new Image(Habitat.class.getResourceAsStream("/image/dog.png"));
                    pets.petsImage = new ImageView(dogImage);
                }
                randomId();
                pets.PetsId = id; //Задаём уникальное id новым кошкам
                petslist.add(pets);
                HashMap.put(pets.PetsId, elapsedTime);
            });
            petsChanges.clear();
        }
        if(elapsedTime >= timeCheckCat) { //Создание кошки
            timeCheckCat = timeCheckCat + timeSpawnEverytimeCat;
            if(boolGenerationCat) { //Если нажат флаг генерации кошек
                boolean isCat = random.nextDouble() < chanceSpawnCat;
                if (isCat) {
                    Thread GenerationCat = new Thread(new GenerationThread("Cat"));
                    if(controller.RBMainMaxprioritet.isSelected()) GenerationCat.setPriority(Thread.MAX_PRIORITY); //Задаём приоритет потоку
                    else if(controller.RBMainAverageprioritet.isSelected()) GenerationCat.setPriority(Thread.NORM_PRIORITY);
                    else GenerationCat.setPriority(Thread.MIN_PRIORITY);
                    GenerationCat.start();
                    try {
                        GenerationCat.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        if(elapsedTime >= timeCheckDog) {
            timeCheckDog = timeCheckDog + timeSpawnEverytimeDog;
            if(boolGenerationDog) { //Если нажат флаг генерации собак
                boolean isDog = random.nextDouble() < chanceSpawnDog;
                if (isDog) {
                    Thread GenerationDog = new Thread(new GenerationThread("Dog"));
                    if(controller.DogRBMaxPrioritet.isSelected()) GenerationDog.setPriority(Thread.MAX_PRIORITY); //Задаём приоритет потоку
                    else if(controller.DogRBAveragePrioritet.isSelected()) GenerationDog.setPriority(Thread.NORM_PRIORITY);
                    else GenerationDog.setPriority(Thread.MIN_PRIORITY);
                    GenerationDog.start();
                    try {
                        GenerationDog.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        if(threadPool == null || threadPool.isShutdown()){
            threadPool = Executors.newCachedThreadPool();
        }
        delPets();
        petslist.forEach(pets -> { //Движение
            if( pets instanceof Cat) {
                BaseAI baseAI;
                baseAI = new CatAI(pets);
                baseAI.ChangeCatAI();
                threadPool.execute(baseAI);
            }
            else if(pets instanceof Dog){
                BaseAI baseAI;
                baseAI = new DogAI(pets);
                baseAI.ChangeDogAI();
                threadPool.execute(baseAI);
            }
        });
    }
    static void randomId() {
        id = random.nextInt(10000000);
        if (TreeSet.contains(id)) {
            randomId();
        }
        TreeSet.add(id);
    }
    static void delPets(){
        for (Pets pets : petslist) {
            id = pets.PetsId;
            Long TimeBirth  = HashMap.get(id);
            if(pets instanceof Cat && elapsedTime >= TimeBirth + timeLifeCat){
                petslist.remove(pets);
                TreeSet.remove(id);
                HashMap.remove(id);
                countCat--;
                break;
            }
            else if(pets instanceof Dog && elapsedTime >= TimeBirth + timeLifeDog){
                petslist.remove(pets);
                TreeSet.remove(id);
                HashMap.remove(id);
                countDog--;
                break;
            }
        }
    }
}