package app.Controller;

import app.DAO.GameDao;
import app.DAO.LeagueTeamDao;
import app.DAO.TeamDao;
import app.Entity.Games;
import app.Entity.League;
import app.Entity.Team;
import app.Other.DBRecordToStringConverter;
import app.Other.Loaders;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class TeamDetailController {
    DBRecordToStringConverter dbRecordToStringConverter = new DBRecordToStringConverter();
    private Team team;
    private LeagueTeamDao leagueTeamDao = new LeagueTeamDao();
    private TeamDao teamDao = new TeamDao();
    private GameDao gameDao = new GameDao();
    @FXML
    private TextField teamName;
    @FXML
    private TextField cityTeam;
    @FXML
    private Text biggestWin;
    @FXML
    private ComboBox<League> league;
    @FXML
    private Button cancelButton;

    @FXML
    public void initialize() {
        league.setConverter(dbRecordToStringConverter.getLeagueStringConverter());
    }

    public void setBiggestWin() {
        String string = Loaders.getResourceBundle().getString("TeamDetail.Text1") + " ";
        int biggestWinGameId = teamDao.biggestWinGameId(this.team.getTeam_id());
        if (biggestWinGameId != 0) {
            Games game = gameDao.id(biggestWinGameId);
            string += Objects.equals(game.getHost_team().getTeam_id(), team.getTeam_id())
                    ? game.getHost_team_score().toString() + ":" + game.getGuest_team_score().toString() + " vs " + game.getGuest_team().getName()
                    : game.getGuest_team_score().toString() + ":" + game.getHost_team_score().toString() + " vs " + game.getHost_team().getName();
        } else string = Loaders.getResourceBundle().getString("TeamDetail.TextNoResult");
        this.biggestWin.setText(string);
    }

    public void setTeamName(String teamName) {
        this.teamName.setText(teamName);
    }

    public void setCityTeam(String cityTeam) {
        this.cityTeam.setText(cityTeam);
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setLeague() {
        ObservableList<League> leagueObservableList = FXCollections.observableArrayList();
        leagueObservableList.clear();
        leagueObservableList.addAll(leagueTeamDao.getLeagueListByTeam(team.getTeam_id()));
        league.setItems(leagueObservableList);
    }

    public void closeDialog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void deleteTeamFromLeague() {
        leagueTeamDao.deleteLeagueTeam(this.team.getTeam_id(),this.league.getValue().getLeague_id());
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void deleteTeamCompletely() {
        teamDao.delete(this.team);
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void editTeam() {
        this.team.setName(this.teamName.getText());
        this.team.setCity(this.cityTeam.getText());
        teamDao.update(this.team);
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
