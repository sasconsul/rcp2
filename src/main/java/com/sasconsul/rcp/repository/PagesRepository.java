package com.sasconsul.rcp.repository;

import com.sasconsul.rcp.domain.Pages;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Pages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PagesRepository extends JpaRepository<Pages, Long> {

}
