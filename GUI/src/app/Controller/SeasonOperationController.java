package app.Controller;

import app.DAO.SeasonDao;
import app.Entity.Season;
import app.Other.DBRecordToStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class SeasonOperationController {
    private SeasonDao seasonDao = new SeasonDao();
    private DBRecordToStringConverter dbRecordToStringConverter = new DBRecordToStringConverter();
    private ObservableList<Season> seasonObservableList = FXCollections.observableArrayList();
    @FXML
    private DatePicker newSeasonStartDate;
    @FXML
    private DatePicker newSeasonEndDate;
    @FXML
    private DatePicker editSeasonStartDate;
    @FXML
    private DatePicker editSeasonEndDate;
    @FXML
    private ComboBox<Season> seasonToEditCombobox;
    @FXML
    private ComboBox<Season> seasonToDelete;
    @FXML
    private Button addNewSeasonButton;
    @FXML
    private Button editSeasonButton;
    @FXML
    private Button deleteSeasonButton;

    @FXML
    public void initialize() {
        this.validation();
        seasonToEditCombobox.setConverter(dbRecordToStringConverter.getSeasonStringConverter());
        seasonToDelete.setConverter(dbRecordToStringConverter.getSeasonStringConverter());
        this.setSeasonObservableList();
        this.setAllComboBox();
    }

    @FXML
    private void validation() {
        this.addNewSeasonButton.disableProperty().bind(newSeasonStartDate.valueProperty().isNull().or(newSeasonEndDate.valueProperty().isNull()));
        this.editSeasonButton.disableProperty().bind(seasonToEditCombobox.valueProperty().isNull().or(editSeasonStartDate.valueProperty().isNull().or(editSeasonEndDate.valueProperty().isNull())));
        this.deleteSeasonButton.disableProperty().bind(seasonToDelete.valueProperty().isNull());
    }

    private void setSeasonObservableList() {
        seasonObservableList.clear();
        seasonObservableList.addAll(seasonDao.all());
    }

    private void setAllComboBox() {
        this.seasonToEditCombobox.setItems(seasonObservableList);
        this.seasonToDelete.setItems(seasonObservableList);
    }

    @FXML
    private void editSeason() {
        Season season = seasonToEditCombobox.getValue();
        season.setSeason_start(editSeasonStartDate.getValue());
        season.setSeason_end(editSeasonEndDate.getValue());
        seasonDao.update(season);
        seasonToEditCombobox.setValue(null);
        editSeasonStartDate.setValue(null);
        editSeasonEndDate.setValue(null);
        this.setSeasonObservableList();
        this.setAllComboBox();
    }

    @FXML
    private void addNewSeasonToDB() {
        Season season = new Season();
        season.setSeason_start(newSeasonStartDate.getValue());
        season.setSeason_end(newSeasonEndDate.getValue());
        seasonDao.insert(season);
        this.setSeasonObservableList();
        this.setAllComboBox();
        newSeasonStartDate.setValue(null);
        newSeasonEndDate.setValue(null);
    }

    @FXML
    private void deleteSeason() {
        seasonDao.delete(this.seasonToDelete.getValue());
        this.setSeasonObservableList();
        this.setAllComboBox();
        this.seasonToDelete.setValue(null);
    }

    @FXML
    private void closeDialog() {
        Stage stage = (Stage) addNewSeasonButton.getScene().getWindow();
        stage.close();
    }
}
