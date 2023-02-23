module com.example.sistdistribuite {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sistdistribuite to javafx.fxml;
    exports com.example.sistdistribuite;
}