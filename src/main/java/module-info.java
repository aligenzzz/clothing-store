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
    requires google.api.client;
    requires com.google.api.client;
    requires google.api.services.drive.v2.rev393;
    requires com.google.auth.oauth2;
    requires com.google.api.client.json.gson;
    requires com.google.auth;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires jdk.httpserver;

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