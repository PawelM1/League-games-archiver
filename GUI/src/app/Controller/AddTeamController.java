package app.Controller;

import app.DAO.LeagueTeamDao;
import app.DAO.TeamDao;
import app.Entity.League;
import app.Entity.Season;
import app.Entity.Team;
import app.Other.DBRecordToStringConverter;
import app.Other.DialogBox;
import app.Other.Loaders;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.PersistenceException;

public class AddTeamController {
    private TeamDao teamDao = new TeamDao();
    private LeagueTeamDao leagueTeamDao = new LeagueTeamDao();
    private League league;
    private Season season;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private Button addNewTeam;
    @FXML
    private Button addExistTeam;
    @FXML
    private Button cancelButton;
    @FXML
    private ComboBox<Team> teamComboBox;

    @FXML
    private void initialize() {
        this.validation();
        this.teamComboBox.setConverter(new DBRecordToStringConverter().getTeamStringConverter());
        this.setTeamComboBox();
    }


    public void validation() {
        this.addNewTeam.disableProperty().bind(this.nameTextField.textProperty().isEmpty()
                .or(this.cityTextField.textProperty().isEmpty()));
        this.addExistTeam.disableProperty().bind(this.teamComboBox.valueProperty().isNull());
    }

    private void setTeamComboBox() {
        ObservableList<Team> teamObservableList = FXCollections.observableArrayList();
        teamObservableList.clear();
        teamObservableList.addAll(teamDao.all());
        teamComboBox.setItems(teamObservableList);
    }

    @FXML
    public void closeDialog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void addTeamToDB() {
        Team team = new Team();
        team.setName(this.nameTextField.getText());
        team.setCity(this.cityTextField.getText());
        try {
            teamDao.insert(team);
            leagueTeamDao.insert(league, teamDao.getTeamIdByName(this.nameTextField.getText()), season);
        } catch (PersistenceException e) {
            DialogBox.ExceptionDialog(Loaders.getResourceBundle().getString("Exception.create"));
        }
        this.nameTextField.clear();
        this.cityTextField.clear();
        this.setTeamComboBox();
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public void addExistTeam() {
        this.leagueTeamDao.insert(league, teamComboBox.getValue(), season);
        this.setTeamComboBox();
    }
}
