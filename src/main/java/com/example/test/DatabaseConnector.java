package com.example.test;

import com.example.test.entities.*;
import com.example.test.enums.*;
import com.example.test.interfaces.User;
import javafx.scene.paint.Color;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DatabaseConnector
{
    private static DatabaseConnector instance;
    public static synchronized DatabaseConnector getInstance()
    {
        if (instance == null) instance = new DatabaseConnector();
        return instance;
    }
    private final String userAccountsFileName;
    private final String itemsFileName;
    private final String favouriteItemsFileName;
    private final String purchasedItemsFileName;
    private final String shoppingItemsFileName;
    private final String shopsFileName;
    private final String shopItemsFileName;
    private final String vendorsShopFileName;
    private final String customerShopsFileName;
    private final String ordersFileName;
    private final String orderItemsFileName;
    private DatabaseConnector()
    {
        this.userAccountsFileName = Constants.USERACCOUNTS;
        this.itemsFileName = Constants.ITEMS;
        this.favouriteItemsFileName = Constants.FAVOURITEITEMS;
        this.purchasedItemsFileName = Constants.PURCHASEDITEMS;
        this.shoppingItemsFileName = Constants.SHOPPINGITEMS;
        this.shopsFileName = Constants.SHOPS;
        this.shopItemsFileName = Constants.SHOPITEMS;
        this.vendorsShopFileName = Constants.VENDORSSHOP;
        this.customerShopsFileName = Constants.CUSTOMERSHOPS;
        this.ordersFileName = Constants.ORDERS;
        this.orderItemsFileName = Constants.ORDERITEMS;
    }
    public User getUser(String username_, String password_) throws IOException
    {
        if (!this.isFoundUser(username_, password_)) return null;

        double id = -1;
        String username = "";
        String password = "";
        String email = "";
        String firstName = "";
        String lastName = "";
        AccessType accessType = AccessType.nonuser;

        FileInputStream file = new FileInputStream(this.userAccountsFileName);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt (0);

        for (Row row : sheet)
        {
            boolean activeRow = false;

            Iterator<Cell> cellIterator = row.cellIterator();
            int column = 0;
            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();
                String data;
                if(cell.getCellType() == CellType.STRING) data = cell.getStringCellValue();
                else data = Double.toString(cell.getNumericCellValue());

                if (column == UserAccount.USERNAME.getIndex() && data.equals(username_))
                {
                    activeRow = true;
                    break;
                }

                column++;
            }

            if (activeRow)
            {
                cellIterator = row.cellIterator();
                column = 0;
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();

                    if (column == UserAccount.ID.getIndex()) id = cell.getNumericCellValue();
                    else if (column == UserAccount.USERNAME.getIndex()) username = cell.getStringCellValue();
                    else if (column == UserAccount.PASSWORD.getIndex()) password = cell.getStringCellValue();
                    else if (column == UserAccount.EMAIL.getIndex()) email = cell.getStringCellValue();
                    else if (column == UserAccount.FIRSTNAME.getIndex()) firstName = cell.getStringCellValue();
                    else if (column == UserAccount.LASTNAME.getIndex()) lastName = cell.getStringCellValue();
                    else if (column == UserAccount.ACCESSTYPE.getIndex()) accessType = AccessType.valueOf(cell.getStringCellValue());

                    column++;
                }
                break;
            }
        }
        file.close();

        User user;
        if (accessType == AccessType.customer) user = new Customer(id, username, password, email, firstName, lastName, accessType);
        else if (accessType == AccessType.vendor) user = new Vendor(id, username, password, email, firstName, lastName, accessType);
        else if (accessType == AccessType.admin) user = new Admin(id, username, password, email, firstName, lastName, accessType);
        else user = new NonUser(accessType);

        return user;
    }
    public List<User> getUsers() throws IOException
    {
        List<User> users = new ArrayList<>();

        double id = -1;
        String username = "";
        String password = "";
        String email = "";
        String firstName = "";
        String lastName = "";
        AccessType accessType = AccessType.nonuser;

        FileInputStream file = new FileInputStream(userAccountsFileName);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt (0);

        for (Row row : sheet)
        {
            Iterator<Cell> cellIterator = row.cellIterator();
            int column = 0;
            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();

                if (column == UserAccount.ID.getIndex()) id = cell.getNumericCellValue();
                else if (column == UserAccount.USERNAME.getIndex()) username = cell.getStringCellValue();
                else if (column == UserAccount.PASSWORD.getIndex()) password = cell.getStringCellValue();
                else if (column == UserAccount.EMAIL.getIndex()) email = cell.getStringCellValue();
                else if (column == UserAccount.FIRSTNAME.getIndex()) firstName = cell.getStringCellValue();
                else if (column == UserAccount.LASTNAME.getIndex()) lastName = cell.getStringCellValue();
                else if (column == UserAccount.ACCESSTYPE.getIndex()) accessType = AccessType.valueOf(cell.getStringCellValue());

                column++;
            }

            users.add(new User(id, username, password, email, firstName, lastName, accessType));
        }
        file.close();

        return users;
    }
    public void AddUser(@NotNull User user) throws IOException
    {
        FileInputStream file = new FileInputStream(this.userAccountsFileName);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = sheet.getLastRowNum();
        row_num++;
        Row row = sheet.createRow(row_num);
        row_num++;

        Cell cell = row.createCell(UserAccount.ID.getIndex());
        user.setId(row_num);
        cell.setCellValue(user.getId());
        cell = row.createCell(UserAccount.USERNAME.getIndex());
        cell.setCellValue(user.getUsername());
        cell = row.createCell(UserAccount.PASSWORD.getIndex());
        cell.setCellValue(user.getPassword());
        cell = row.createCell(UserAccount.EMAIL.getIndex());
        cell.setCellValue(user.getEmail());
        cell = row.createCell(UserAccount.FIRSTNAME.getIndex());
        cell.setCellValue(user.getFirstName());
        cell = row.createCell(UserAccount.LASTNAME.getIndex());
        cell.setCellValue(user.getLastName());
        cell = row.createCell(UserAccount.ACCESSTYPE.getIndex());
        cell.setCellValue(user.getAccessType().toString());

        FileOutputStream out = new FileOutputStream(userAccountsFileName);
        workbook.write(out);
        out.close();
        file.close();
    }
    public boolean isFoundUser(String parameter, UserAccount property) throws IOException
    {
        FileInputStream file = new FileInputStream(this.userAccountsFileName);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt (0);

        for (Row row : sheet)
        {
            Iterator<Cell> cellIterator = row.cellIterator();
            int column = 0;
            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();
                String data;
                if(cell.getCellType() == CellType.STRING) data = cell.getStringCellValue();
                else data = Double.toString(cell.getNumericCellValue());

                if (column == property.getIndex() && data.equals(parameter)) return true;

                column++;
            }
        }
        file.close();
        return false;
    }
    public List<Item> getItems() throws IOException
    {
        List<Item> items = new ArrayList<>();

        FileInputStream file = new FileInputStream(this.itemsFileName);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt (0);

        for (Row row : sheet)
        {
            double id = 0;
            String name = "";
            String imageSource = "";
            double price = 0;
            double shop = 0;

            Iterator<Cell> cellIterator = row.cellIterator();
            int column = 0;
            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();
                if (column == ItemInfo.ID.getIndex()) id = cell.getNumericCellValue();
                else if (column == ItemInfo.NAME.getIndex()) name = cell.getStringCellValue();
                else if (column == ItemInfo.IMAGESOURCE.getIndex()) imageSource = cell.getStringCellValue();
                else if (column == ItemInfo.PRICE.getIndex()) price = Double.parseDouble(cell.getStringCellValue().replaceAll("[ $]", ""));
                else if (column == ItemInfo.SHOP.getIndex()) shop = cell.getNumericCellValue();
                column++;
            }
            items.add(new Item(id, name, imageSource, price, shop));
        }
        file.close();

        return items;
    }
    public @NotNull Item getItem(double id) throws IOException
    {
        String name = "";
        String imageSource = "";
        double price = 0;
        double shop = 0;

        FileInputStream file = new FileInputStream(this.itemsFileName);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt (0);
        Row row = sheet.getRow((int)id - 1);
        Iterator<Cell> cellIterator = row.cellIterator();
        int column = 0;
        while (cellIterator.hasNext())
        {
            Cell cell = cellIterator.next();
            if (column == ItemInfo.NAME.getIndex()) name = cell.getStringCellValue();
            else if (column == ItemInfo.IMAGESOURCE.getIndex()) imageSource = cell.getStringCellValue();
            else if (column == ItemInfo.PRICE.getIndex()) price = Double.parseDouble(cell.getStringCellValue().replaceAll("[ $]", ""));
            else if (column == ItemInfo.SHOP.getIndex()) shop = cell.getNumericCellValue();
            column++;
        }

        return new Item(id, name, imageSource, price, shop);
    }
    public List<Item> getFavouriteItems(double customer) throws IOException { return this.getSubItems(customer, this.favouriteItemsFileName); }
    public List<Item> getPurchasedItems(double customer) throws IOException { return this.getSubItems(customer, this.purchasedItemsFileName); }
    public List<Item> getShoppingItems(double customer) throws IOException { return this.getSubItems(customer, this.shoppingItemsFileName); }
    public void addFavouriteItem(double customer, double item) throws IOException { this.addItemToList(customer, item, this.favouriteItemsFileName); }
    public void addPurchasedItem(double customer, double item) throws IOException { this.addItemToList(customer, item, this.purchasedItemsFileName); }
    public void addShoppingItem(double customer, double item) throws IOException { this.addItemToList(customer, item, this.shoppingItemsFileName); }
    public List<Shop> getFavouriteShops(double customer) throws IOException
    {
        List<Shop> result = new ArrayList<>();

        FileInputStream file = new FileInputStream(this.customerShopsFileName);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt (0);

        Row row = sheet.getRow((int)customer - 1);

        Iterator<Cell> cellIterator = row.cellIterator();

        while (cellIterator.hasNext())
        {
            Cell cell = cellIterator.next();
            result.add(this.getShop(cell.getNumericCellValue()));
        }
        file.close();

        return result;
    }
    public Shop getShop(double id) throws IOException
    {
        String name = "";
        List<Item> items;
        String imageSource = "";
        Color textColor = Color.WHITE;
        double vendor = 0;

        FileInputStream file = new FileInputStream(this.shopsFileName);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt (0);

        Row row = sheet.getRow((int)id - 1);

        Iterator<Cell> cellIterator = row.cellIterator();
        int column = 0;
        while (cellIterator.hasNext())
        {
            Cell cell = cellIterator.next();
            if (column == ShopInfo.NAME.getIndex()) name = cell.getStringCellValue();
            else if (column == ShopInfo.IMAGESOURCE.getIndex()) imageSource = cell.getStringCellValue();
            else if (column == ShopInfo.TEXTCOLOR.getIndex()) textColor = Color.web(cell.getStringCellValue());
            else if (column == ShopInfo.VENDOR.getIndex()) vendor = cell.getNumericCellValue();
            column++;
        }
        file.close();

        items = getSubItems(id, this.shopItemsFileName);

        return new Shop(id, name, items, imageSource, textColor, vendor);
    }
    public double getShopId(double vendor) throws IOException
    {
        double id = 0;

        FileInputStream file = new FileInputStream(this.vendorsShopFileName);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt (0);

        for (Row row : sheet)
        {
            if (row.getCell(0).getNumericCellValue() == vendor)
            {
                id = row.getCell(1).getNumericCellValue();
                break;
            }
        }
        file.close();

        return id;
    }
    public List<Order> getOrders(double customer) throws IOException
    {
        List<Order> orders = new ArrayList<>();

        double id = -1;
        OrderState state = OrderState.booked;
        double price = 0;

        FileInputStream file = new FileInputStream(this.ordersFileName);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt (0);

        for (Row row : sheet)
        {
            Iterator<Cell> cellIterator = row.cellIterator();
            int column = 0;
            double temp = 0;
            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();

                if (column == OrderInfo.ID.getIndex()) id = cell.getNumericCellValue();
                else if (column == OrderInfo.STATE.getIndex()) state = OrderState.valueOf(cell.getStringCellValue());
                else if (column == OrderInfo.PRICE.getIndex()) price = cell.getNumericCellValue();
                else if (column == OrderInfo.CUSTOMER.getIndex()) temp = cell.getNumericCellValue();

                column++;
            }

            if(temp == customer)
            {
                List<Item> items = this.getSubItems(id, this.orderItemsFileName);
                orders.add(new Order(id, state, price, customer));
                orders.get(orders.size() - 1).setItems(items);
            }
        }
        file.close();

        return orders;
    }
    public void addOrder(@NotNull Order order) throws IOException
    {
        FileInputStream file = new FileInputStream(this.ordersFileName);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = sheet.getLastRowNum();
        row_num++;
        Row row = sheet.createRow(row_num);
        row_num++;

        Cell cell = row.createCell(OrderInfo.ID.getIndex());
        order.setId(row_num);
        cell.setCellValue(order.getId());
        cell = row.createCell(OrderInfo.STATE.getIndex());
        cell.setCellValue(order.getState().toString());
        cell = row.createCell(OrderInfo.PRICE.getIndex());
        cell.setCellValue(order.getPrice());
        cell = row.createCell(OrderInfo.CUSTOMER.getIndex());
        cell.setCellValue(order.getCustomer());

        FileOutputStream out = new FileOutputStream(this.ordersFileName);
        workbook.write(out);
        out.close();
        file.close();

        file = new FileInputStream(this.orderItemsFileName);
        workbook = new XSSFWorkbook(file);
        sheet = workbook.getSheetAt (0);

        row = sheet.createRow((int)order.getId() - 1);
        for (int i = 0; i < order.getItems().size(); i++)
        {
            cell = row.createCell(i);
            cell.setCellValue(order.getItems().get(i).getId());
        }

        out = new FileOutputStream(this.orderItemsFileName);
        workbook.write(out);
        out.close();
        file.close();
    }
    private boolean isFoundUser(String username, String password) throws IOException
    {
        FileInputStream file = new FileInputStream(this.userAccountsFileName);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt (0);

        for (Row row : sheet)
        {
            boolean result1 = false;
            boolean result2 = false;

            Iterator<Cell> cellIterator = row.cellIterator();
            int column = 0;
            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();
                String data;
                if(cell.getCellType() == CellType.STRING) data = cell.getStringCellValue();
                else data = Double.toString(cell.getNumericCellValue());

                if (column == UserAccount.USERNAME.getIndex() && data.equals(username)) result1 = true;
                else if (column == UserAccount.PASSWORD.getIndex() && result1 && data.equals(password)) result2 = true;

                column++;
            }
            if (result1 && result2)
            {
                file.close();
                return true;
            }
        }
        file.close();
        return false;
    }
    private @Nullable List<Item> getSubItems(double id, String path) throws IOException
    {
        List<Double> subItemsIds = new ArrayList<>();
        List<Item> subItems = new ArrayList<>();

        FileInputStream file = new FileInputStream(path);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt (0);

        Row row = sheet.getRow((int)id - 1);

        if (row == null) return null;

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext())
        {
            Cell cell = cellIterator.next();
            subItemsIds.add(cell.getNumericCellValue());
        }
        file.close();

        file = new FileInputStream(this.itemsFileName);
        workbook = new XSSFWorkbook(file);
        sheet = workbook.getSheetAt (0);

        for (double element: subItemsIds)
            subItems.add(this.getItem(element, sheet));

        file.close();

        return subItems;
    }
    private @NotNull Item getItem(double id, @NotNull XSSFSheet sheet)
    {
        String name = "";
        String imageSource = "";
        double price = 0;
        double shop = 0;

        Row row = sheet.getRow((int)id - 1);
        Iterator<Cell> cellIterator = row.cellIterator();
        int column = 0;
        while (cellIterator.hasNext())
        {
            Cell cell = cellIterator.next();
            if (column == ItemInfo.NAME.getIndex()) name = cell.getStringCellValue();
            else if (column == ItemInfo.IMAGESOURCE.getIndex()) imageSource = cell.getStringCellValue();
            else if (column == ItemInfo.PRICE.getIndex()) price = Double.parseDouble(cell.getStringCellValue().replaceAll("[ $]", ""));
            else if (column == ItemInfo.SHOP.getIndex()) shop = cell.getNumericCellValue();
            column++;
        }

        return new Item(id, name, imageSource, price, shop);
    }
    private void addItemToList(double user, double item, String path) throws IOException
    {
        FileInputStream file = new FileInputStream(path);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = (int)user - 1;
        Row row = sheet.getRow(row_num);

        if (row == null)
        {
            row = sheet.createRow(row_num);
            Cell cell = row.createCell(0);
            cell.setCellValue(item);
        }
        else
        {
            Cell cell = row.createCell(row.getLastCellNum());
            cell.setCellValue(item);
        }

        FileOutputStream out = new FileOutputStream(path);
        workbook.write(out);
        out.close();
        file.close();
    }
}
