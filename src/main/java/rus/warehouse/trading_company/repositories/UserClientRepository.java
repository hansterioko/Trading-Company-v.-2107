package rus.warehouse.trading_company.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.models.Company;
import rus.warehouse.trading_company.models.UserClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class UserClientRepository {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String BASE_URL = "http://" + RunApplication.ip_address + ":8080";

    public static List<UserClient> getAll(){

        Request request = new Request.Builder()
                .url(BASE_URL + "/clients")
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        try {
            Response response = call.execute();
            Type typeOfT = new TypeToken<List<UserClient>>(){}.getType();

            String dataJson = response.body().string();

            //System.out.println(dataJson);

            if (response.isSuccessful()){
                return gson.fromJson(dataJson, typeOfT);
            }
            else{
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createCompany(UserClient userClient){
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        String jsonDate = gson.toJson(userClient);
        RequestBody body = RequestBody.create(JSON, jsonDate);

        Request request = new Request.Builder()
                .url(BASE_URL + "/clients/create")
                .post(body)
                .build();
        //System.out.println(request.url() + jsonDate);
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);


        try {
            Response response = call.execute();

            if (response.code() == 200){
                return "true";
            } else if (response.code() == 500) {
                return "exist";
            }
            else{
                return "false";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
