module org.example.semestr4newlaba {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.semestr4newlaba to javafx.fxml;
    exports org.example.semestr4newlaba;
}