package app.Other;

import app.Entity.Country;
import app.Entity.League;
import app.Entity.Season;
import app.Entity.Team;
import javafx.util.StringConverter;

public class DBRecordToStringConverter {
    private StringConverter<Country> countryStringConverter;
    private StringConverter<Season> seasonStringConverter;
    private StringConverter<League> leagueStringConverter;
    private StringConverter<Team> teamStringConverter;

    public DBRecordToStringConverter() {
        countryStringConverter = new StringConverter<>() {
            @Override
            public String toString(Country country) {
                return country.getName();
            }

            @Override
            public Country fromString(String s) {
                return null;
            }
        };

        seasonStringConverter = new StringConverter<>() {
            @Override
            public String toString(Season season) {
                return season.getSeason_start().toString() + " / " + season.getSeason_end().toString();
            }

            @Override
            public Season fromString(String s) {
                return null;
            }
        };

        leagueStringConverter = new StringConverter<>() {
            @Override
            public String toString(League league) {
                return league.getLeague_name();
            }

            @Override
            public League fromString(String s) {
                return null;
            }
        };

        teamStringConverter = new StringConverter<>() {
            @Override
            public String toString(Team team) {
                return team.getName();
            }

            @Override
            public Team fromString(String s) {
                return null;
            }
        };
    }

    public StringConverter<Country> getCountryStringConverter() {
        return countryStringConverter;
    }

    public StringConverter<Season> getSeasonStringConverter() {
        return seasonStringConverter;
    }

    public StringConverter<League> getLeagueStringConverter() {
        return leagueStringConverter;
    }

    public StringConverter<Team> getTeamStringConverter() {
        return teamStringConverter;
    }
}
