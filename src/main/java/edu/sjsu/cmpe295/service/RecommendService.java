package edu.sjsu.cmpe295.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.sjsu.cmpe295.domain.RecHistory;
import edu.sjsu.cmpe295.domain.User;
import edu.sjsu.cmpe295.repository.RecHistoryRepository;
import edu.sjsu.cmpe295.repository.UserRepository;
import edu.sjsu.cmpe295.security.SecurityUtils;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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

    @Inject
    private DataSource dataSource;

    @Inject
    private RecHistoryRepository recHistoryRepository;

    @Inject
    private UserRepository userRepository;

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
        fields.add("item_id");
        fields.add("brand_id");
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

    public String fetchNutritionixByQuery(int min, int max, String query) throws Exception{

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("appId", NUTRITIONIX_APP_ID);
        jsonObject.addProperty("appKey", NUTRITIONIX_APP_PWD);
        JsonArray fields = new JsonArray();
        fields.add("item_name");
        fields.add("brand_name");
        fields.add("item_id");
        fields.add("brand_id");
        fields.add("nf_calories");
        jsonObject.add("fields", fields);
        JsonObject queries = new JsonObject();
        queries.addProperty("item_name", query);
        jsonObject.add("queries", queries);
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

    public String fetchNutritionixByBrand(int min, int max, String brandId) throws Exception{

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("appId", NUTRITIONIX_APP_ID);
        jsonObject.addProperty("appKey", NUTRITIONIX_APP_PWD);
        JsonArray fields = new JsonArray();
        fields.add("item_name");
        fields.add("brand_name");
        fields.add("item_id");
        fields.add("brand_id");
        fields.add("nf_calories");
        jsonObject.add("fields", fields);
        JsonObject filters = new JsonObject();
        JsonObject nfCalories = new JsonObject();
        nfCalories.addProperty("from", min);
        nfCalories.addProperty("to", max);
        filters.addProperty("item_type",1);
        filters.addProperty("brand_id", brandId);
        filters.add("nf_calories", nfCalories);
        jsonObject.add("filters",filters);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);
        ResponseEntity<String> result = restTemplate.postForEntity(NUTRITIONIX_URL, entity, String.class);
        return result.getBody();

    }

    public List<String> fetchBrandFromMahout(long id) throws Exception{
        JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource, "rating", "user_id", "item_id", "rating", null);
        System.out.println("!!!!!!yeah max!!!!!!!!!! "+dataModel.getMaxPreference());
        ItemSimilarity itemSimilarity = new EuclideanDistanceSimilarity(dataModel);
        Recommender itemRecommender = new GenericItemBasedRecommender(dataModel,itemSimilarity);
        List<RecommendedItem> itemRecommendations = itemRecommender.recommend(id, 3);
        List<String> ret = new ArrayList<>();
        List<String> brandIds = recHistoryRepository.findBrandId();
        for (RecommendedItem itemRecommendation : itemRecommendations) {
            ret.addAll(brandIds.stream().filter(brandId -> itemRecommendation.getItemID() == Math.abs(brandId.hashCode())).collect(Collectors.toList()));
            //System.out.println("Item: " + itemRecommendation);
        }
        return ret;
    }

    public List<RecHistory> fetchNutritionixResult(JsonArray results, int n){
        HashSet<String> hs = new HashSet<>();
        List<RecHistory> ret = new ArrayList<>();
        User u = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        for(int i=0;i<results.size();i++){
            JsonObject cur = results.get(i).getAsJsonObject().get("fields").getAsJsonObject();
            if(!hs.contains(cur.get("brand_id").getAsString())){
                RecHistory recHistory = new RecHistory();
                recHistory.setBrandId(cur.get("brand_id").getAsString());
                recHistory.setBrandName(cur.get("brand_name").getAsString());
                recHistory.setFoodId(cur.get("item_id").getAsString());
                recHistory.setFoodName(cur.get("item_name").getAsString());
                recHistory.setTimestamp(ZonedDateTime.now());
                recHistory.setUser(u);
                ret.add(recHistory);
                hs.add(cur.get("brand_id").getAsString());
            }
            if(hs.size() >= n) break;
        }
        return ret;
    }

}
