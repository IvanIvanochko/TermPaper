import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;

public class MainMenu {
    private String path = "G:\\Інші комп’ютери\\My laptop\\Політехніка\\Прикладне програмування\\Лабораторні\\Комплексна робота\\TaxiDepotApp\\src\\main\\resources\\";

    @FXML
    BorderPane borderPane;
    @FXML
    private void switchToLogIn(ActionEvent event) throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("logIn.fxml"));
        root = loader.load();
        LogIn logIn = loader.getController();
        logIn.switchToLogIn(event);
    }
    public Stage stage;
    public void switchToMain(ActionEvent event) throws IOException {
        //Stage stage;
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());

        Image icon = new Image(path + "logo.png");
        stage.getIcons().add(icon);
        stage.setTitle("TaxiDepotApp");
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void switchToRegister(ActionEvent event) throws IOException{
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
        root = loader.load();
        Register register = loader.getController();
        register.switchToRegister(event);
    }

    @FXML
    private void exit(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Do you want to exit?");

        ButtonType buttonTypeYES = new ButtonType("Yes");
        ButtonType buttonTypeNO = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeYES, buttonTypeNO);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonTypeYES){
            Stage stage = (Stage) borderPane.getScene().getWindow();
            stage.close();
        }
    }
}
