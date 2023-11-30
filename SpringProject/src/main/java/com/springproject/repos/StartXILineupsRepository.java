package com.springproject.repos;

import com.springproject.model.StartXILineups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StartXILineupsRepository extends JpaRepository<StartXILineups, Long> {
    List<StartXILineups> findByFixtureIdAndTeamId(Long fixtureId, Long teamId);
}
