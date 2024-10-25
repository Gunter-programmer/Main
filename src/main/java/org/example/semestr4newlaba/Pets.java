package org.example.semestr4newlaba;

import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.Objects;

public abstract class Pets implements iBehaviour, Serializable {
    public double x, y;
    public int PetsId;
    transient public ImageView petsImage;
    public Pets(double x, double y, int PetsId) {
        this.x = x;
        this.y = y;
        this.PetsId = PetsId;
    }

    @Override
    public boolean equals(Object o) {
        Pets temp = (Pets)o;
        if(this.PetsId == temp.PetsId) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, PetsId, petsImage);
    }

    @Override
    public String toString() {
        return "Pets{" +
//                "x=" + x +
//                ", y=" + y +
                ", PetsId=" + PetsId +
                '}';
    }
}
