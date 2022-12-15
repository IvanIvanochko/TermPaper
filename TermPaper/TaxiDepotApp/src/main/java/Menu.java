import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Menu {
    private String path = "G:\\Інші комп’ютери\\My laptop\\Політехніка\\Прикладне програмування\\Лабораторні\\Комплексна робота\\TaxiDepotApp\\src\\main\\resources\\";
    private Stage stage;
    private Scene scene;
    private Parent root;
    Statement statement;
    @FXML
    ScrollPane rentCarSP;
    @FXML
    VBox rentCarVB, myRentCarsVB;
    @FXML
    TextField yearTF, priceFromTF, priceToTF, priceFromToRentTF, priceToToRentTF, searchBarTF;
    @FXML
    TextField litersFromTF, litersToTF, accelerationFromTF, accelerationToTF;
    @FXML
    ChoiceBox<String> fuelChB, bodyTypeChB, brandChB, transmissionChB, modelChB;
    @FXML
    CheckBox unoccupiedCB;
    @FXML
    Button renewMyCabinetB;
    @FXML
    Tab rentCarTab, myCabinetTab;
    @FXML
    TabPane tabPane;
    @FXML
    Label snpL, birthDateL, cityL, phoneNumberL;

    String[] fuelS_Arr, bodyTypeS_Arr, brandS_Arr, transmissionS_Arr, modelS_Arr;
    ArrayList<String> modelS_List = new ArrayList<>();
    ArrayList<String> checkS_List = new ArrayList<>(Arrays.asList("All", "All", "All", "All", "Choose Brand"
            , "", "", "", "", "", "0"));
    @FXML
    private void initialize() {
        JDBC jdbc = new JDBC();
        Connection connection = jdbc.getConnection();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        choiceBoxFill("Fuel", fuelS_Arr, fuelChB);
        fuelChB.setOnAction(this::getPreferences);
        choiceBoxFill("BodyType", bodyTypeS_Arr, bodyTypeChB);
        bodyTypeChB.setOnAction(this::getPreferences);
        choiceBoxFill("Brand", brandS_Arr, brandChB);
        brandChB.setOnAction(this::getPreferences);
        choiceBoxFill("Transmission", transmissionS_Arr, transmissionChB);
        transmissionChB.setOnAction(this::getPreferences);
        modelBoxFill(null,modelS_Arr,modelChB);
        modelChB.setOnAction(this::updateModelChB);

        EnhancedMethods.onlySpecifiedCharactersTF(yearTF, "0123456789");
        EnhancedMethods.addTextLimiterTF(yearTF, 4);
        EnhancedMethods.onlySpecifiedCharactersTF(priceFromTF, "0123456789");
        EnhancedMethods.onlySpecifiedCharactersTF(priceToTF, "0123456789");
        EnhancedMethods.onlySpecifiedCharactersTF(priceFromToRentTF, "0123456789");
        EnhancedMethods.onlySpecifiedCharactersTF(priceToToRentTF, "0123456789");

        EnhancedMethods.onlySpecifiedCharactersTF(litersFromTF, "0123456789.");
        EnhancedMethods.onlySpecifiedCharactersTF(litersToTF, "0123456789.");
        EnhancedMethods.onlySpecifiedCharactersTF(accelerationFromTF, "0123456789.");
        EnhancedMethods.onlySpecifiedCharactersTF(accelerationToTF, "0123456789.");

        try {
            addRentCars(checkS_List, false, false);
            addRentCars(checkS_List, true, false);
        } catch (SQLException e){
        } catch (IOException e){}

        rentCarTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                if (rentCarTab.isSelected()) {
                    rentCarVB.getChildren().clear();
                    try {
                        addRentCars(checkS_List, false, false);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        myCabinetTab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                if (myCabinetTab.isSelected()) {
                    myRentCarsVB.getChildren().clear();
                    try {
                        addRentCars(checkS_List, true, false);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        try {
        String sqlCommand = "select * from UserInformation where ID = " + Main.ID;
        ResultSet resultSet = null;
        resultSet = statement.executeQuery(sqlCommand);
        int iter = 0;
        while (resultSet.next()) {
            //vars
            int ID = resultSet.getInt("ID");
            if(ID == Main.ID && iter == 0){
            String snp = resultSet.getString("SNP");
            String birthDate = resultSet.getString("BirthDate");
            String city = resultSet.getString("City");
            String experienceDriving = resultSet.getString("ExperienceDriving");
            String categoryB = resultSet.getString("CategoryB");
            String experienceTaxiDriverB = resultSet.getString("ExperienceTaxiDriverB");
            String experienceTaxiDriver = resultSet.getString("ExperienceTaxiDriver");
            String phoneNumber = resultSet.getString("PhoneNumber");
            String selfDescription = resultSet.getString("SelfDescription");

            snpL.setText(snp);
            birthDateL.setText(birthDate);
            cityL.setText(city);
            phoneNumberL.setText(phoneNumber);

            iter++;
            }
        }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Main.ID);
    }

    private void getPreferences(ActionEvent event) {
        try {
            checkS_List.set(0,fuelChB.getValue());
            checkS_List.set(1,bodyTypeChB.getValue());
            checkS_List.set(2,brandChB.getValue());
            checkS_List.set(3,transmissionChB.getValue());

            modelBoxFill(checkS_List.get(2), modelS_Arr, modelChB);

            System.out.println(Arrays.toString(checkS_List.toArray()));
            updateRentCars(checkS_List, false);
        } catch (SQLException e){
        } catch (IOException e){}
    }
    @FXML
    private void getPreferencesNums(KeyEvent event) throws IOException{
        try{
            checkS_List.set(5,yearTF.getText());

            checkS_List.set(6, priceFromTF.getText()+" - "+priceToTF.getText());
            checkS_List.set(7, priceFromToRentTF.getText()+" - "+priceToToRentTF.getText());
            checkS_List.set(8, litersFromTF.getText()+" - "+litersToTF.getText());
            checkS_List.set(9, accelerationFromTF.getText()+" - "+accelerationToTF.getText());

            updateRentCars(checkS_List, false);
        } catch (SQLException e){
        } catch (IOException e){}
    }
    private void updateModelChB(ActionEvent event)
    {
        checkS_List.set(4,modelChB.getValue());
        try{
        updateRentCars(checkS_List, false);
        } catch (SQLException e){
        } catch (IOException e){}
    }

    @FXML
    private void getCB(ActionEvent event){
        checkS_List.set(10, unoccupiedCB.isSelected() ? "1" : "0");
        try{
            updateRentCars(checkS_List, false);
        } catch (SQLException e){
        } catch (IOException e){}
    }

    private void choiceBoxFill(String column, String[] columnArr, ChoiceBox<String> columnChb){
        try {
            int numForArr = 0;
            String getNumForArr = "select count(distinct "+column+") as cc from TaxiDepot";

            ResultSet getNumForArrtSet = statement.executeQuery(getNumForArr);
            if(getNumForArrtSet.next())
                numForArr = getNumForArrtSet.getInt("cc");

            columnArr = new String[numForArr + 1];

            String selectDistinctString = "select distinct "+column+" as aa from TaxiDepot";
            ResultSet selectDistinctStringSet = statement.executeQuery(selectDistinctString);
            int count = 1;
            columnArr[0] = "All";
            while (selectDistinctStringSet.next())
            {
                columnArr[count] = selectDistinctStringSet.getString("aa");
                count++;
            }
            columnChb.getItems().addAll(columnArr);
            columnChb.setValue(columnArr[0]);
        } catch (SQLException ex) {}
    }
    private void modelBoxFill(String brand, String[] columnArr, ChoiceBox<String> columnChb){
        try {

            if(brand == null || brand.equals("All") || brand.equals("Choose Brand")) {
                columnChb.getItems().clear();

                columnArr = new String[1];
                columnArr[0] = "Choose Brand";
                columnChb.getItems().addAll(columnArr);
                columnChb.setValue("Choose Brand");
            } else {
                columnChb.getItems().clear();

                int numForArr = 0;
                String getNumForArr = "select count(distinct Brand) as cc from TaxiDepot where Brand = '" + brand + "'";

                ResultSet getNumForArrtSet = statement.executeQuery(getNumForArr);
                if (getNumForArrtSet.next())
                    numForArr = getNumForArrtSet.getInt("cc");

                columnArr = new String[numForArr + 1];

                String selectDistinctString = "select distinct Model as aa from TaxiDepot where Brand = '" + brand + "'";
                ResultSet selectDistinctStringSet = statement.executeQuery(selectDistinctString);
                int count = 1;
                columnArr[0] = "All";
                while (selectDistinctStringSet.next()) {
                    columnArr[count] = selectDistinctStringSet.getString("aa");
                    count++;
                }
                columnChb.getItems().addAll(columnArr);
                columnChb.setValue("All");
            }
        } catch (SQLException ex) {}
    }
    private void updateRentCars(ArrayList<String> checkS_List, boolean myCabinet) throws IOException, SQLException
    {
        rentCarVB.getChildren().clear();
        addRentCars(checkS_List, myCabinet, false);
    }
    @FXML
    private void updateMyCabinet(ActionEvent event) throws IOException, SQLException
    {
        myRentCarsVB.getChildren().clear();
        addRentCars(checkS_List, true, false);
    }

    private String newSQLcommand;
    private boolean first;
    private int counter;
    protected void addRentCars(ArrayList<String> checkS_List, boolean myCabinet, boolean searchBar) throws IOException, SQLException {
        String sqlCommand = "select * from TaxiDepot" + ((myCabinet == true) ? " where UserAccountID = "+Main.ID+"" : "");

        if(myCabinet == false) {
            ArrayList<String> columnsOrder =
                    new ArrayList<>(Arrays.asList("Fuel", "BodyType", "Brand", "Transmission", "Model", "Year", "Price",
                            "PriceToRent", "LitersPer100Km", "AccelerationTo100KmAnHour", "UserAccountID"));
            newSQLcommand = sqlCommand + " where ";
            this.first = true;
            counter = 0;
            checkS_List.forEach((str) -> {
                if (str != null)
                    if (!str.equals("All") && !str.equals("Choose Brand") && !str.equals("") && !str.equals(" - ")) {
                        if (counter < 6) {
                            if (this.first == false)
                                newSQLcommand += " and " + columnsOrder.get(counter) + "=" + "'" + str + "'";
                            if (this.first == true) {
                                newSQLcommand += columnsOrder.get(counter) + "=" + "'" + str + "'";
                                this.first = false;
                            }
                        }
                        if (counter >= 6 && counter < 10) {
                            String[] part = str.split("-");

                            if (part[0].equals(" "))
                                part[0] = "(select min(" + columnsOrder.get(counter) + ") from TaxiDepot)";
                            if (part[1].equals(" "))
                                part[1] = "(select max(" + columnsOrder.get(counter) + ") from TaxiDepot)";

                            if (this.first == false)
                                newSQLcommand += " and " + columnsOrder.get(counter) + " BETWEEN " + part[0] + " and " + part[1];
                            if (this.first == true) {
                                newSQLcommand += columnsOrder.get(counter) + " BETWEEN " + part[0] + " and " + part[1];
                                this.first = false;
                            }
                        }
                        if (counter == 10) {
                            if (str.equals("1")) {
                                if (this.first == false)
                                    newSQLcommand += " and " + columnsOrder.get(counter) + " is null";
                                if (this.first == true) {
                                    newSQLcommand += columnsOrder.get(counter) + " is null";
                                    this.first = false;
                                }
                            }

                        }
                    }
                counter++;
            });
            if (first != true){
                sqlCommand = newSQLcommand;
                if(searchBar == true && !searchBarS.equals(""))
                    sqlCommand += " and fullname like '%"+searchBarS+"%'";
            }
        }
        if(searchBar == true && first == true && !searchBarS.equals(""))
            sqlCommand += " where fullname like '%"+searchBarS+"%'";

        System.out.println(sqlCommand);

        ResultSet resultSet = statement.executeQuery(sqlCommand);
        VBox vBox = (myCabinet == true) ? myRentCarsVB : rentCarVB;
        while (resultSet.next()) {
            //vars
            int ID = resultSet.getInt("ID");
            String fuel = resultSet.getString("Fuel");
            String bodyType = resultSet.getString("BodyType");
            String brand = resultSet.getString("Brand");
            String model = resultSet.getString("Model");
            String year = resultSet.getString("Year");
            String price = resultSet.getString("Price");
            String priceToRent = resultSet.getString("PriceToRent");
            String transmission = resultSet.getString("Transmission");
            int userAccountID = resultSet.getInt("UserAccountID");
            float litersPer100Km = resultSet.getFloat("LitersPer100Km");
            float accelerationTo100KmAnHour = resultSet.getFloat("AccelerationTo100KmAnHour");
            InputStream img = resultSet.getBinaryStream("IMG");

            try {
                FXMLLoader loader = new FXMLLoader();
                Node node = loader.load(getClass().getResource("HBoxDB.fxml").openStream());
                vBox.getChildren().add(node);
                //get the controller
                HBoxDB controller = (HBoxDB) loader.getController();
                //my function
                controller.setContent(ID, brand, model, year, bodyType, transmission, fuel, priceToRent, price,
                        Float.toString(litersPer100Km), Float.toString(accelerationTo100KmAnHour),
                        userAccountID, img);
                vBox.setSpacing(10);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    String searchBarS;
    @FXML
    private void searchBar(KeyEvent event) throws IOException, SQLException
    {
        searchBarS = searchBarTF.getText();
        rentCarVB.getChildren().clear();
        addRentCars(checkS_List, false, true);
    }
    public void switchToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());


        Image icon = new Image(path + "logo.png");
        stage.getIcons().add(icon);
        stage.setTitle("TaxiDepotApp");
        stage.setMinWidth(1245);
        stage.setMinHeight(600);

        stage.setScene(this.scene);
        stage.show();
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
            Stage stage = (Stage) tabPane.getScene().getWindow();
            stage.close();
        }
    }

    public void switchToMain(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log out");
        alert.setHeaderText("Do you want to log out?");

        ButtonType buttonTypeYES = new ButtonType("Yes");
        ButtonType buttonTypeNO = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeYES, buttonTypeNO);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonTypeYES) {
            Main.ID = 0;
            Parent root;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            root = loader.load();
            MainMenu mainMenu = loader.getController();
            mainMenu.switchToMain(event);
            mainMenu.stage.setWidth(700);
            mainMenu.stage.setHeight(500);
        }
    }
}
