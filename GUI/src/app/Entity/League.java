package app.Entity;

import java.util.ArrayList;
import java.util.List;

public class League {
    private Integer league_id;

    private List<Team> team = new ArrayList<>();
    private String league_name;
    private Country country;

    public Integer getLeague_id() {
        return league_id;
    }

    public void setLeague_id(Integer league_id) {
        this.league_id = league_id;
    }

    public String getLeague_name() {
        return league_name;
    }

    public void setLeague_name(String league_name) {
        this.league_name = league_name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

}
