package app.Model;

import app.Entity.Team;

public class TeamDetailsForTableViewModel implements Comparable<TeamDetailsForTableViewModel> {
    int place;
    int points;
    int matches;
    int won;
    int draw;
    int loose;
    int scoreGoals;
    int lostGoals;
    int balance;
    private Team team;

    @Override
    public int compareTo(TeamDetailsForTableViewModel t) {
        if (this.points != t.points) return this.points - t.points;
        if (balance != t.balance) return this.balance - t.balance;
        if (scoreGoals != t.scoreGoals) return this.scoreGoals - t.scoreGoals;
        return t.scoreGoals - this.lostGoals;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLoose() {
        return loose;
    }

    public void setLoose(int loose) {
        this.loose = loose;
    }

    public int getScoreGoals() {
        return scoreGoals;
    }

    public void setScoreGoals(int scoreGoals) {
        this.scoreGoals = scoreGoals;
    }

    public int getLostGoals() {
        return lostGoals;
    }

    public void setLostGoals(int lostGoals) {
        this.lostGoals = lostGoals;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
