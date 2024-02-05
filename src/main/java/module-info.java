module dev.javaball {
    requires javafx.controls;
    requires javafx.fxml;

    opens dev.javaball to javafx.fxml;
    exports dev.javaball;
}
