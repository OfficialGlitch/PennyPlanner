module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens pennyplanner.pennyplanner to javafx.fxml;
    exports pennyplanner.pennyplanner;
    exports pennyplanner.pennyplanner.gui;
    opens pennyplanner.pennyplanner.gui to javafx.fxml;
}
