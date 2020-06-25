package app.Entity;

import java.time.LocalDate;

public class Season {
    private Integer season_id;
    private LocalDate season_start;
    private LocalDate season_end;

    public Integer getSeason_id() {
        return season_id;
    }

    public void setSeason_id(Integer season_id) {
        this.season_id = season_id;
    }

    public LocalDate getSeason_start() {
        return season_start;
    }

    public void setSeason_start(LocalDate season_start) {
        this.season_start = season_start;
    }

    public LocalDate getSeason_end() {
        return season_end;
    }

    public void setSeason_end(LocalDate season_end) {
        this.season_end = season_end;
    }

}
