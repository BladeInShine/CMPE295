package edu.sjsu.cmpe295.web.rest;

import edu.sjsu.cmpe295.Application;
import edu.sjsu.cmpe295.domain.RecHistory;
import edu.sjsu.cmpe295.repository.RecHistoryRepository;

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
 * Test class for the RecHistoryResource REST controller.
 *
 * @see RecHistoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RecHistoryResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_FOOD_ID = "AAAAA";
    private static final String UPDATED_FOOD_ID = "BBBBB";
    private static final String DEFAULT_FOOD_NAME = "AAAAA";
    private static final String UPDATED_FOOD_NAME = "BBBBB";
    private static final String DEFAULT_BRAND_ID = "AAAAA";
    private static final String UPDATED_BRAND_ID = "BBBBB";
    private static final String DEFAULT_BRAND_NAME = "AAAAA";
    private static final String UPDATED_BRAND_NAME = "BBBBB";

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TIMESTAMP_STR = dateTimeFormatter.format(DEFAULT_TIMESTAMP);

    @Inject
    private RecHistoryRepository recHistoryRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRecHistoryMockMvc;

    private RecHistory recHistory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RecHistoryResource recHistoryResource = new RecHistoryResource();
        ReflectionTestUtils.setField(recHistoryResource, "recHistoryRepository", recHistoryRepository);
        this.restRecHistoryMockMvc = MockMvcBuilders.standaloneSetup(recHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        recHistory = new RecHistory();
        recHistory.setFoodId(DEFAULT_FOOD_ID);
        recHistory.setFoodName(DEFAULT_FOOD_NAME);
        recHistory.setBrandId(DEFAULT_BRAND_ID);
        recHistory.setBrandName(DEFAULT_BRAND_NAME);
        recHistory.setTimestamp(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void createRecHistory() throws Exception {
        int databaseSizeBeforeCreate = recHistoryRepository.findAll().size();

        // Create the RecHistory

        restRecHistoryMockMvc.perform(post("/api/recHistorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recHistory)))
                .andExpect(status().isCreated());

        // Validate the RecHistory in the database
        List<RecHistory> recHistorys = recHistoryRepository.findAll();
        assertThat(recHistorys).hasSize(databaseSizeBeforeCreate + 1);
        RecHistory testRecHistory = recHistorys.get(recHistorys.size() - 1);
        assertThat(testRecHistory.getFoodId()).isEqualTo(DEFAULT_FOOD_ID);
        assertThat(testRecHistory.getFoodName()).isEqualTo(DEFAULT_FOOD_NAME);
        assertThat(testRecHistory.getBrandId()).isEqualTo(DEFAULT_BRAND_ID);
        assertThat(testRecHistory.getBrandName()).isEqualTo(DEFAULT_BRAND_NAME);
        assertThat(testRecHistory.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllRecHistorys() throws Exception {
        // Initialize the database
        recHistoryRepository.saveAndFlush(recHistory);

        // Get all the recHistorys
        restRecHistoryMockMvc.perform(get("/api/recHistorys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(recHistory.getId().intValue())))
                .andExpect(jsonPath("$.[*].foodId").value(hasItem(DEFAULT_FOOD_ID.toString())))
                .andExpect(jsonPath("$.[*].foodName").value(hasItem(DEFAULT_FOOD_NAME.toString())))
                .andExpect(jsonPath("$.[*].brandId").value(hasItem(DEFAULT_BRAND_ID.toString())))
                .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME.toString())))
                .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP_STR)));
    }

    @Test
    @Transactional
    public void getRecHistory() throws Exception {
        // Initialize the database
        recHistoryRepository.saveAndFlush(recHistory);

        // Get the recHistory
        restRecHistoryMockMvc.perform(get("/api/recHistorys/{id}", recHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(recHistory.getId().intValue()))
            .andExpect(jsonPath("$.foodId").value(DEFAULT_FOOD_ID.toString()))
            .andExpect(jsonPath("$.foodName").value(DEFAULT_FOOD_NAME.toString()))
            .andExpect(jsonPath("$.brandId").value(DEFAULT_BRAND_ID.toString()))
            .andExpect(jsonPath("$.brandName").value(DEFAULT_BRAND_NAME.toString()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP_STR));
    }

    @Test
    @Transactional
    public void getNonExistingRecHistory() throws Exception {
        // Get the recHistory
        restRecHistoryMockMvc.perform(get("/api/recHistorys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecHistory() throws Exception {
        // Initialize the database
        recHistoryRepository.saveAndFlush(recHistory);

		int databaseSizeBeforeUpdate = recHistoryRepository.findAll().size();

        // Update the recHistory
        recHistory.setFoodId(UPDATED_FOOD_ID);
        recHistory.setFoodName(UPDATED_FOOD_NAME);
        recHistory.setBrandId(UPDATED_BRAND_ID);
        recHistory.setBrandName(UPDATED_BRAND_NAME);
        recHistory.setTimestamp(UPDATED_TIMESTAMP);

        restRecHistoryMockMvc.perform(put("/api/recHistorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recHistory)))
                .andExpect(status().isOk());

        // Validate the RecHistory in the database
        List<RecHistory> recHistorys = recHistoryRepository.findAll();
        assertThat(recHistorys).hasSize(databaseSizeBeforeUpdate);
        RecHistory testRecHistory = recHistorys.get(recHistorys.size() - 1);
        assertThat(testRecHistory.getFoodId()).isEqualTo(UPDATED_FOOD_ID);
        assertThat(testRecHistory.getFoodName()).isEqualTo(UPDATED_FOOD_NAME);
        assertThat(testRecHistory.getBrandId()).isEqualTo(UPDATED_BRAND_ID);
        assertThat(testRecHistory.getBrandName()).isEqualTo(UPDATED_BRAND_NAME);
        assertThat(testRecHistory.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void deleteRecHistory() throws Exception {
        // Initialize the database
        recHistoryRepository.saveAndFlush(recHistory);

		int databaseSizeBeforeDelete = recHistoryRepository.findAll().size();

        // Get the recHistory
        restRecHistoryMockMvc.perform(delete("/api/recHistorys/{id}", recHistory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RecHistory> recHistorys = recHistoryRepository.findAll();
        assertThat(recHistorys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
