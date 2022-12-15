import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.stage.Stage;

import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LogIn {
    private Stage stage;
    private Scene scene;
    private Parent root;
    static Statement statement;
    @FXML
    TextField usernameTextField;
    @FXML
    PasswordField passwordPasswordField;
    @FXML
    Label usernameLabel, passwordLabel;
    private String path = "G:\\Інші комп’ютери\\My laptop\\Політехніка\\Прикладне програмування\\Лабораторні\\Комплексна робота\\TaxiDepotApp\\src\\main\\resources\\";


    @FXML
    private void initialize() {
        JDBC jdbc = new JDBC();
        Connection connection = jdbc.getConnection();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        usernameLabel.setVisible(false);
        passwordLabel.setVisible(false);
    }
    public void switchToLogIn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("logIn.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
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
    public void switchToMain(ActionEvent event) throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        root = loader.load();
        MainMenu mainMenu = loader.getController();
        mainMenu.switchToMain(event);
    }
    public void switchToMenu(ActionEvent event) throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        root = loader.load();
        Menu menu = loader.getController();
        menu.switchToMenu(event);
    }
    public void typeUsername(KeyEvent event) throws IOException, SQLException{
        String username = usernameTextField.getText();
        String usernameCheck = " ";

        try {
            usernameCheck = " ";

            String sqlCommand = "select Username from UserAccount where Username = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(sqlCommand);
            while (resultSet.next()) {
                usernameCheck = resultSet.getString("Username");
            }
            if(!username.equals(usernameCheck)){
                usernameLabel.setVisible(true);
            }else
                usernameLabel.setVisible(false);
        } catch (SQLException e) {
            System.out.println("An error occurred:");
            throw new RuntimeException(e);
        }
    }
    public void logIn(ActionEvent event) throws IOException, SQLException {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();
        String usernameCheck = " ";

        try {
            usernameCheck = " ";

            String sqlCommand = "select Username from UserAccount where Username = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(sqlCommand);
            while (resultSet.next()) {
                usernameCheck = resultSet.getString("Username");
            }
            if(!username.equals(usernameCheck))
                usernameLabel.setVisible(true);
            else
                usernameLabel.setVisible(false);
        } catch (SQLException e) {
            System.out.println("An error occurred:");
            throw new RuntimeException(e);
        }

        try {
            usernameCheck = " ";

            String sqlCommand = "select Username from UserAccount where Username = '" +
                    username + "' and Password = '"+ password +"'";
            ResultSet resultSet = statement.executeQuery(sqlCommand);

            while (resultSet.next()) {
                usernameCheck = resultSet.getString("Username");
            }
            if(!username.equals(usernameCheck))
                passwordLabel.setVisible(true);
            else
                passwordLabel.setVisible(false);
        } catch (SQLException e) {
            System.out.println("An error occurred:");
            throw new RuntimeException(e);
        }

        try {
            String sqlCommand = "select ID from UserAccount where Username = '"+username+"'";
            ResultSet resultSet = statement.executeQuery(sqlCommand);

            while (resultSet.next()){
                Main.ID = resultSet.getInt("ID");
            }
        }catch (SQLException e) {
            System.out.println("An error occurred:");
            throw new RuntimeException(e);
        }

        if(!usernameCheck.equals(" "))
            switchToMenu(event);
    }
}
