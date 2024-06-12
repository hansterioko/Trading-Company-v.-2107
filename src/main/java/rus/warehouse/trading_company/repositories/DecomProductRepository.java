package rus.warehouse.trading_company.repositories;

import okhttp3.MediaType;
import rus.warehouse.trading_company.RunApplication;

public class DecomProductRepository {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String BASE_URL = "http://" + RunApplication.ip_address + ":8080";


}
