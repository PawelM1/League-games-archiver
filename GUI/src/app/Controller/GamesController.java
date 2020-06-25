package app.Controller;

import app.DAO.GameDao;
import app.DAO.LeagueDao;
import app.DAO.SeasonDao;
import app.Entity.Games;
import app.Entity.League;
import app.Entity.Season;
import app.Other.DBRecordToStringConverter;
import app.Other.DialogBox;
import app.Other.Loaders;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class GamesController {
    private SeasonDao seasonDao = new SeasonDao();
    private LeagueDao leagueDao = new LeagueDao();
    private GameDao gameDao = new GameDao();
    private ObservableList<Games> gamesObservableList = FXCollections.observableArrayList();
    private List<League> leagueListName = leagueDao.all();
    @FXML
    private TableColumn<Games, String> leagueColumn;
    @FXML
    private TableView<Games> gamesTableView;
    @FXML
    private ComboBox<Season> seasonComboBox;
    @FXML
    private ComboBox<League> leagueComboBox;
    @FXML
    private TableColumn<Games, String> hostTeamColumn;
    @FXML
    private TableColumn<Games, String> guestTeamColumn;
    @FXML
    private TableColumn<Games, Integer> hostGoalColumn;
    @FXML
    private TableColumn<Games, Integer> guestGoalColumn;
    @FXML
    private TableColumn<Games, LocalDate> gameDateColumn;

    @FXML
    public void initialize() {
        this.bindingGamesTableView();
        this.changeComboBoxValueFromAddressToString();
        this.setComboBox();
        this.setGamesObservableList();
        this.setCellFactoryToEditCell();
        this.setDeletePressedAction();
    }

    private void bindingGamesTableView() {
        this.leagueColumn.setCellValueFactory(gamesStringCellDataFeatures -> new SimpleStringProperty(this.getLeagueName(gamesStringCellDataFeatures.getValue().getLeague_id())));
        this.hostTeamColumn.setCellValueFactory(gamesIntegerCellDataFeatures -> new SimpleStringProperty(gamesIntegerCellDataFeatures.getValue().getHost_team().getName()));
        this.guestTeamColumn.setCellValueFactory(gamesStringCellDataFeatures -> new SimpleStringProperty(gamesStringCellDataFeatures.getValue().getGuest_team().getName()));
        this.hostGoalColumn.setCellValueFactory(gamesIntegerCellDataFeatures -> new SimpleIntegerProperty(gamesIntegerCellDataFeatures.getValue().getHost_team_score()).asObject());
        this.guestGoalColumn.setCellValueFactory(gamesIntegerCellDataFeatures -> new SimpleIntegerProperty(gamesIntegerCellDataFeatures.getValue().getGuest_team_score()).asObject());
        this.gameDateColumn.setCellValueFactory(gamesLocalDateCellDataFeatures -> new SimpleObjectProperty<>(gamesLocalDateCellDataFeatures.getValue().getGameDate()));
    }

    private void setCellFactoryToEditCell() {
        this.hostGoalColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        this.guestGoalColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        this.gameDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
    }

    private void setSeasonComboBox() {
        ObservableList<Season> seasonObservableList = FXCollections.observableArrayList();
        seasonObservableList.clear();
        seasonObservableList.addAll(seasonDao.all());
        seasonComboBox.setItems(seasonObservableList);
    }

    private void setLeagueComboBox() {
        ObservableList<League> leagueObservableList1 = FXCollections.observableArrayList();
        leagueObservableList1.clear();
        List<League> leagueBySeasonId = leagueDao.all();
        leagueObservableList1.addAll(leagueBySeasonId);
        leagueComboBox.setItems(leagueObservableList1);
    }

    private void changeComboBoxValueFromAddressToString() {
        DBRecordToStringConverter dbRecordToStringConverter = new DBRecordToStringConverter();
        seasonComboBox.setConverter(dbRecordToStringConverter.getSeasonStringConverter());
        leagueComboBox.setConverter(dbRecordToStringConverter.getLeagueStringConverter());
    }

    private void setComboBox() {
        setSeasonComboBox();
        setLeagueComboBox();
    }

    private String getLeagueName(int id) {
        for (League l : leagueListName
        ) {
            if (l.getLeague_id() == id) return l.getLeague_name();
        }
        return "---";
    }

    private void setGamesObservableList() {
        this.gamesObservableList.clear();
        List<Games> gamesList = gameDao.all();
        if (seasonComboBox.getValue() != null)
            gamesList.removeIf(games -> games.getSeason_id() != seasonComboBox.getValue().getSeason_id());
        if (leagueComboBox.getValue() != null)
            gamesList.removeIf(games -> games.getLeague_id() != leagueComboBox.getValue().getLeague_id());
        this.gamesObservableList.addAll(gamesList);
        this.gamesTableView.setItems(gamesObservableList);
    }

    @FXML
    private void setGamesOnSeasonChange() {
        this.setGamesObservableList();
    }

    @FXML
    private void setGamesOnLeagueChange() {
        this.setGamesObservableList();
    }

    private void setDeletePressedAction() {
        gamesTableView.setOnKeyPressed(keyEvent -> {
            Games games = gamesTableView.getSelectionModel().getSelectedItem();
            if (games != null && keyEvent.getCode().equals(KeyCode.DELETE)) {
                Optional<ButtonType> result = DialogBox.confirmationExitDialog(Loaders.getResourceBundle().getString("GamesController.Delete.Title"), Loaders.getResourceBundle().getString("GamesController.Delete.Header"));
                if (result.get() == ButtonType.OK) {
                    gamesObservableList.remove(games);
                    gameDao.delete(games);
                }
            }

        });
    }

    @FXML
    private void editHostGoal(TableColumn.CellEditEvent<Games, Integer> gamesIntegerCellEditEvent) {
        Games games = gamesTableView.getSelectionModel().getSelectedItem();
        if (gamesIntegerCellEditEvent.getNewValue().compareTo(gamesIntegerCellEditEvent.getOldValue()) != 0) {
            games.setHost_team_score(gamesIntegerCellEditEvent.getNewValue());
            gameDao.update(games);
        }
    }

    @FXML
    private void editGuestGoal(TableColumn.CellEditEvent<Games, Integer> gamesIntegerCellEditEvent) {
        Games games = gamesTableView.getSelectionModel().getSelectedItem();
        if (gamesIntegerCellEditEvent.getNewValue().compareTo(gamesIntegerCellEditEvent.getOldValue()) != 0) {
            games.setGuest_team_score(gamesIntegerCellEditEvent.getNewValue());
            gameDao.update(games);
        }
    }

    @FXML
    private void editGameDate(TableColumn.CellEditEvent<Games, LocalDate> gamesLocalDateCellEditEvent) {
        Games games = gamesTableView.getSelectionModel().getSelectedItem();
        if (gamesLocalDateCellEditEvent.getNewValue().compareTo(gamesLocalDateCellEditEvent.getOldValue()) != 0) {
            games.setGameDate(gamesLocalDateCellEditEvent.getNewValue());
            gameDao.update(games);
        }
    }
}
