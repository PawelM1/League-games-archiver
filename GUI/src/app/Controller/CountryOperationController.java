package app.Controller;

import app.DAO.CountryDao;
import app.Entity.Country;
import app.Other.DBRecordToStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CountryOperationController {
    private CountryDao countryDao = new CountryDao();
    private DBRecordToStringConverter dbRecordToStringConverter = new DBRecordToStringConverter();
    private ObservableList<Country> countryObservableList = FXCollections.observableArrayList();
    @FXML
    private TextField newNameTextField;
    @FXML
    private TextField editNameTextField;
    @FXML
    private ComboBox<Country> countryToEditCombobox;
    @FXML
    private ComboBox<Country> countryToDelete;
    @FXML
    private Button addNewCountryButton;
    @FXML
    private Button deleteCountryButton;
    @FXML
    private Button editCountryButton;

    @FXML
    public void initialize() {
        this.validation();
        countryToEditCombobox.setConverter(dbRecordToStringConverter.getCountryStringConverter());
        countryToDelete.setConverter(dbRecordToStringConverter.getCountryStringConverter());
        this.setCountryObservableList();
        this.setAllComboBox();
    }

    private void validation() {
        this.addNewCountryButton.disableProperty().bind(newNameTextField.textProperty().isEmpty());
        this.deleteCountryButton.disableProperty().bind(countryToDelete.valueProperty().isNull());
        this.editCountryButton.disableProperty().bind(countryToEditCombobox.valueProperty().isNull().or(editNameTextField.textProperty().isEmpty()));
    }

    private void setCountryObservableList() {
        this.countryObservableList.clear();
        this.countryObservableList.addAll(countryDao.all());
    }

    private void setAllComboBox() {
        this.countryToEditCombobox.setItems(countryObservableList);
        this.countryToDelete.setItems(countryObservableList);
    }


    @FXML
    private void addNewCountryToDB() {
        Country country = new Country();
        country.setName(newNameTextField.getText());
        countryDao.insert(country);
        this.newNameTextField.clear();
        this.setCountryObservableList();
        this.setAllComboBox();
    }

    @FXML
    private void editCountry() {
        Country country = countryToEditCombobox.getValue();
        country.setName(editNameTextField.getText());
        countryDao.update(country);
        editNameTextField.clear();
        this.setCountryObservableList();
        this.setAllComboBox();
    }

    @FXML
    private void deleteCountry() {
        countryDao.delete(this.countryToDelete.getValue());
        countryToDelete.setValue(null);
        this.setCountryObservableList();
        this.setAllComboBox();
    }

    @FXML
    private void closeDialog() {
        Stage stage = (Stage) addNewCountryButton.getScene().getWindow();
        stage.close();
    }
}
