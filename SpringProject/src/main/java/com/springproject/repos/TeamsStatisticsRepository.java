package com.springproject.repos;

import com.springproject.model.TeamsStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamsStatisticsRepository extends JpaRepository<TeamsStatistics, Long> {
    List<TeamsStatistics> findByFixtureId(Long fixtureId);
}
