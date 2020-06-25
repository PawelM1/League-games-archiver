package app.Controller;

import app.DAO.CountryDao;
import app.DAO.LeagueDao;
import app.DAO.SeasonDao;
import app.DAO.SeasonLeagueDao;
import app.Entity.Country;
import app.Entity.League;
import app.Entity.Season;
import app.Other.DBRecordToStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class LeagueOperationController {
    private ObservableList<League> leagueObservableList = FXCollections.observableArrayList();
    private ObservableList<Season> seasonObservableList = FXCollections.observableArrayList();
    private ObservableList<Country> countryObservableList = FXCollections.observableArrayList();
    private LeagueDao leagueDao = new LeagueDao();
    private SeasonDao seasonDao = new SeasonDao();
    private CountryDao countryDao = new CountryDao();
    private SeasonLeagueDao seasonLeagueDao = new SeasonLeagueDao();
    //Textfield
    @FXML
    private TextField editLeagueNameTextField;
    @FXML
    private TextField newLeagueNameTextField;
    //combobox
    @FXML
    private ComboBox<Country> newLeagueCountry;
    @FXML
    private ComboBox<Country> editLeagueCountry;
    @FXML
    private ComboBox<League> leagueSeasonToDelete;
    @FXML
    private ComboBox<League> leagueToEdit;
    @FXML
    private ComboBox<Season> seasonToDeleteFromLeague;
    @FXML
    private ComboBox<League> leagueToDeleteCompletely;
    @FXML
    private ComboBox<League> leagueSeasonAdd;
    @FXML
    private ComboBox<Season> seasonLeagueAdd;
    //button
    @FXML
    private Button addNewLeagueButton;
    @FXML
    private Button editLeagueButton;
    @FXML
    private Button deleteSeasonFromLeagueButton;
    @FXML
    private Button deleteLeagueCompletelyButton;
    @FXML
    private Button addSeasonToLeagueButton;

    @FXML
    public void initialize() {
        this.validation();
        this.convertEntityAddressFromComboboxToString();
        seasonObservableList.addAll(seasonDao.all());
        countryObservableList.addAll(countryDao.all());
        this.setLeagueObservableList();
        this.setAllCombobox();
    }

    private void convertEntityAddressFromComboboxToString() {
        DBRecordToStringConverter dbRecordToStringConverter = new DBRecordToStringConverter();
        newLeagueCountry.setConverter(dbRecordToStringConverter.getCountryStringConverter());
        editLeagueCountry.setConverter(dbRecordToStringConverter.getCountryStringConverter());
        leagueSeasonToDelete.setConverter(dbRecordToStringConverter.getLeagueStringConverter());
        leagueToEdit.setConverter(dbRecordToStringConverter.getLeagueStringConverter());
        seasonToDeleteFromLeague.setConverter(dbRecordToStringConverter.getSeasonStringConverter());
        leagueToDeleteCompletely.setConverter(dbRecordToStringConverter.getLeagueStringConverter());
        leagueSeasonAdd.setConverter(dbRecordToStringConverter.getLeagueStringConverter());
        seasonLeagueAdd.setConverter(dbRecordToStringConverter.getSeasonStringConverter());
    }

    private void setLeagueObservableList() {
        leagueObservableList.clear();
        leagueObservableList.addAll(leagueDao.all());

    }

    private void setAllCombobox() {
        this.setNewLeagueCountry();
        this.setNewCountryToLeague();
        this.setLeagueSeasonToDelete();
        this.setLeagueToEdit();
        this.setLeagueToDeleteCompletely();
        this.setLeagueSeasonAdd();
        this.setSeasonLeagueAdd();
    }

    private void validation() {
        this.addNewLeagueButton.disableProperty().bind(this.newLeagueCountry.valueProperty().isNull().or(this.newLeagueNameTextField.textProperty().isEmpty()));
        this.addSeasonToLeagueButton.disableProperty().bind(seasonLeagueAdd.valueProperty().isNull().or(this.leagueSeasonAdd.valueProperty().isNull()));
        this.editLeagueButton.disableProperty().bind(this.leagueToEdit.valueProperty().isNull().or(this.editLeagueNameTextField.textProperty().isEmpty().or(this.editLeagueCountry.valueProperty().isNull())));
        this.deleteSeasonFromLeagueButton.disableProperty().bind(this.seasonToDeleteFromLeague.valueProperty().isNull().or(this.leagueSeasonToDelete.valueProperty().isNull()));
        this.deleteLeagueCompletelyButton.disableProperty().bind(this.leagueToDeleteCompletely.valueProperty().isNull());
    }

    public void setNewLeagueCountry() {
        this.newLeagueCountry.setItems(countryObservableList);
    }

    public void setNewCountryToLeague() {
        this.editLeagueCountry.setItems(countryObservableList);
    }

    public void setLeagueSeasonToDelete() {
        this.leagueSeasonToDelete.setItems(leagueObservableList);
    }

    public void setLeagueToEdit() {
        this.leagueToEdit.setItems(leagueObservableList);
    }

    public void setLeagueToDeleteCompletely() {
        this.leagueToDeleteCompletely.setItems(leagueObservableList);
    }

    public void setLeagueSeasonAdd() {
        this.leagueSeasonAdd.setItems(leagueObservableList);
    }

    public void setSeasonLeagueAdd() {
        this.seasonLeagueAdd.setItems(seasonObservableList);
    }

    @FXML
    private void setSeasonToDeleteComboBox() {
        if (leagueSeasonToDelete.getValue() != null) {
            List<Season> tmpSeasonList = seasonLeagueDao.getSeasonByLeagueId(leagueSeasonToDelete.getValue().getLeague_id());
            ObservableList<Season> tmpSeasonObservableList = FXCollections.observableArrayList();
            tmpSeasonObservableList.addAll(tmpSeasonList);
            seasonToDeleteFromLeague.setItems(tmpSeasonObservableList);
        }
    }

    public void closeDialog() {
        Stage stage = (Stage) addSeasonToLeagueButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addNewLeagueToDB() {
        League league = new League();
        league.setLeague_name(this.newLeagueNameTextField.getText());
        league.setCountry(newLeagueCountry.getValue());
        leagueDao.insert(league);
        this.newLeagueNameTextField.clear();
        this.newLeagueCountry.setValue(null);
        this.setLeagueObservableList();
        this.setAllCombobox();
    }

    @FXML
    private void addSeasonToLeague() {
        seasonLeagueDao.insert(this.leagueSeasonAdd.getValue(), this.seasonLeagueAdd.getValue());
        this.leagueSeasonAdd.setValue(null);
        this.seasonLeagueAdd.setValue(null);
    }

    @FXML
    private void editLeague() {
        League league = this.leagueToEdit.getValue();
        league.setLeague_name(this.editLeagueNameTextField.getText());
        league.setCountry(this.editLeagueCountry.getValue());
        editLeagueNameTextField.clear();
        editLeagueCountry.setValue(null);
        leagueDao.update(league);
        this.setLeagueObservableList();
        this.setAllCombobox();
    }

    @FXML
    private void deleteSeasonFromLeague() {
        seasonLeagueDao.deleteSeasonLeague(this.seasonToDeleteFromLeague.getValue().getSeason_id(), this.leagueSeasonToDelete.getValue().getLeague_id());
        this.seasonToDeleteFromLeague.setValue(null);
        this.leagueSeasonToDelete.setValue(null);
        this.setLeagueObservableList();
        this.setAllCombobox();
    }

    @FXML
    private void deleteLeagueCompletely() {
        leagueDao.delete(this.leagueToDeleteCompletely.getValue());
        leagueToDeleteCompletely.setValue(null);
        this.setLeagueObservableList();
        this.setAllCombobox();
    }
}
