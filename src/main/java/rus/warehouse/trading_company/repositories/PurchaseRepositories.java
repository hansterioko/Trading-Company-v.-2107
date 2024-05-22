package rus.warehouse.trading_company.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.adapters.LocalDateTimeTypeAdapter;
import rus.warehouse.trading_company.models.Company;
import rus.warehouse.trading_company.models.Purchase;
import rus.warehouse.trading_company.modelsDTO.PagedDataDTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class PurchaseRepositories {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String BASE_URL = "http://" + RunApplication.ip_address + ":8080";
    public static PagedDataDTO getAll(Number pageIndex, LocalDate startDate, LocalDate endDate, String listCompany, String sortFilterDate){
        String listProviders = "";
        if (!Objects.isNull(listCompany) & !listCompany.trim().isEmpty()){
            //System.out.println(listCompany + "  in repos");
            listProviders = "&providers=" + listCompany;
        }
        String sort = "";
        if (sortFilterDate.equals("DESC")){
            sort = "&sort=0";
        }
        else {
            sort = "&sort=1";
        }
        String from = "";
        String to = "";
        if (!Objects.isNull(startDate)) {
            from = startDate.atTime(00, 00, 00).toString();
            //System.out.println(from);
        }
        if (!Objects.isNull(endDate)){
            to = endDate.atTime(23, 59, 59).toString();
            //System.out.println(from);
        }

        Request request = new Request.Builder()
                .url(BASE_URL + "/purchases?page=" + pageIndex + "&from=" + from + "&to=" + to + sort + listProviders)
                .build();
        System.out.println("/purchases?page=" + pageIndex + "&from=" + from + "&to=" + to + sort + listProviders);
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        //System.out.println(request.body());
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        try {
            Response response = call.execute();
            Type typeOfT = new TypeToken<PagedDataDTO<Purchase>>(){}.getType();

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
