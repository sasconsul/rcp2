package com.sasconsul.rcp.repository;

import com.sasconsul.rcp.domain.Words;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Words entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WordsRepository extends JpaRepository<Words, Long> {
    @Query("select distinct words from Words words left join fetch words.pages")
    List<Words> findAllWithEagerRelationships();

    @Query("select words from Words words left join fetch words.pages where words.id =:id")
    Words findOneWithEagerRelationships(@Param("id") Long id);

}
