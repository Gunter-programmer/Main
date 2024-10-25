package org.example.semestr4newlaba;

import javafx.scene.control.ToggleGroup;

import static org.example.semestr4newlaba.CatAI.moveCat;
import static org.example.semestr4newlaba.DogAI.moveDog;
import static org.example.semestr4newlaba.Habitat.workHeight;
import static org.example.semestr4newlaba.Habitat.workWidth;

public abstract class BaseAI implements Runnable {
    Pets pets;
    public static double speedCat = 5;
    public static double speedDog = 4;

    public BaseAI(Pets pets) {
        this.pets = pets;
    }
    public void ChangeCatAI() {
        if (moveCat) {
            synchronized (pets) {
                pets.notify();
            }
        }
    }
    public void ChangeDogAI() {
        if (moveDog) {
            synchronized (pets) {
                pets.notify();
            }
        }
    }
}