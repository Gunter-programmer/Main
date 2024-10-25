package org.example.semestr4newlaba;

import static org.example.semestr4newlaba.HelloApplication.elapsedTime;

public class TimerSimulation {
    public static String time;
    public static void TimerSimulationLabel(){
        long milliseconds = elapsedTime % 1000;
        long seconds = (elapsedTime / 1000) % 60;
        long minutes = (elapsedTime / (1000 * 60)) % 60;
        long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
        time = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
    }
}
