package app.Entity;

import java.time.LocalDate;

public class Games {
    private Integer game_id;
    private Team host_team;
    private Team guest_team;
    private Integer host_team_score;
    private Integer guest_team_score;
    private LocalDate GameDate;
    private int season_id;
    private int league_id;

    public Integer getGame_id() {
        return game_id;
    }

    public void setGame_id(Integer game_id) {
        this.game_id = game_id;
    }

    public Team getHost_team() {
        return host_team;
    }

    public void setHost_team(Team host_team) {
        this.host_team = host_team;
    }

    public Team getGuest_team() {
        return guest_team;
    }

    public void setGuest_team(Team guest_team) {
        this.guest_team = guest_team;
    }

    public Integer getHost_team_score() {
        return host_team_score;
    }

    public void setHost_team_score(Integer host_team_score) {
        this.host_team_score = host_team_score;
    }

    public Integer getGuest_team_score() {
        return guest_team_score;
    }

    public void setGuest_team_score(Integer guest_team_score) {
        this.guest_team_score = guest_team_score;
    }

    public LocalDate getGameDate() {
        return GameDate;
    }

    public void setGameDate(LocalDate gameDate) {
        GameDate = gameDate;
    }

    public int getSeason_id() {
        return season_id;
    }

    public void setSeason_id(int season_id) {
        this.season_id = season_id;
    }

    public int getLeague_id() {
        return league_id;
    }

    public void setLeague_id(int league_id) {
        this.league_id = league_id;
    }
}
