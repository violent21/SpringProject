package com.springproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "startXI_lineups")
@Entity
public class StartXILineups {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "fixture_id", nullable = false)
    private Long fixtureId;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "player_name", nullable = false)
    private String playerName;

    @Column(name = "player_number", nullable = false)
    private String playerNumber;

    @Column(name = "player_pos", nullable = false)
    private String playerPos;

    @Column(name = "player_grid", nullable = false)
    private String playerGrid;

}
