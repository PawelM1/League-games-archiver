package app.Controller;

import app.DAO.GameDao;
import app.DAO.SeasonLeagueDao;
import app.Entity.Games;
import app.Entity.Team;
import app.Other.DBRecordToStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class AddGameController {
    @FXML
    public ComboBox<Team> hTeamComboBox;
    @FXML
    public ComboBox<Team> gTeamCombobox;
    @FXML
    public Button addButton;
    @FXML
    public Button cancelButton;
    @FXML
    public TextField hScore;
    @FXML
    public TextField gScore;
    @FXML
    public DatePicker gameDate;
    private SeasonLeagueDao seasonLeagueDao = new SeasonLeagueDao();
    private GameDao gameDao = new GameDao();
    private DBRecordToStringConverter dbRecordToStringConverter = new DBRecordToStringConverter();
    private int leagueId;
    private int seasonId;

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    @FXML
    public void initialize() {
        validation();
        gTeamCombobox.setConverter(dbRecordToStringConverter.getTeamStringConverter());
        hTeamComboBox.setConverter(dbRecordToStringConverter.getTeamStringConverter());
        onlyNumberInTextField();

    }

    private void validation() {
        addButton.disableProperty().bind(this.hTeamComboBox.valueProperty().isNull()
                .or(this.gTeamCombobox.valueProperty().isNull()
                        .or(this.gScore.textProperty().isEmpty().
                                or(this.hScore.textProperty().isEmpty()
                                        .or(this.gameDate.valueProperty().isNull())))));
    }

    public void setTeamComboBox() {
        ObservableList<Team> teamObservableList = FXCollections.observableArrayList();
        teamObservableList.clear();
        List<Team> teamList = seasonLeagueDao.getTeamBySeasonAndLeagueId(leagueId, seasonId);
        teamObservableList.addAll(teamList);
        hTeamComboBox.setItems(teamObservableList);
        gTeamCombobox.setItems(teamObservableList);
    }

    public void addMatchToDB() {
        Games game = new Games();
        game.setHost_team(hTeamComboBox.getValue());
        game.setGuest_team(gTeamCombobox.getValue());
        game.setGuest_team_score(Integer.parseInt(gScore.getText()));
        game.setHost_team_score(Integer.parseInt(hScore.getText()));
        game.setGameDate(gameDate.getValue());
        gameDao.insert(game, leagueId, seasonId);
        hTeamComboBox.setValue(null);
        gTeamCombobox.setValue(null);
        hScore.clear();
        gScore.clear();
    }

    public void closeDialog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void onlyNumberInTextField() {
        hScore.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                hScore.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        gScore.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                gScore.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
