package com.springproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "head_to_head")
@Entity
public class HeadToHead {
    @jakarta.persistence.Id
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @Column(name = "teamsId", nullable = false)
    private String teamsId;
    @Column(name = "date", nullable = false)
    private String date;
    @Column(name = "home_team_name", nullable = false)
    private String homeTeamName;
    @Column(name = "home_team_logo")
    private String homeTeamLogo;
    @Column(name = "home_goals")
    private int homeGoals;
    @Column(name = "away_team_name", nullable = false)
    private String awayTeamName;
    @Column(name = "away_team_logo")
    private String awayTeamLogo;
    @Column(name = "away_goals")
    private int awayGoals;
    @Column(name = "venue_name")
    private String venueName;
    @Column(name = "venue_city")
    private String venueCity;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    private String formattedDate;

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }
}
