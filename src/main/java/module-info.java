module proggang {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.logging;
  requires java.desktop;

  exports backend.core;
  exports backend.transforms;
  exports backend.geometry;
  exports backend.models;

  exports frontend;
  exports frontend.controllers;

  opens frontend.controllers;

  opens css;
  opens config;

  opens frontend;
  opens backend.geometry;
  opens backend.models;
  opens backend.core;
  opens backend.transforms;

  exports backend.utility.state;
}