package com.example.test;

import javafx.scene.paint.Color;

import java.text.DecimalFormat;

public final class Constants
{
    public static double[] MODIFYCOLLECTION = new double[] { 4, 24, 34, 58, 72, 80, 91,102, 107, 114, 118, 120, 123 };
    public static double[] DAZEDCOLLECTION = new double[] { 58, 63, 65, 79, 87, 89, 99, 105, 108, 109, 118, 122, 129, 130 };
    public static DecimalFormat PRICE_FORMAT = new DecimalFormat("#.##");
    public static DecimalFormat ID_FORMAT = new DecimalFormat("#");


    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String COUNTRY_PATTERN = "^[A-Za-z- ]+";
    public static final String CITY_PATTERN = "^[A-Za-z- ]+";
    public static final String ZIPCODE_PATTERN = "^\\d{6}";
    public static final String CARDNAME_PATTERN = "^[A-Z -]+";
    public static final String CARDNUMBER_PATTERN = "^\\d{4} \\d{4} \\d{4} \\d{4}";
    public static final String EXPDATE_PATTERN = "^\\d{2}/\\d{2}";
    public static final String CVV_PATTERN = "^\\d{3}";
    public static final String URL_PATTERN = "^https://.+";
    public static final String PRICE_PATTERN = "^\\d+,\\d{1,2}";


    public static final String MAINPAGE = "./src/main/resources/com/example/test/main_page.fxml";
    public static final String CUSTOMERPAGE = "./src/main/resources/com/example/test/customer_page.fxml";
    public static final String VENDORPAGE = "./src/main/resources/com/example/test/vendor_page.fxml";
    public static final String SHOPPAGE = "./src/main/resources/com/example/test/shop_page.fxml";
    public static final String ADMINPAGE = "./src/main/resources/com/example/test/admin_page.fxml";
    public static final String LOGIN = "./src/main/resources/com/example/test/login.fxml";
    public static final String SIGNUP = "./src/main/resources/com/example/test/signup.fxml";
    public static final String ITEM = "./src/main/resources/com/example/test/item.fxml";

    public static final String SHOP = "./src/main/resources/com/example/test/shop.fxml";
    public static final String PAYMENT = "./src/main/resources/com/example/test/payment.fxml";
    public static final String PROFILE = "./src/main/resources/com/example/test/profile.fxml";
    public static final String ORDER = "./src/main/resources/com/example/test/order.fxml";
    public static final String SETTINGS = "./src/main/resources/com/example/test/settings.fxml";
    public static final String PAYMENTPAGE = "./src/main/resources/com/example/test/payment_page.fxml";
    public static final String ORDERITEM = "./src/main/resources/com/example/test/order_item.fxml";
    public static final String REQUESTSPAGE = "./src/main/resources/com/example/test/requests_page.fxml";
    public static final String REQUEST = "./src/main/resources/com/example/test/request.fxml";
    public static final String CONNECTIONERROR = "./src/main/resources/com/example/test/connection_error.fxml";
    public static final String EDITITEM = "./src/main/resources/com/example/test/edit_item.fxml";


    // по другому почему-то не работает...
    public static final String ITEMSIMAGEPATH = "D:\\Repositories\\clothing-store\\src\\main\\resources\\images\\items\\";
    public static final String SHOPSIMAGEPATH = "D:\\Repositories\\clothing-store\\src\\main\\resources\\images\\banners\\";
    public static final String ICONPATH = "/images/icon.png";


    public static final Color ACTIVECOLOR = Color.web("#484c58");
    public static final Color DISACTIVECOLOR = Color.WHITE;
    public static final String JSONFILE = "./src/main/resources/data.json";
}
