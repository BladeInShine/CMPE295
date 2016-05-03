package edu.sjsu.cmpe295.web.rest;

import edu.sjsu.cmpe295.repository.UserRepository;
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

    @RequestMapping(value = "/recommend", method = RequestMethod.GET)
    public ResponseEntity<String> recommend(@RequestParam(name = "username") String username){
        return userRepository.findOneByLogin(username)
            .map(u -> {
                double calory = recommendService.calorieCalculator(u);
                int min = (int) (calory*1.2/2.5);
                int max = (int) (calory*1.375/2.5);
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

}
