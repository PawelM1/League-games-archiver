package app.Entity;

public class Team {
    private Integer team_id;
    private String name;
    private String city;

    public Integer getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Integer id_adres) {
        this.team_id = id_adres;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
