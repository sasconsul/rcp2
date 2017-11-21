package com.sasconsul.rcp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sasconsul.rcp.domain.Words;

import com.sasconsul.rcp.repository.WordsRepository;
import com.sasconsul.rcp.web.rest.errors.BadRequestAlertException;
import com.sasconsul.rcp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Words.
 */
@RestController
@RequestMapping("/api")
public class WordsResource {

    private final Logger log = LoggerFactory.getLogger(WordsResource.class);

    private static final String ENTITY_NAME = "words";

    private final WordsRepository wordsRepository;

    public WordsResource(WordsRepository wordsRepository) {
        this.wordsRepository = wordsRepository;
    }

    /**
     * POST  /words : Create a new words.
     *
     * @param words the words to create
     * @return the ResponseEntity with status 201 (Created) and with body the new words, or with status 400 (Bad Request) if the words has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/words")
    @Timed
    public ResponseEntity<Words> createWords(@RequestBody Words words) throws URISyntaxException {
        log.debug("REST request to save Words : {}", words);
        if (words.getId() != null) {
            throw new BadRequestAlertException("A new words cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Words result = wordsRepository.save(words);
        return ResponseEntity.created(new URI("/api/words/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /words : Updates an existing words.
     *
     * @param words the words to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated words,
     * or with status 400 (Bad Request) if the words is not valid,
     * or with status 500 (Internal Server Error) if the words couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/words")
    @Timed
    public ResponseEntity<Words> updateWords(@RequestBody Words words) throws URISyntaxException {
        log.debug("REST request to update Words : {}", words);
        if (words.getId() == null) {
            return createWords(words);
        }
        Words result = wordsRepository.save(words);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, words.getId().toString()))
            .body(result);
    }

    /**
     * GET  /words : get all the words.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of words in body
     */
    @GetMapping("/words")
    @Timed
    public List<Words> getAllWords() {
        log.debug("REST request to get all Words");
        return wordsRepository.findAllWithEagerRelationships();
        }

    /**
     * GET  /words/:id : get the "id" words.
     *
     * @param id the id of the words to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the words, or with status 404 (Not Found)
     */
    @GetMapping("/words/{id}")
    @Timed
    public ResponseEntity<Words> getWords(@PathVariable Long id) {
        log.debug("REST request to get Words : {}", id);
        Words words = wordsRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(words));
    }

    /**
     * DELETE  /words/:id : delete the "id" words.
     *
     * @param id the id of the words to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/words/{id}")
    @Timed
    public ResponseEntity<Void> deleteWords(@PathVariable Long id) {
        log.debug("REST request to delete Words : {}", id);
        wordsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
