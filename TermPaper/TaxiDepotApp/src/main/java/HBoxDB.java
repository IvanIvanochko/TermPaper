import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

public class HBoxDB {
    private String path = "G:\\Інші комп’ютери\\My laptop\\Політехніка\\Прикладне програмування\\Лабораторні\\Комплексна робота\\TaxiDepotApp\\src\\main\\resources\\";
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    Label brandL, modelL, yearL, priceL,
            bodyTypeL, transmissionL,
            fuelL, priceToRentL,
            litersPer100KmL, accelerationL, unoccupiedL;

    @FXML
    Button rentB;
    @FXML
    ImageView pictureIV;

    public void setContent(int ID, String brand, String model, String year, String bodyType,
                           String transmission, String fuel, String priceToRent, String price,
                           String litersPer100Km, String acceleration, int userAccountID,
                           InputStream img){
        this.ID = ID;
        brandL.setText(brand);
        modelL.setText(model);
        yearL.setText(year);
        bodyTypeL.setText(bodyType);
        transmissionL.setText(transmission);
        fuelL.setText(fuel);
        priceToRentL.setText(priceToRent.substring(0, priceToRent.indexOf(".")) + "$");
        priceL.setText(price.substring(0, price.indexOf(".")) + "$");

        // Add logic | this is only for gasoline
        litersPer100KmL.setText(litersPer100Km);

        accelerationL.setText(acceleration);
        unoccupiedL.setText((userAccountID == 0) ? "Unoccupied" : "Occupied");
//        boolean notAvailableCar = true;
//        if(userAccountID == 0 && userAccountID == Main.ID)

        rentB.setDisable((userAccountID == 0 || userAccountID == Main.ID) ? false : true);
        if(userAccountID == Main.ID)
            rentB.setText("Unrent");

        if(img != null) {
            pictureIV.setImage(null);
            Image image = new Image(img, 400, 250, false, false); // , 400, 250, false, false
            pictureIV.setImage(image);
        }
        if(img == null){
            File file = new File("NoImageFound.png");
            Image image = new Image(file.toURI().toString(), 400, 246, false, false);
            pictureIV.setImage(image);
        }

    }
    int ID;
    public void getButton(ActionEvent event){
        boolean changed = false;
        if(rentB.getText().equals("Rent") && changed == false) {
            String sqlCommand = "update TaxiDepot set UserAccountID = " + Main.ID + " where ID = " + this.ID + ";";
            try {
                LogIn.statement.execute(sqlCommand);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            rentB.setText("Unrent");
            changed = true;
        }
        if(rentB.getText().equals("Unrent") && changed == false) {
            String sqlCommand = "update TaxiDepot set UserAccountID = null where ID = " + this.ID + ";";
            try {
                LogIn.statement.execute(sqlCommand);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            rentB.setText("Rent");
            changed = true;
        }
    }
    @FXML
    void action(ActionEvent e){
        rentB.setText("Action");
        System.out.println("Action!");
    }
}
