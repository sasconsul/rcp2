package com.sasconsul.rcp.web.rest;

import com.sasconsul.rcp.Rcp2App;

import com.sasconsul.rcp.domain.Pages;
import com.sasconsul.rcp.repository.PagesRepository;
import com.sasconsul.rcp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sasconsul.rcp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PagesResource REST controller.
 *
 * @see PagesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Rcp2App.class)
public class PagesResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private PagesRepository pagesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPagesMockMvc;

    private Pages pages;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PagesResource pagesResource = new PagesResource(pagesRepository);
        this.restPagesMockMvc = MockMvcBuilders.standaloneSetup(pagesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pages createEntity(EntityManager em) {
        Pages pages = new Pages()
            .url(DEFAULT_URL);
        return pages;
    }

    @Before
    public void initTest() {
        pages = createEntity(em);
    }

    @Test
    @Transactional
    public void createPages() throws Exception {
        int databaseSizeBeforeCreate = pagesRepository.findAll().size();

        // Create the Pages
        restPagesMockMvc.perform(post("/api/pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pages)))
            .andExpect(status().isCreated());

        // Validate the Pages in the database
        List<Pages> pagesList = pagesRepository.findAll();
        assertThat(pagesList).hasSize(databaseSizeBeforeCreate + 1);
        Pages testPages = pagesList.get(pagesList.size() - 1);
        assertThat(testPages.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createPagesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pagesRepository.findAll().size();

        // Create the Pages with an existing ID
        pages.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPagesMockMvc.perform(post("/api/pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pages)))
            .andExpect(status().isBadRequest());

        // Validate the Pages in the database
        List<Pages> pagesList = pagesRepository.findAll();
        assertThat(pagesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPages() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get all the pagesList
        restPagesMockMvc.perform(get("/api/pages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pages.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void getPages() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);

        // Get the pages
        restPagesMockMvc.perform(get("/api/pages/{id}", pages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pages.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPages() throws Exception {
        // Get the pages
        restPagesMockMvc.perform(get("/api/pages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePages() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);
        int databaseSizeBeforeUpdate = pagesRepository.findAll().size();

        // Update the pages
        Pages updatedPages = pagesRepository.findOne(pages.getId());
        updatedPages
            .url(UPDATED_URL);

        restPagesMockMvc.perform(put("/api/pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPages)))
            .andExpect(status().isOk());

        // Validate the Pages in the database
        List<Pages> pagesList = pagesRepository.findAll();
        assertThat(pagesList).hasSize(databaseSizeBeforeUpdate);
        Pages testPages = pagesList.get(pagesList.size() - 1);
        assertThat(testPages.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingPages() throws Exception {
        int databaseSizeBeforeUpdate = pagesRepository.findAll().size();

        // Create the Pages

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPagesMockMvc.perform(put("/api/pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pages)))
            .andExpect(status().isCreated());

        // Validate the Pages in the database
        List<Pages> pagesList = pagesRepository.findAll();
        assertThat(pagesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePages() throws Exception {
        // Initialize the database
        pagesRepository.saveAndFlush(pages);
        int databaseSizeBeforeDelete = pagesRepository.findAll().size();

        // Get the pages
        restPagesMockMvc.perform(delete("/api/pages/{id}", pages.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Pages> pagesList = pagesRepository.findAll();
        assertThat(pagesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pages.class);
        Pages pages1 = new Pages();
        pages1.setId(1L);
        Pages pages2 = new Pages();
        pages2.setId(pages1.getId());
        assertThat(pages1).isEqualTo(pages2);
        pages2.setId(2L);
        assertThat(pages1).isNotEqualTo(pages2);
        pages1.setId(null);
        assertThat(pages1).isNotEqualTo(pages2);
    }
}
