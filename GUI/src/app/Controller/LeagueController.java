package app.Controller;

import app.DAO.*;
import app.Entity.Country;
import app.Entity.League;
import app.Entity.Season;
import app.Model.TeamDetailsForTableViewModel;
import app.Other.DBRecordToStringConverter;
import app.Other.DialogBox;
import app.Other.Loaders;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class LeagueController {
    public static final String MODAL_WINDOWS_STYLE = "app/Other/css/MainStyle.css";
    private static final String ADD_TEAM_FXML = "/resources/FXML/AddTeam.fxml";
    private static final String ADD_GAME_FXML = "/resources/FXML/AddGame.fxml";
    private static final String TEAM_DETAIL_FXML = "/resources/FXML/TeamDetail.fxml";
    @FXML
    public TableView<TeamDetailsForTableViewModel> teamTableView;
    @FXML
    public TableColumn<TeamDetailsForTableViewModel, Integer> placeColumn;
    @FXML
    public TableColumn<TeamDetailsForTableViewModel, String> teamColumn;
    @FXML
    public TableColumn<TeamDetailsForTableViewModel, Integer> pointsColumn;
    @FXML
    public TableColumn<TeamDetailsForTableViewModel, Integer> matchesColumn;
    @FXML
    public TableColumn<TeamDetailsForTableViewModel, Integer> wonColumn;
    @FXML
    public TableColumn<TeamDetailsForTableViewModel, Integer> drawColumn;
    @FXML
    public TableColumn<TeamDetailsForTableViewModel, Integer> looseColumn;
    @FXML
    public TableColumn<TeamDetailsForTableViewModel, Integer> scoreGoalsColumn;
    @FXML
    public TableColumn<TeamDetailsForTableViewModel, Integer> looseGoalsColumn;
    @FXML
    public TableColumn<TeamDetailsForTableViewModel, Integer> balanceColumn;
    @FXML
    public TableColumn<TeamDetailsForTableViewModel, League> deleteColumn;
    private LeagueDao leagueDao = new LeagueDao();
    private CountryDao countryDao = new CountryDao();
    private SeasonDao seasonDao = new SeasonDao();
    private TeamDao teamDao = new TeamDao();
    private SeasonLeagueDao seasonLeagueDao = new SeasonLeagueDao();
    private ObservableList<TeamDetailsForTableViewModel> teamObservableList = FXCollections.observableArrayList();
    @FXML
    private Button addTeamButton;
    @FXML
    private Button addMatchButton;
    @FXML
    private ComboBox<Season> seasonComboBox;
    @FXML
    private ComboBox<Country> countryComboBox;
    @FXML
    private ComboBox<League> leagueComboBox;

    @FXML
    public void initialize() {
        this.setComboBox();
        this.bindingTeamTableView();
        validation();
    }

    private void bindingTeamTableView() {
        this.placeColumn.setCellValueFactory(teamDetailsForTableViewModelTeamDetailsForTableViewModelCellDataFeatures -> new SimpleIntegerProperty(teamDetailsForTableViewModelTeamDetailsForTableViewModelCellDataFeatures.getValue().getPlace()).asObject());
        this.teamColumn.setCellValueFactory(teamDetailsForTableViewModelStringCellDataFeatures -> new SimpleStringProperty(teamDetailsForTableViewModelStringCellDataFeatures.getValue().getTeam().getName()));
        this.pointsColumn.setCellValueFactory(teamDetailsForTableViewModelIntegerCellDataFeatures -> new SimpleIntegerProperty(teamDetailsForTableViewModelIntegerCellDataFeatures.getValue().getPoints()).asObject());
        this.matchesColumn.setCellValueFactory(teamDetailsForTableViewModelIntegerCellDataFeatures -> new SimpleIntegerProperty(teamDetailsForTableViewModelIntegerCellDataFeatures.getValue().getMatches()).asObject());
        this.wonColumn.setCellValueFactory(teamDetailsForTableViewModelIntegerCellDataFeatures -> new SimpleIntegerProperty(teamDetailsForTableViewModelIntegerCellDataFeatures.getValue().getWon()).asObject());
        this.drawColumn.setCellValueFactory(teamDetailsForTableViewModelIntegerCellDataFeatures -> new SimpleIntegerProperty(teamDetailsForTableViewModelIntegerCellDataFeatures.getValue().getDraw()).asObject());
        this.looseColumn.setCellValueFactory(teamDetailsForTableViewModelIntegerCellDataFeatures -> new SimpleIntegerProperty(teamDetailsForTableViewModelIntegerCellDataFeatures.getValue().getLoose()).asObject());
        this.scoreGoalsColumn.setCellValueFactory(teamDetailsForTableViewModelIntegerCellDataFeatures -> new SimpleIntegerProperty(teamDetailsForTableViewModelIntegerCellDataFeatures.getValue().getScoreGoals()).asObject());
        this.looseGoalsColumn.setCellValueFactory(teamDetailsForTableViewModelIntegerCellDataFeatures -> new SimpleIntegerProperty(teamDetailsForTableViewModelIntegerCellDataFeatures.getValue().getLostGoals()).asObject());
        this.balanceColumn.setCellValueFactory(teamDetailsForTableViewModelIntegerCellDataFeatures -> new SimpleIntegerProperty(teamDetailsForTableViewModelIntegerCellDataFeatures.getValue().getBalance()).asObject());
    }

    private void setCountryComboBox() {
        ObservableList<Country> countryObservableList = FXCollections.observableArrayList();
        countryObservableList.clear();
        countryObservableList.addAll(countryDao.all());
        countryComboBox.setItems(countryObservableList);
    }

    private void setSeasonComboBox() {
        ObservableList<Season> seasonObservableList = FXCollections.observableArrayList();
        seasonObservableList.clear();
        seasonObservableList.addAll(seasonDao.all());
        seasonComboBox.setItems(seasonObservableList);
    }

    private void setLeagueComboBox() {
        if (seasonComboBox.getValue() != null && countryComboBox.getValue() != null) {
            ObservableList<League> leagueObservableList1 = FXCollections.observableArrayList();
            leagueObservableList1.clear();
            List<League> leagueBySeasonId = seasonLeagueDao.getLeagueBySeasonId(seasonComboBox.getValue().getSeason_id());
            leagueBySeasonId.removeIf(league -> !league.getCountry().getCountry_id().equals(countryComboBox.getValue().getCountry_id()));
            leagueObservableList1.addAll(leagueBySeasonId);
            leagueComboBox.setItems(leagueObservableList1);
        }
    }

    private void setComboBox() {
        DBRecordToStringConverter dbRecordToStringConverter = new DBRecordToStringConverter();
        countryComboBox.setConverter(dbRecordToStringConverter.getCountryStringConverter());
        seasonComboBox.setConverter(dbRecordToStringConverter.getSeasonStringConverter());
        leagueComboBox.setConverter(dbRecordToStringConverter.getLeagueStringConverter());
        setSeasonComboBox();
        setCountryComboBox();
        setLeagueComboBox();
    }

    private void setTeamObservableList() {
        if (leagueComboBox.getValue() != null) {
            teamObservableList.clear();
            List<TeamDetailsForTableViewModel> teamList = teamDao.getTeamListWithDetails(this.leagueComboBox.getValue().getLeague_id(), this.seasonComboBox.getValue().getSeason_id());
            Collections.sort(teamList);
            Collections.reverse(teamList);
            AtomicInteger place = new AtomicInteger(1);
            teamList.forEach(team -> team.setPlace(place.getAndIncrement()));
            this.teamObservableList.addAll(teamList);
        }
    }

    @FXML
    private void actionOnSeasonChange() {
        setLeagueComboBox();
    }

    @FXML
    private void actionOnCountryChange() {
        setLeagueComboBox();
    }

    @FXML
    void actionOnLeagueChange() {
        this.setTeamObservableList();
        this.teamTableView.setItems(this.teamObservableList);
    }

    private void validation() {
        this.addTeamButton.disableProperty().bind(this.leagueComboBox.valueProperty().isNull()
                .or(this.countryComboBox.valueProperty().isNull())
                .or(this.seasonComboBox.valueProperty().isNull()));

        this.addMatchButton.disableProperty().bind(this.leagueComboBox.valueProperty().isNull()
                .or(this.countryComboBox.valueProperty().isNull())
                .or(this.seasonComboBox.valueProperty().isNull()));
    }

    @FXML
    private void openAddTeamDialog() {
        FXMLLoader fxmlLoader = Loaders.getFXMLloader(ADD_TEAM_FXML);
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            DialogBox.ExceptionDialog(e.getMessage());
        }
        AddTeamController addTeamController = fxmlLoader.getController();
        addTeamController.setLeague(leagueDao.id(leagueComboBox.getValue().getLeague_id()));
        addTeamController.setSeason(seasonDao.id(seasonComboBox.getValue().getSeason_id()));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Objects.requireNonNull(scene).getStylesheets().add(MODAL_WINDOWS_STYLE);
        stage.setScene(scene);
        stage.setTitle(Loaders.getResourceBundle().getString("AddTeam.StageTitle"));
        stage.showAndWait();
        this.setTeamObservableList();
        this.teamTableView.setItems(this.teamObservableList);
    }

    @FXML
    private void openAddGameDialog() {
        FXMLLoader fxmlLoader = Loaders.getFXMLloader(ADD_GAME_FXML);
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            DialogBox.ExceptionDialog(e.getMessage());
        }
        AddGameController addGameController = fxmlLoader.getController();
        addGameController.setLeagueId(this.leagueComboBox.getValue().getLeague_id());
        addGameController.setSeasonId(this.seasonComboBox.getValue().getSeason_id());
        addGameController.setTeamComboBox();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Objects.requireNonNull(scene).getStylesheets().add(MODAL_WINDOWS_STYLE);
        stage.setScene(scene);
        stage.setTitle(Loaders.getResourceBundle().getString("AddGame.StageTitle"));
        stage.showAndWait();
        this.setTeamObservableList();
        this.teamTableView.setItems(this.teamObservableList);
    }

    @FXML
    private void showTeamDetail(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2 && teamTableView.getSelectionModel().getSelectedItem() != null) {
            TeamDetailsForTableViewModel team = teamTableView.getSelectionModel().getSelectedItem();

            FXMLLoader fxmlLoader = Loaders.getFXMLloader(TEAM_DETAIL_FXML);
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                DialogBox.ExceptionDialog(e.getMessage());
            }
            TeamDetailController teamDetailController = fxmlLoader.getController();
            teamDetailController.setCityTeam(team.getTeam().getCity());
            teamDetailController.setTeamName(team.getTeam().getName());
            teamDetailController.setTeam(team.getTeam());
            teamDetailController.setLeague();
            teamDetailController.setBiggestWin();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Objects.requireNonNull(scene).getStylesheets().add(MODAL_WINDOWS_STYLE);
            stage.setScene(scene);
            stage.setTitle(Loaders.getResourceBundle().getString("TeamDetail.StageTitle"));
            stage.showAndWait();
            this.setTeamObservableList();
            this.teamTableView.setItems(this.teamObservableList);
        }
    }
}
