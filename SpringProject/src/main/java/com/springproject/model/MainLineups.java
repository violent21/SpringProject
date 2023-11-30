package com.springproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "main_lineups")
@Entity
public class MainLineups {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "fixture_id", nullable = false)
    private Long fixtureId;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Column(name = "player_primary_color")
    private String playerPrimaryColor;

    @Column(name = "player_number_color")
    private String playerNumberColor;

    @Column(name = "player_border_color")
    private String playerBorderColor;

    @Column(name = "goalkeeper_primary_color")
    private String goalkeeperPrimaryColor;

    @Column(name = "goalkeeper_number_color")
    private String goalkeeperNumberColor;

    @Column(name = "goalkeeper_border_color")
    private String goalkeeperBorderColor;

    @Column(name = "coach_id")
    private Long coachId;

    @Column(name = "coach_name")
    private String coachName;

    @Column(name = "coach_photo")
    private String coachPhoto;

    @Column(name = "formation")
    private String formation;
}
