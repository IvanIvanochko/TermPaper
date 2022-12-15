import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class Register {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Statement statement;
    private String path = "G:\\Інші комп’ютери\\My laptop\\Політехніка\\Прикладне програмування\\Лабораторні\\Комплексна робота\\TaxiDepotApp\\src\\main\\resources\\";

    @FXML
    TextField passwordTF, usernameTF;
    @FXML
    VBox infoVBox;
    @FXML
    HBox taxiDriverHBox;
    @FXML
    TextField snpTF, experienceTaxiDriverTF, phoneTF, cityTF;
    @FXML
    DatePicker birthDateDP, driverExperienceDP;
    @FXML
    CheckBox categoryB_CB,taxiDriverExperienceCB;
    @FXML
    TextArea selfDescriptionTA;
    @FXML
    Button registerB;
    @FXML
    Label categoryB_error;
    String passwordS, usernameS;
    String snpS, experienceTaxiDriverS, phoneS, cityS;
    LocalDate birthDateLD, driverExperienceLD;
    String categoryB_S,taxiDriverExperienceS, selfDescriptionS;


    private final DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    @FXML
    private void initialize() {
        JDBC jdbc = new JDBC();
        Connection connection = jdbc.getConnection();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        infoVBox.setDisable(true);
        taxiDriverHBox.setDisable(true);
        categoryB_error.setVisible(false);

        EnhancedMethods.enhanceDatePickers(birthDateDP);
        EnhancedMethods.enhanceDatePickers(driverExperienceDP);

        EnhancedMethods.onlySpecifiedCharactersTF(experienceTaxiDriverTF, "0123456789");
        EnhancedMethods.onlySpecifiedCharactersTF(phoneTF, "0123456789");
    }
    public void switchToRegister(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("register.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());

        Image icon = new Image(path + "logo.png");
        stage.getIcons().add(icon);
        stage.setTitle("TaxiDepotApp");
        stage.setMinWidth(600);
        stage.setMinHeight(400);

        stage.setScene(this.scene);
        stage.show();
    }

    public void switchToMain(ActionEvent event) throws IOException {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        root = loader.load();
        MainMenu mainMenu = loader.getController();
        mainMenu.switchToMain(event);
    }
    public void typeUsernameAndPassword(KeyEvent event) throws IOException
    {
        usernameS = usernameTF.getText();
        passwordS = passwordTF.getText();

        if(!usernameS.equals("") && !passwordS.equals(""))
            infoVBox.setDisable(false);
        else
            infoVBox.setDisable(true);
    }
    @FXML
    public void register(ActionEvent event) throws IOException, SQLException
    {
        String error = "-fx-background-color: #fdb6b6;\n" + "-fx-border-color: #611d00;";
        Boolean[] checkList = new Boolean[9];

        snpS = snpTF.getText();
        if(snpS == "")
            snpTF.setStyle(error);
        else{
            snpTF.setStyle(null);
            checkList[0] = true;
        }

        try {
            birthDateLD = birthDateDP.getValue();

            if(birthDateLD.equals(LocalDate.now())){
                TextField tf = birthDateDP.getEditor();
                tf.setStyle(error);
            }
            else{
                TextField tf = birthDateDP.getEditor();
                tf.setStyle(null);
                checkList[1] = true;
            }
        }catch (DateTimeParseException e){
            TextField tf = birthDateDP.getEditor();
            tf.setStyle(error);
        }

        cityS = cityTF.getText();
        if(cityS == "")
            cityTF.setStyle(error);
        else{
            cityTF.setStyle(null);
            checkList[2] = true;
        }

        try {
            driverExperienceLD = driverExperienceDP.getValue();
            System.out.println(driverExperienceLD);
            System.out.println(LocalDate.now());

            if(driverExperienceLD.equals(LocalDate.now())){
                TextField tf = driverExperienceDP.getEditor();
                tf.setStyle(error);
            }
            else {
                TextField tf = driverExperienceDP.getEditor();
                tf.setStyle(null);
                checkList[3] = true;
            }
            System.out.println(checkList[3]);
        }catch (DateTimeParseException e){
            TextField tf = driverExperienceDP.getEditor();
            tf.setStyle(error);
        }

        categoryB_S = categoryB_CB.isSelected() ? "1" : "0";
        if(categoryB_S == "0"){
            categoryB_error.setVisible(true);
            categoryB_CB.setId("box-error");
        }
        else {
            categoryB_error.setVisible(false);
            categoryB_CB.setId("check-box");
            checkList[4] = true;
        }

        taxiDriverExperienceS = taxiDriverExperienceCB.isSelected() ? "1" : "0";
        checkList[5] = true;
        experienceTaxiDriverS = experienceTaxiDriverTF.getText();
        if(experienceTaxiDriverS == "")
            experienceTaxiDriverS = null;
        checkList[6] = true;

        phoneS = phoneTF.getText();
        if(phoneS == "")
            phoneTF.setStyle(error);
        else {
            phoneTF.setStyle(null);
            checkList[7] = true;
        }

        selfDescriptionS = selfDescriptionTA.getText();
        checkList[8] = true;

        boolean allTrue = Arrays.asList(checkList).contains(null);

        if(allTrue == false){
            // Username and Password
            try {
                String sqlCommand = "insert into UserAccount(Username,[Password]) values('" +usernameS+"','"+passwordS+"');";
                statement.execute(sqlCommand);
            }catch (SQLException e) {
                System.out.println("An error occurred:");
                throw new RuntimeException(e);
            }
            // Get ID
            try {
                String sqlCommand = "select ID from UserAccount where Username = '"+usernameS+"'";
                ResultSet resultSet = statement.executeQuery(sqlCommand);

                while (resultSet.next()){
                    Main.ID = resultSet.getInt("ID");
                }
            }catch (SQLException e) {
                System.out.println("An error occurred:");
                throw new RuntimeException(e);
            }
            // Rest info
            String sqlCommand, sqlCommand1, sqlCommand2;
            sqlCommand1 = "SET IDENTITY_INSERT UserInformation ON insert into UserInformation(ID,SNP,BirthDate,City,ExperienceDriving,CategoryB,ExperienceTaxiDriverB,ExperienceTaxiDriver,PhoneNumber,SelfDescription) ";
            sqlCommand2 = "values("+
                    Main.ID+",'"+
                    snpS+"', '"+
                    birthDateLD.toString()+"', '"+
                    cityS+"','"+
                    driverExperienceLD.toString()+"'," +
                    categoryB_S+","+
                    taxiDriverExperienceS+"," +
                    experienceTaxiDriverS+",'"+
                    phoneS+"', '" +
                    selfDescriptionS+"');";
            sqlCommand = sqlCommand1 + sqlCommand2;
            System.out.println(sqlCommand);
            try{
                statement.execute(sqlCommand);
            }catch (SQLException e) {
                System.out.println("An error occurred:");
                throw new RuntimeException(e);
            }

            registerB.setDisable(true);
        }

    }

    @FXML
    void enableTaxiDriverHbox(ActionEvent event) throws IOException
    {
        taxiDriverExperienceS = taxiDriverExperienceCB.isSelected() ? "1" : "0";
        if(taxiDriverExperienceS == "1")
            taxiDriverHBox.setDisable(false);
        else
            taxiDriverHBox.setDisable(true);
    }

    @FXML
    void action(TouchEvent event) throws IOException
    {
        System.out.println("Action");
    }
}
