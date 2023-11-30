package com.springproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "players_squads")
@Entity
public class PlayersSquads {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @Column(name = "team_id", nullable = false)
    private Long teamId;
    @Column(name = "team_name", nullable = false)
    private String teamName;
    @Column(name = "team_logo")
    private String teamLogo;
    @Column(name = "player_id")
    private String playerId;
    @Column(name = "player_name")
    private String playerName;
    @Column(name = "player_age")
    private String playerAge;
    @Column(name = "player_number")
    private String playerNumber;
    @Column(name = "player_position")
    private String playerPosition;
    @Column(name = "player_photo")
    private String playerPhoto;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
