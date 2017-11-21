package com.sasconsul.rcp.web.rest;

import com.sasconsul.rcp.Rcp2App;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Test class for the PageWordCount REST controller.
 *
 * @see PageWordCountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Rcp2App.class)
public class PageWordCountResourceIntTest {

    private MockMvc restMockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        PageWordCountResource pageWordCountResource = new PageWordCountResource();
        restMockMvc = MockMvcBuilders
            .standaloneSetup(pageWordCountResource)
            .build();
    }

    /**
    * Test count
    */
    @Test
    public void testCount() throws Exception {
        restMockMvc.perform(post("/api/page-word-count/count"))
            .andExpect(status().isOk());
    }
    /**
    * Test histogram
    */
    @Test
    public void testHistogram() throws Exception {
        restMockMvc.perform(get("/api/page-word-count/histogram"))
            .andExpect(status().isOk());
    }

}
