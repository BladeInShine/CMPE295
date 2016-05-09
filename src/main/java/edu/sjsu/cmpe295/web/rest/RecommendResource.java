package edu.sjsu.cmpe295.web.rest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.sjsu.cmpe295.domain.RecHistory;
import edu.sjsu.cmpe295.repository.RecHistoryRepository;
import edu.sjsu.cmpe295.repository.UserRepository;
import edu.sjsu.cmpe295.security.SecurityUtils;
import edu.sjsu.cmpe295.service.RecommendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by BladeInShine on 16/4/17.
 */
@RestController
@RequestMapping("/api")
public class RecommendResource {

    private final Logger log = LoggerFactory.getLogger(RecommendResource.class);

    @Inject
    private RecommendService recommendService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private RecHistoryRepository recHistoryRepository;

    @RequestMapping(value = "/recommend", method = RequestMethod.GET)
    public ResponseEntity<String> recommend(){
        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                double calorie = recommendService.calorieCalculator(u);
                int min = (int) (calorie*1.2/2.5);
                int max = (int) (calorie*1.375/2.5);
                String recommend = null;
                try {
                    recommend = recommendService.fetchNutritionixByCalory(min, max);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new ResponseEntity<String>(recommend, HttpStatus.OK);
            })
            .orElse(new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/recommend/search", method = RequestMethod.GET)
    public ResponseEntity<String> recommendQuery(@RequestParam(value="query")String query){
        return userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                double calorie = recommendService.calorieCalculator(u);
                int min = (int) (calorie*1.2/2.5);
                int max = (int) (calorie*1.375/2.5);
                String recommend = null;
                try {
                    recommend = recommendService.fetchNutritionixByQuery(min, max, query);
                    JsonObject jsonObject = (new JsonParser()).parse(recommend).getAsJsonObject();
                    List<RecHistory> results = recommendService.fetchNutritionixResult(jsonObject.get("hits").getAsJsonArray(), 5);
                    recHistoryRepository.save(results);
                    return new ResponseEntity<String>("history saved", HttpStatus.OK);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<String>("bad request", HttpStatus.BAD_REQUEST);
                }
                //return new ResponseEntity<String>(recommend, HttpStatus.OK);
            })
            .orElse(new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/mahout/test", method = RequestMethod.GET)
    public void test() throws Exception {
        recommendService.fetchBrandFromMahout(1);
    }

}
