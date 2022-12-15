import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class Main extends Application {
    public static int ID;
    public static void main(String[] args){launch(args);}
    @Override
    public void start(Stage stage) throws Exception {
        String path = "G:\\Інші комп’ютери\\My laptop\\Політехніка\\Прикладне програмування\\Лабораторні\\Комплексна робота\\TaxiDepotApp\\src\\main\\resources\\";

        //Group root = new Group();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());

        Image icon = new Image(path + "logo.png");
        stage.getIcons().add(icon);
        stage.setTitle("TaxiDepotApp");
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.setOnCloseRequest(event -> {
            event.consume();
            try {
                exit(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Keep this at the end
        stage.setScene(scene);
        stage.show();
        stage.setWidth(700);
        stage.setHeight(500);

        System.out.println("Height: " + stage.getHeight() + " Width: " + stage.getWidth());
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
                System.out.println("Height: " + stage.getHeight() + " Width: " + stage.getWidth());

        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);
    }

    public void exit(Stage stage) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Do you want to exit?");

        ButtonType buttonTypeYES = new ButtonType("Yes");
        ButtonType buttonTypeNO = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeYES, buttonTypeNO);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonTypeYES){
            stage.close();
        }
    }
}
