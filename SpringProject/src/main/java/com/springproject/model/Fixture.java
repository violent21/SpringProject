package com.springproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "matches")
@Entity
public class Fixture {
    @jakarta.persistence.Id
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @Column(name = "date", nullable = false)
    private String date;
    @Column(name = "home_team_name", nullable = false)
    private String homeTeamName;
    @Column(name = "home_team_id", nullable = false)
    private String homeTeamId;
    @Column(name = "away_team_name", nullable = false)
    private String awayTeamName;
    @Column(name = "away_team_id", nullable = false)
    private String awayTeamId;
    @Column(name = "referee")
    private String referee;
    @Column(name = "venue_id")
    private String venueId;
    @Column(name = "venue_name")
    private String venueName;
    @Column(name = "venue_city")
    private String venueCity;
    @Column(name = "status")
    private String status;
    @Column(name = "league_name")
    private String leagueName;
    @Column(name = "league_country")
    private String leagueCountry;
    @Column(name = "league_logo")
    private String leagueLogo;
    @Column(name = "league_flag")
    private String leagueFlag;
    @Column(name = "home_team_logo")
    private String homeTeamLogo;
    @Column(name = "home_team_winner")
    private String homeTeamWinner;
    @Column(name = "away_team_logo")
    private String awayTeamLogo;
    @Column(name = "away_team_winner")
    private String awayTeamWinner;
    @Column(name = "home_goals")
    private int homeGoals;
    @Column(name = "away_goals")
    private int awayGoals;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

//    private OffsetDateTime parsedDate;
//
//    public OffsetDateTime getParsedDate() {
//        return parsedDate;
//    }

//    public void setParsedDate(OffsetDateTime parsedDate) {
//        this.parsedDate = parsedDate;
//    }
    private String formattedDate;

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }
}

//package com.springproject.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "matches")
//@Entity
//public class Fixture {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false, unique = true)
//    private Long id;
//
//    @Column(name = "date", nullable = false)
//    private String date;
//
//    @Column(name = "home_team_name", nullable = false)
//    private String homeTeamName;
//
//    @Column(name = "away_team_name", nullable = false)
//    private String awayTeamName;
//
//    @Column(name = "referee")
//    private String referee;
//
//    @Column(name = "venue_name")
//    private String venueName;
//
//    @Column(name = "venue_city")
//    private String venueCity;
//
//    @Column(name = "status")
//    private String status;
//
//    @Column(name = "league_name")
//    private String leagueName;
//
//    @Column(name = "league_country")
//    private String leagueCountry;
//
//    @Column(name = "league_logo")
//    private String leagueLogo;
//
//    @Column(name = "league_flag")
//    private String leagueFlag;
//
//    @Column(name = "home_team_logo")
//    private String homeTeamLogo;
//
//    @Column(name = "home_team_winner")
//    private boolean homeTeamWinner;
//
//    @Column(name = "away_team_logo")
//    private String awayTeamLogo;
//
//    @Column(name = "away_team_winner")
//    private boolean awayTeamWinner;
//
//    @Column(name = "home_goals")
//    private int homeGoals;
//
//    @Column(name = "away_goals")
//    private int awayGoals;
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getId() {
//        return id;
//    }
//}
