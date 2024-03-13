module com.example.comiler_project {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.comiler_project to javafx.fxml;
    exports com.example.comiler_project;
}