package com.sasconsul.rcp.web.rest;

import com.sasconsul.rcp.Rcp2App;

import com.sasconsul.rcp.domain.Words;
import com.sasconsul.rcp.repository.WordsRepository;
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
 * Test class for the WordsResource REST controller.
 *
 * @see WordsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Rcp2App.class)
public class WordsResourceIntTest {

    private static final String DEFAULT_WORD = "AAAAAAAAAA";
    private static final String UPDATED_WORD = "BBBBBBBBBB";

    private static final Long DEFAULT_COUNT = 1L;
    private static final Long UPDATED_COUNT = 2L;

    private static final Long DEFAULT_PAGE = 1L;
    private static final Long UPDATED_PAGE = 2L;

    @Autowired
    private WordsRepository wordsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWordsMockMvc;

    private Words words;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WordsResource wordsResource = new WordsResource(wordsRepository);
        this.restWordsMockMvc = MockMvcBuilders.standaloneSetup(wordsResource)
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
    public static Words createEntity(EntityManager em) {
        Words words = new Words()
            .word(DEFAULT_WORD)
            .count(DEFAULT_COUNT)
            .page(DEFAULT_PAGE);
        return words;
    }

    @Before
    public void initTest() {
        words = createEntity(em);
    }

    @Test
    @Transactional
    public void createWords() throws Exception {
        int databaseSizeBeforeCreate = wordsRepository.findAll().size();

        // Create the Words
        restWordsMockMvc.perform(post("/api/words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(words)))
            .andExpect(status().isCreated());

        // Validate the Words in the database
        List<Words> wordsList = wordsRepository.findAll();
        assertThat(wordsList).hasSize(databaseSizeBeforeCreate + 1);
        Words testWords = wordsList.get(wordsList.size() - 1);
        assertThat(testWords.getWord()).isEqualTo(DEFAULT_WORD);
        assertThat(testWords.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testWords.getPage()).isEqualTo(DEFAULT_PAGE);
    }

    @Test
    @Transactional
    public void createWordsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wordsRepository.findAll().size();

        // Create the Words with an existing ID
        words.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWordsMockMvc.perform(post("/api/words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(words)))
            .andExpect(status().isBadRequest());

        // Validate the Words in the database
        List<Words> wordsList = wordsRepository.findAll();
        assertThat(wordsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWords() throws Exception {
        // Initialize the database
        wordsRepository.saveAndFlush(words);

        // Get all the wordsList
        restWordsMockMvc.perform(get("/api/words?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(words.getId().intValue())))
            .andExpect(jsonPath("$.[*].word").value(hasItem(DEFAULT_WORD.toString())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].page").value(hasItem(DEFAULT_PAGE.intValue())));
    }

    @Test
    @Transactional
    public void getWords() throws Exception {
        // Initialize the database
        wordsRepository.saveAndFlush(words);

        // Get the words
        restWordsMockMvc.perform(get("/api/words/{id}", words.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(words.getId().intValue()))
            .andExpect(jsonPath("$.word").value(DEFAULT_WORD.toString()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT.intValue()))
            .andExpect(jsonPath("$.page").value(DEFAULT_PAGE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWords() throws Exception {
        // Get the words
        restWordsMockMvc.perform(get("/api/words/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWords() throws Exception {
        // Initialize the database
        wordsRepository.saveAndFlush(words);
        int databaseSizeBeforeUpdate = wordsRepository.findAll().size();

        // Update the words
        Words updatedWords = wordsRepository.findOne(words.getId());
        updatedWords
            .word(UPDATED_WORD)
            .count(UPDATED_COUNT)
            .page(UPDATED_PAGE);

        restWordsMockMvc.perform(put("/api/words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWords)))
            .andExpect(status().isOk());

        // Validate the Words in the database
        List<Words> wordsList = wordsRepository.findAll();
        assertThat(wordsList).hasSize(databaseSizeBeforeUpdate);
        Words testWords = wordsList.get(wordsList.size() - 1);
        assertThat(testWords.getWord()).isEqualTo(UPDATED_WORD);
        assertThat(testWords.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testWords.getPage()).isEqualTo(UPDATED_PAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingWords() throws Exception {
        int databaseSizeBeforeUpdate = wordsRepository.findAll().size();

        // Create the Words

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWordsMockMvc.perform(put("/api/words")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(words)))
            .andExpect(status().isCreated());

        // Validate the Words in the database
        List<Words> wordsList = wordsRepository.findAll();
        assertThat(wordsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWords() throws Exception {
        // Initialize the database
        wordsRepository.saveAndFlush(words);
        int databaseSizeBeforeDelete = wordsRepository.findAll().size();

        // Get the words
        restWordsMockMvc.perform(delete("/api/words/{id}", words.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Words> wordsList = wordsRepository.findAll();
        assertThat(wordsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Words.class);
        Words words1 = new Words();
        words1.setId(1L);
        Words words2 = new Words();
        words2.setId(words1.getId());
        assertThat(words1).isEqualTo(words2);
        words2.setId(2L);
        assertThat(words1).isNotEqualTo(words2);
        words1.setId(null);
        assertThat(words1).isNotEqualTo(words2);
    }
}
