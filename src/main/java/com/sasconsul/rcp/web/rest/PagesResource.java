package com.sasconsul.rcp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sasconsul.rcp.domain.Pages;

import com.sasconsul.rcp.repository.PagesRepository;
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
 * REST controller for managing Pages.
 */
@RestController
@RequestMapping("/api")
public class PagesResource {

    private final Logger log = LoggerFactory.getLogger(PagesResource.class);

    private static final String ENTITY_NAME = "pages";

    private final PagesRepository pagesRepository;

    public PagesResource(PagesRepository pagesRepository) {
        this.pagesRepository = pagesRepository;
    }

    /**
     * POST  /pages : Create a new pages.
     *
     * @param pages the pages to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pages, or with status 400 (Bad Request) if the pages has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pages")
    @Timed
    public ResponseEntity<Pages> createPages(@RequestBody Pages pages) throws URISyntaxException {
        log.debug("REST request to save Pages : {}", pages);
        if (pages.getId() != null) {
            throw new BadRequestAlertException("A new pages cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pages result = pagesRepository.save(pages);
        return ResponseEntity.created(new URI("/api/pages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pages : Updates an existing pages.
     *
     * @param pages the pages to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pages,
     * or with status 400 (Bad Request) if the pages is not valid,
     * or with status 500 (Internal Server Error) if the pages couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pages")
    @Timed
    public ResponseEntity<Pages> updatePages(@RequestBody Pages pages) throws URISyntaxException {
        log.debug("REST request to update Pages : {}", pages);
        if (pages.getId() == null) {
            return createPages(pages);
        }
        Pages result = pagesRepository.save(pages);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pages.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pages : get all the pages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pages in body
     */
    @GetMapping("/pages")
    @Timed
    public List<Pages> getAllPages() {
        log.debug("REST request to get all Pages");
        return pagesRepository.findAll();
        }

    /**
     * GET  /pages/:id : get the "id" pages.
     *
     * @param id the id of the pages to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pages, or with status 404 (Not Found)
     */
    @GetMapping("/pages/{id}")
    @Timed
    public ResponseEntity<Pages> getPages(@PathVariable Long id) {
        log.debug("REST request to get Pages : {}", id);
        Pages pages = pagesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pages));
    }

    /**
     * DELETE  /pages/:id : delete the "id" pages.
     *
     * @param id the id of the pages to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pages/{id}")
    @Timed
    public ResponseEntity<Void> deletePages(@PathVariable Long id) {
        log.debug("REST request to delete Pages : {}", id);
        pagesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
