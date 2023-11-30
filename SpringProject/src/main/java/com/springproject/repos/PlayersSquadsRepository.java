package com.springproject.repos;

import com.springproject.model.PlayersSquads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayersSquadsRepository extends JpaRepository<PlayersSquads, Long> {
    List<PlayersSquads> findByTeamId(Long teamId);
}
