package edu.sjsu.cmpe295.web.rest;

import edu.sjsu.cmpe295.Application;
import edu.sjsu.cmpe295.domain.History;
import edu.sjsu.cmpe295.repository.HistoryRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the HistoryResource REST controller.
 *
 * @see HistoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HistoryResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_IMAGE = "AAAAA";
    private static final String UPDATED_IMAGE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_CALORIE = 1;
    private static final Integer UPDATED_CALORIE = 2;

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIME_STR = dateTimeFormatter.format(DEFAULT_TIME);

    @Inject
    private HistoryRepository historyRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHistoryMockMvc;

    private History history;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HistoryResource historyResource = new HistoryResource();
        ReflectionTestUtils.setField(historyResource, "historyRepository", historyRepository);
        this.restHistoryMockMvc = MockMvcBuilders.standaloneSetup(historyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        history = new History();
        history.setImage(DEFAULT_IMAGE);
        history.setDescription(DEFAULT_DESCRIPTION);
        history.setCalorie(DEFAULT_CALORIE);
        history.setTime(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void createHistory() throws Exception {
        int databaseSizeBeforeCreate = historyRepository.findAll().size();

        // Create the History

        restHistoryMockMvc.perform(post("/api/historys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(history)))
                .andExpect(status().isCreated());

        // Validate the History in the database
        List<History> historys = historyRepository.findAll();
        assertThat(historys).hasSize(databaseSizeBeforeCreate + 1);
        History testHistory = historys.get(historys.size() - 1);
        assertThat(testHistory.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testHistory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testHistory.getCalorie()).isEqualTo(DEFAULT_CALORIE);
        assertThat(testHistory.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void getAllHistorys() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get all the historys
        restHistoryMockMvc.perform(get("/api/historys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(history.getId().intValue())))
                .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].calorie").value(hasItem(DEFAULT_CALORIE)))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME_STR)));
    }

    @Test
    @Transactional
    public void getHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

        // Get the history
        restHistoryMockMvc.perform(get("/api/historys/{id}", history.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(history.getId().intValue()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.calorie").value(DEFAULT_CALORIE))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingHistory() throws Exception {
        // Get the history
        restHistoryMockMvc.perform(get("/api/historys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

		int databaseSizeBeforeUpdate = historyRepository.findAll().size();

        // Update the history
        history.setImage(UPDATED_IMAGE);
        history.setDescription(UPDATED_DESCRIPTION);
        history.setCalorie(UPDATED_CALORIE);
        history.setTime(UPDATED_TIME);

        restHistoryMockMvc.perform(put("/api/historys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(history)))
                .andExpect(status().isOk());

        // Validate the History in the database
        List<History> historys = historyRepository.findAll();
        assertThat(historys).hasSize(databaseSizeBeforeUpdate);
        History testHistory = historys.get(historys.size() - 1);
        assertThat(testHistory.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testHistory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHistory.getCalorie()).isEqualTo(UPDATED_CALORIE);
        assertThat(testHistory.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    public void deleteHistory() throws Exception {
        // Initialize the database
        historyRepository.saveAndFlush(history);

		int databaseSizeBeforeDelete = historyRepository.findAll().size();

        // Get the history
        restHistoryMockMvc.perform(delete("/api/historys/{id}", history.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<History> historys = historyRepository.findAll();
        assertThat(historys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
