package com.springproject.repos;

import com.springproject.model.MainLineups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainLineupsRepository extends JpaRepository<MainLineups, Long> {
    List<MainLineups> findByFixtureId(Long fixtureId);
    List<MainLineups> findByFixtureIdAndTeamId(Long fixtureId, Long teamId);
}
