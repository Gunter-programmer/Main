package org.example.semestr4newlaba;

import java.util.function.Consumer;

import static java.lang.Math.*;
import static org.example.semestr4newlaba.Habitat.*;

public class DogAI extends BaseAI{
    static boolean moveDog = true;
    public DogAI(Pets pets) {
        super(pets);
    }

    @Override
    public void run() {
        synchronized (pets) {
            if(moveDog) {
                moveDog();
            }
            else{
                try {
                    pets.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public void moveDog(){
        int countCat = 0;
        double tempX, tempY, tempDistance;
        double x = 0;
        double y = 0;
        double distance = 10000;
        for (Pets pets1 : petslist) {
            if(pets1 instanceof Cat) {
                countCat++;
                tempX = abs(pets.x - pets1.x);
                tempY = abs(pets.y - pets1.y);
                tempDistance = sqrt(pow(tempX, 2) + pow(tempY, 2));
                if (distance > tempDistance){ //Перезапись самой близжайшей кошки
                    distance = tempDistance;
                    x = pets1.x;
                    y = pets1.y;
                }
            }
        }
        if(countCat == 0 || distance < 1){ //Если кошек нет на экране
            return; //Собака стоит на месте
        }
        double tempSpeed = speedDog; //Расчёты для плавного перемещения
        if(pets.x > (workWidth - 5) || pets.x < 25 || pets.y > (workHeight - 5) || pets.y < 25 || (pets.x - x > 1) ||
                (pets.y - y > 1)) tempSpeed = tempSpeed * 2; //Учёт перемещения по катетам
        else tempSpeed = tempSpeed * 1.4; //Учёт перемещения по гипотенузе
        while(distance > 1){
            if(pets.x - x > 1){
                pets.x = pets.x - 1;
                tempSpeed--; if(tempSpeed <= 0) break;
                distance--;
            }
            else {
                pets.x = pets.x + 1;
                tempSpeed--; if(tempSpeed <= 0) break;
                distance--;
            }
            if(pets.y - y > 1){
                pets.y = pets.y - 1;
                tempSpeed--; if(tempSpeed <= 0) break;
                distance--;
            }
            else {
                pets.y = pets.y + 1;
                tempSpeed--; if(tempSpeed <= 0) break;
                distance--;
            }
        }
    }
}
