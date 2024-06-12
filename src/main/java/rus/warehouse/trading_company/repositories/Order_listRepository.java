package rus.warehouse.trading_company.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.adapters.LocalDateTimeTypeAdapter;
import rus.warehouse.trading_company.modelsDTO.Order_listDTO;
import rus.warehouse.trading_company.modelsDTO.Purchase_listDTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class Order_listRepository {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String BASE_URL = "http://" + RunApplication.ip_address + ":8080";

    public static Order_listDTO getByIdOrder(Integer idOrder){

        Request request = new Request.Builder()
                .url(BASE_URL + "/orders/detail?idOrder=" + idOrder)
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        //System.out.println(request.body());
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        try {
            Response response = call.execute();
            Type typeOfT = new TypeToken<Order_listDTO>(){}.getType();

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
