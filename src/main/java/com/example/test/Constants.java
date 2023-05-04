package com.example.test;

import javafx.scene.paint.Color;

import java.text.DecimalFormat;

public final class Constants
{
    public static double[] MODIFYCOLLECTION = new double[] { 4, 24, 34, 58, 72, 80, 91,102, 107, 114, 118, 120, 123 };
    public static double[] DAZEDCOLLECTION = new double[] { 58, 63, 65, 79, 87, 89, 99, 105, 108, 109, 118, 122, 129, 130 };
    public static DecimalFormat FORMAT = new DecimalFormat("#.##");
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

    public static final String USERACCOUNTS = "./src/main/resources/database/user_account.xlsx";
    public static final String ITEMS = "./src/main/resources/database/item.xlsx";
    public static final String FAVOURITEITEMS = "./src/main/resources/database/favourite_items.xlsx";
    public static final String PURCHASEDITEMS = "./src/main/resources/database/purchased_items.xlsx";
    public static final String SHOPPINGITEMS = "./src/main/resources/database/shopping_items.xlsx";
    public static final String SHOPS = "./src/main/resources/database/shops.xlsx";
    public static final String SHOPITEMS = "./src/main/resources/database/shop_items.xlsx";
    public static final String CUSTOMERSHOPS = "./src/main/resources/database/customer_shops.xlsx";
    public static final String ORDERS = "./src/main/resources/database/orders.xlsx";
    public static final String ORDERITEMS = "./src/main/resources/database/order_items.xlsx";

    // по другому почему-то не работает...
    public static final String ITEMSIMAGEPATH = "D:\\Repositories\\clothing-store\\src\\main\\resources\\images\\items\\";
    public static final String SHOPSIMAGEPATH = "D:\\Repositories\\clothing-store\\src\\main\\resources\\images\\banners\\";
    public static final String ICONPATH = "/images/icon.png";

    public static final Color ACTIVECOLOR = Color.web("#484c58");
    public static final String JSONFILE = "./src/main/resources/data.json";
}
