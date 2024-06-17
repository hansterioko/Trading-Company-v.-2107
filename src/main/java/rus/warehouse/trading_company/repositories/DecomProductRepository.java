package rus.warehouse.trading_company.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.adapters.LocalDateTimeTypeAdapter;
import rus.warehouse.trading_company.models.Company;
import rus.warehouse.trading_company.models.DecomProduct;
import rus.warehouse.trading_company.modelsDTO.DecomProductDTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

public class DecomProductRepository {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String BASE_URL = "http://" + RunApplication.ip_address + ":8080";

    public static void addDecomProd(Integer idProduct, Integer count, String summery){
        DecomProductDTO decomProductDTO = new DecomProductDTO(idProduct, summery, count);

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();

        String jsonDate = gson.toJson(decomProductDTO);
        RequestBody body = RequestBody.create(JSON, jsonDate);

        Request request = new Request.Builder()
                .url(BASE_URL + "/decomprod/create")
                .post(body)
                .build();
        System.out.println(request.url() + jsonDate);
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        //System.out.println(request.body() + " COMPANY REP");

        try {
            Response response = call.execute();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<DecomProduct> getAll(){
        Request request = new Request.Builder()
                .url(BASE_URL + "/decomprod")
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        //System.out.println(request.body() + " COMPANY REP");
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        try {
            Response response = call.execute();
            Type typeOfT = new TypeToken<List<DecomProduct>>(){}.getType();

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
}
