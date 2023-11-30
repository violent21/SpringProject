package com.springproject.repos;

import com.springproject.model.HeadToHead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeadToHeadRepository extends JpaRepository<HeadToHead, String> {
    List<HeadToHead> findByTeamsId(String teamsId);
}
