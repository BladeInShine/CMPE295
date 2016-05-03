package edu.sjsu.cmpe295.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.sjsu.cmpe295.domain.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

/**
 * Created by BladeInShine on 16/4/17.
 */
@Service
public class RecommendService {

    private static final String NUTRITIONIX_APP_ID = "4cfcb19a";
    private static final String NUTRITIONIX_APP_PWD = "3df8811bc08567ce00dc8c67b8de0327";
    private static final String NUTRITIONIX_URL = "https://api.nutritionix.com/v1_1/search";

    @Inject
    private RestTemplate restTemplate;

    public double calorieCalculator(User user){
        double base;
        if(user.getGender().equals("M")){
            base = 66+13.7*user.getWeight()+5*user.getHeight()-6.8*user.getAge();
        }
        else if(user.getGender().equals("F")){
            base = 655+9.6*user.getWeight()+1.8*user.getHeight()-4.7*user.getAge();
        }
        else{
            return -1;
        }

        return base;
    }

    public String fetchNutritionixByCalory(int min, int max) throws Exception{

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("appId", NUTRITIONIX_APP_ID);
        jsonObject.addProperty("appKey", NUTRITIONIX_APP_PWD);
        JsonArray fields = new JsonArray();
        fields.add("item_name");
        fields.add("brand_name");
        fields.add("nf_calories");
        jsonObject.add("fields", fields);
        JsonObject filters = new JsonObject();
        JsonObject nfCalories = new JsonObject();
        nfCalories.addProperty("from", min);
        nfCalories.addProperty("to", max);
        filters.addProperty("item_type",1);
        filters.add("nf_calories", nfCalories);
        jsonObject.add("filters",filters);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);
        ResponseEntity<String> result = restTemplate.postForEntity(NUTRITIONIX_URL, entity, String.class);
        return result.getBody();

    }

}
