module com.example.test
{
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.logging.log4j;
    requires org.jetbrains.annotations;
    requires serialize;
    requires com.google.gson;
    requires github.api;

    opens com.example.test to javafx.fxml;
    exports com.example.test;
    exports com.example.test.enums;
    opens com.example.test.enums to javafx.fxml;
    exports com.example.test.entities;
    opens com.example.test.entities to javafx.fxml;
    exports com.example.test.controllers;
    opens com.example.test.controllers to javafx.fxml;
    exports com.example.test.interfaces;
    opens com.example.test.interfaces to javafx.fxml;
}