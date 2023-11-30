package com.springproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
@Entity
public class Events {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @Column(name = "fixture_id", nullable = false)
    private Long fixtureId;
    @Column(name = "elapsed_time")
    private Long elapsedTime;
    @Column(name = "extra_time")
    private String extraTime;
    @Column(name = "team_id")
    private String teamId;
    @Column(name = "team_name")
    private String teamName;
    @Column(name = "team_logo")
    private String teamLogo;
    @Column(name = "player_id")
    private String playerId;
    @Column(name = "player_name")
    private String playerName;
    @Column(name = "assist_id")
    private String assistId;
    @Column(name = "assist_name")
    private String assistName;
    @Column(name = "type")
    private String type;
    @Column(name = "detail")
    private String detail;
    @Column(name = "comments")
    private String comments;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
