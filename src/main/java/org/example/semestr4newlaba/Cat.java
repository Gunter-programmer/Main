package org.example.semestr4newlaba;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Cat extends Pets {
    public Cat(double x, double y, int id) {
        super(x, y, id);
        Image catImage = new Image(getClass().getResourceAsStream("/image/cat.png"));
        petsImage = new ImageView(catImage);
    }
}