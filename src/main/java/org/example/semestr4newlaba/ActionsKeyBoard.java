package org.example.semestr4newlaba;

import javafx.scene.input.KeyCode;

import static org.example.semestr4newlaba.ActionSimulation.StartSimulation;
import static org.example.semestr4newlaba.ActionSimulation.StopSimulation;
import static org.example.semestr4newlaba.HelloApplication.controller;
import static org.example.semestr4newlaba.HelloApplication.scene;

public class ActionsKeyBoard {
    static void ActionsKeyBoard() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.B) { // Старт
                StartSimulation();
            } else if(event.getCode() == KeyCode.E){ //Стоп
                StopSimulation();
            } else if(event.getCode() == KeyCode.T){ //Скрытие таймера
                if(controller.SimulationTimeLabel.isVisible()){
                    controller.SimulationTimeLabel.setVisible(false);
                    controller.MenuRadioShowTime.setSelected(false);
                    controller.MenuRadioHideTime.setDisable(true);
                    controller.MenuRadioShowTime.setDisable(false);
                    controller.RadioHideTime.fire();
                }
                else{
                    controller.SimulationTimeLabel.setVisible(true);
                    controller.MenuRadioHideTime.setSelected(false);
                    controller.MenuRadioShowTime.setDisable(true);
                    controller.MenuRadioHideTime.setDisable(false);
                    controller.RadioShowTime.fire();
                }
            }
        });
    }
}