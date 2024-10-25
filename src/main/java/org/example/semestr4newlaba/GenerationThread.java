package org.example.semestr4newlaba;

import static org.example.semestr4newlaba.Habitat.*;
import static org.example.semestr4newlaba.HelloApplication.elapsedTime;

public class GenerationThread implements Runnable {
    public String type;

    public GenerationThread(String type) {
        this.type = type;
    }
    public synchronized void generationCat() {
        if(boolGenerationCat) {
            x = random.nextDouble() * workWidth;
            y = random.nextDouble() * workHeight;
            countCat++;
            randomId();
            Pets pets;
            pets = new Cat(x, y, id);
            petslist.add(pets);
            HashMap.put(id, elapsedTime);
        }
        else{
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public synchronized void generationDog() {
        x = random.nextDouble() * workWidth;
        y = random.nextDouble() * workHeight;
        countDog++;
        randomId();
        Pets pets;
        pets = new Dog(x, y, id);
        petslist.add(pets);
        HashMap.put(id, elapsedTime);
    }
    @Override
    public void run() {
        if (type.equals("Cat")) {
            generationCat();
        }
        if (type.equals("Dog")) {
            generationDog();
        }
    }
}