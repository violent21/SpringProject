package com.springproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teams_statistics")
@Entity
public class TeamsStatistics {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @Column(name = "fixtureId", nullable = false)
    private Long fixtureId;
    @Column(name = "teamId", nullable = false)
    private Long teamId;
    @Column(name = "teamName", nullable = false)
    private String teamName;
    @Column(name = "ShotsOnGoal")
    private String shotsOnGoal;
    @Column(name = "ShotsOffGoal")
    private String shotsOffGoal;
    @Column(name = "TotalShots")
    private String totalShots;
    @Column(name = "BlockedShots")
    private String blockedShots;
    @Column(name = "ShotsInsidebox")
    private String shotsInsidebox;
    @Column(name = "ShotsOutsidebox")
    private String shotsOutsidebox;
    @Column(name = "Fouls")
    private String fouls;
    @Column(name = "CornerKicks")
    private String cornerKicks;
    @Column(name = "Offsides")
    private String offsides;
    @Column(name = "BallPossession")
    private String ballPossession;
    @Column(name = "YellowCards")
    private String yellowCards;
    @Column(name = "RedCards")
    private String redCards;
    @Column(name = "GoalkeeperSaves")
    private String goalkeeperSaves;
    @Column(name = "TotalPasses")
    private String totalPasses;
    @Column(name = "PassesAccurate")
    private String passesAccurate;
    @Column(name = "PassesPrcnt")
    private String passesPrcnt;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
