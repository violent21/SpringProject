package com.springproject.repos;

import com.springproject.model.SubstitutesLineups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubstitutesLineupsRepository extends JpaRepository<SubstitutesLineups, Long> {
    List<SubstitutesLineups> findByFixtureIdAndTeamId(Long fixtureId, Long teamId);
}
