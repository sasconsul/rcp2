package com.sasconsul.rcp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sasconsul.rcp.domain.Pages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * PageWordCount controller
 */
@RestController
@RequestMapping("/api/page-word-count")
public class PageWordCountResource {

    private final Logger log = LoggerFactory.getLogger(PageWordCountResource.class);

    /**
    * POST count word count for the url parameter
     *
     * query parameter
     * The body is a HTML encoded URL for the page to be counted.
    */
    @Timed
    @PostMapping("/count")
    public Pages count(@RequestBody Pages pages) {



        return pages;
    }

    /**
    * GET histogram
    */
    @GetMapping("/histogram")
    public String histogram() {
        return "histogram";
    }

}
