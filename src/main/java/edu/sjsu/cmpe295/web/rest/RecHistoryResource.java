package edu.sjsu.cmpe295.web.rest;

import com.codahale.metrics.annotation.Timed;
import edu.sjsu.cmpe295.domain.RecHistory;
import edu.sjsu.cmpe295.repository.RecHistoryRepository;
import edu.sjsu.cmpe295.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RecHistory.
 */
@RestController
@RequestMapping("/api")
public class RecHistoryResource {

    private final Logger log = LoggerFactory.getLogger(RecHistoryResource.class);
        
    @Inject
    private RecHistoryRepository recHistoryRepository;
    
    /**
     * POST  /recHistorys -> Create a new recHistory.
     */
    @RequestMapping(value = "/recHistorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RecHistory> createRecHistory(@RequestBody RecHistory recHistory) throws URISyntaxException {
        log.debug("REST request to save RecHistory : {}", recHistory);
        if (recHistory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("recHistory", "idexists", "A new recHistory cannot already have an ID")).body(null);
        }
        RecHistory result = recHistoryRepository.save(recHistory);
        return ResponseEntity.created(new URI("/api/recHistorys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("recHistory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recHistorys -> Updates an existing recHistory.
     */
    @RequestMapping(value = "/recHistorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RecHistory> updateRecHistory(@RequestBody RecHistory recHistory) throws URISyntaxException {
        log.debug("REST request to update RecHistory : {}", recHistory);
        if (recHistory.getId() == null) {
            return createRecHistory(recHistory);
        }
        RecHistory result = recHistoryRepository.save(recHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("recHistory", recHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recHistorys -> get all the recHistorys.
     */
    @RequestMapping(value = "/recHistorys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RecHistory> getAllRecHistorys() {
        log.debug("REST request to get all RecHistorys");
        return recHistoryRepository.findAll();
            }

    /**
     * GET  /recHistorys/:id -> get the "id" recHistory.
     */
    @RequestMapping(value = "/recHistorys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RecHistory> getRecHistory(@PathVariable Long id) {
        log.debug("REST request to get RecHistory : {}", id);
        RecHistory recHistory = recHistoryRepository.findOne(id);
        return Optional.ofNullable(recHistory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /recHistorys/:id -> delete the "id" recHistory.
     */
    @RequestMapping(value = "/recHistorys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRecHistory(@PathVariable Long id) {
        log.debug("REST request to delete RecHistory : {}", id);
        recHistoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("recHistory", id.toString())).build();
    }
}
