package com.springproject.repos;

import com.springproject.model.VenuesInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VenuesInfoRepository extends JpaRepository<VenuesInfo, Long> { }
