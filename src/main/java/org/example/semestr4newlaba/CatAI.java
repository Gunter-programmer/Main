package org.example.semestr4newlaba;

import static org.example.semestr4newlaba.Habitat.*;
import static org.example.semestr4newlaba.HelloApplication.controller;

public class CatAI extends BaseAI {
    static boolean moveCat = true;
    public CatAI(Pets pets) {
        super(pets);
    }
    @Override
    public void run() {
        synchronized (pets) {
            if (moveCat) {
                moveCat();
            }
            else {
                try {
                    pets.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public void moveCat() {
        if ((pets.x > 25 && pets.x < (workWidth - 5)) && (pets.y > 25 && pets.y < (workHeight - 5))) { //Проверка на достижение любой стороны рабочей симуляции
            pets.x = pets.x + speedCat;
        } else {
            if (pets.x > (workWidth - 10) && pets.y < workHeight) { //справа вниз
                pets.y = pets.y + speedCat;
            } else if (pets.x > 20 && pets.y > (workHeight - 10)) { //Снизу влево
                pets.x = pets.x - speedCat;
            } else if (pets.x < (workWidth - 10) && pets.y > 20) { //слева вверх
                pets.y = pets.y - speedCat;
            } else { //Сверху вправо
                pets.x = pets.x + speedCat;
            }
        }
    }
    public Pets getTargetObject() {
        return this.pets;
    }
}