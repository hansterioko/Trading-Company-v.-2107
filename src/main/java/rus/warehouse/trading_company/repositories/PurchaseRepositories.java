package rus.warehouse.trading_company.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.adapters.LocalDateTimeTypeAdapter;
import rus.warehouse.trading_company.models.Purchase;
import rus.warehouse.trading_company.modelsDTO.PagedDataDTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

public class PurchaseRepositories {

    private static final String BASE_URL = "http://" + RunApplication.ip_address + ":8080";
    public static PagedDataDTO getAll(){
        Request request = new Request.Builder()
                .url(BASE_URL + "/purchases")
                .build();

        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        try {
            Response response = call.execute();
            Type typeOfT = new TypeToken<PagedDataDTO<Purchase>>(){}.getType();

            String dataJson = response.body().string();

            System.out.println(dataJson);

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
