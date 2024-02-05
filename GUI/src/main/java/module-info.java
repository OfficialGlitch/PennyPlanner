module org.yorku.pennyplannerorg {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.yorku.pennyplannerorg to javafx.fxml;
    exports org.yorku.pennyplannerorg;
}
