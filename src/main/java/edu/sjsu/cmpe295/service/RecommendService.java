package edu.sjsu.cmpe295.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.sjsu.cmpe295.domain.User;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by BladeInShine on 16/4/17.
 */
@Service
public class RecommendService {

    private static final String NUTRITIONIX_APP_ID = "4cfcb19a";
    private static final String NUTRITIONIX_APP_PWD = "3df8811bc08567ce00dc8c67b8de0327";
    private static final String NUTRITIONIX_URL = "https://api.nutritionix.com/v1_1/search";

    public double calorieCalculator(User user){
        double base;
        if(user.getGender().equals("M")){
            base = 66+1.38*user.getWeight()+5*user.getHeight()-6.8*user.getAge();
        }
        else if(user.getGender().equals("F")){
            base = 65.5+9.6*user.getWeight()+1.9*user.getHeight()-4.7*user.getAge();
        }
        else{
            return -1;
        }

        return base;
    }

    public String fetchNutritionixByCalory(int min, int max) throws Exception{

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(NUTRITIONIX_URL);

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

        StringEntity entity = new StringEntity(jsonObject.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        BufferedReader rd = new BufferedReader(
            new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        client.close();
        return result.toString();

    }

}
