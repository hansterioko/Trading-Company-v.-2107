package rus.warehouse.trading_company.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.adapters.LocalDateTimeTypeAdapter;
import rus.warehouse.trading_company.models.Product;
import rus.warehouse.trading_company.models.Purchase;
import rus.warehouse.trading_company.modelsDTO.PagedDataDTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class ProductRepository {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String BASE_URL = "http://" + RunApplication.ip_address + ":8080";

    public static PagedDataDTO getAll(Number pageIndex, String isCheckExpirated, String search, String typeSorting, String sortBy){

        Request request = new Request.Builder()
                .url(BASE_URL + "/products?pageIndex=" + pageIndex + "&isChecked=" + isCheckExpirated + "&search="
                        + search + "&typeSorting=" + typeSorting + "&sortBy=" + sortBy)
                .build();
        System.out.println("/products?pageIndex=" + pageIndex + "&search=" + search + "&typeSorting=" + typeSorting + "&sortBy=" + sortBy);
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        try {
            Response response = call.execute();
            Type typeOfT = new TypeToken<PagedDataDTO<Product>>(){}.getType();
            String dataJson = response.body().string();

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
