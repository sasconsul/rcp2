package com.sasconsul.rcp.web.rest;

import com.sasconsul.rcp.Rcp2App;
import com.sasconsul.rcp.domain.Pages;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
/**
 * Test class for the PageWordCount REST controller.
 *
 * @see PageWordCountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Rcp2App.class)
@WebAppConfiguration
public class PageWordCountResourceIntTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        Charset.forName("utf8"));

    private MockMvc restMockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
            this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.restMockMvc = webAppContextSetup(webApplicationContext).build();

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
        Pages pages = new Pages();
        pages.setUrl("http://www.google.com");
        String pagesJson = json(pages);
        restMockMvc.perform(post("/api/page-word-count/count")
            .contentType(contentType)
            .content(pagesJson))
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

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
            o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
