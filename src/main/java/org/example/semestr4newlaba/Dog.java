package org.example.semestr4newlaba;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Dog extends Pets{
    public Dog(double x, double y, int id) {
        super(x, y, id);
        Image dogImage = new Image(getClass().getResourceAsStream("/image/dog.png"));
        petsImage = new ImageView(dogImage);
    }
}