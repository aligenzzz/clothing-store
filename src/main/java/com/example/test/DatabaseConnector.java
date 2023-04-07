package com.example.test;

import com.example.test.entities.*;
import com.example.test.enums.AccessType;
import com.example.test.enums.ItemInfo;
import com.example.test.enums.ShopInfo;
import com.example.test.enums.UserAccount;
import com.example.test.interfaces.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
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
    private DatabaseConnector()
    {
        userAccountsFileName = Constants.USERACCOUNTS;
        itemsFileName = Constants.ITEMS;
        favouriteItemsFileName = Constants.FAVOURITEITEMS;
        purchasedItemsFileName = Constants.PURCHASEDITEMS;
        shoppingItemsFileName = Constants.SHOPPINGITEMS;
        shopsFileName = Constants.SHOPS;
        shopItemsFileName = Constants.SHOPITEMS;
        vendorsShopFileName = Constants.VENDORSSHOP;
        customerShopsFileName = Constants.CUSTOMERSHOPS;
    }
    public boolean isFoundUser(String username, String password)
    {
        try
        {
            FileInputStream file = new FileInputStream(userAccountsFileName);
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
        catch (Exception exception)
        {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean isFoundUser(String parameter, int col) throws IOException
    {
        try
        {
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
                    String data;
                    if(cell.getCellType() == CellType.STRING) data = cell.getStringCellValue();
                    else data = Double.toString(cell.getNumericCellValue());

                    if (column == col && data.equals(parameter)) return true;

                    column++;
                }
            }
            file.close();
            return false;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return false;
        }
    }

    public User getUser(String username_, String password_)
    {
        if (!this.isFoundUser(username_, password_)) return null;

        double id = -1;
        String username = "";
        String password = "";
        String email = "";
        String firstName = "";
        String lastName = "";
        AccessType accessType = AccessType.nonuser;

        try {
            FileInputStream file = new FileInputStream(userAccountsFileName);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt (0);

            for (Row row : sheet) {
                boolean activeRow = false;

                Iterator<Cell> cellIterator = row.cellIterator();
                int column = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String data;
                    if(cell.getCellType() == CellType.STRING) data = cell.getStringCellValue();
                    else data = Double.toString(cell.getNumericCellValue());

                    if (column == UserAccount.USERNAME.getIndex() && data.equals(username_)) {
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
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
    public void AddUser(String username, String email, String firstname, String lastname, String password)
    {
        try
        {
            FileInputStream file = new FileInputStream(userAccountsFileName);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt (0);

            int row_num = sheet.getLastRowNum();
            row_num++;
            Row row = sheet.createRow(row_num);
            row_num++;

            Cell cell = row.createCell(UserAccount.ID.getIndex());
            cell.setCellValue(row_num);
            cell = row.createCell(UserAccount.USERNAME.getIndex());
            cell.setCellValue(username);
            cell = row.createCell(UserAccount.PASSWORD.getIndex());
            cell.setCellValue(password);
            cell = row.createCell(UserAccount.EMAIL.getIndex());
            cell.setCellValue(email);
            cell = row.createCell(UserAccount.FIRSTNAME.getIndex());
            cell.setCellValue(firstname);
            cell = row.createCell(UserAccount.LASTNAME.getIndex());
            cell.setCellValue(lastname);
            cell = row.createCell(UserAccount.ACCESSTYPE.getIndex());
            cell.setCellValue(AccessType.customer.toString());

            FileOutputStream out = new FileOutputStream(userAccountsFileName);
            workbook.write(out);
            out.close();
            file.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }
    }
    public void getItems(List<Item> itemList) throws IOException
    {
        try
        {
            FileInputStream file = new FileInputStream(itemsFileName);
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
                itemList.add(new Item(id, name, imageSource, price, shop));
            }
            file.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }
    }

    public List<Item> getFavouriteItems(double id) { return this.getSubItems(id, favouriteItemsFileName); }

    public List<Item> getPurchasedItems(double id) { return this.getSubItems(id, purchasedItemsFileName); }

    public List<Item> getShoppingItems(double id) { return this.getSubItems(id, shoppingItemsFileName); }

    public void addFavouriteItem(double userId, double itemId) { this.addItemToList(userId, itemId, favouriteItemsFileName); }
    public void addPurchasedItem(double userId, double itemId) { this.addItemToList(userId, itemId, purchasedItemsFileName); }
    public void addShoppingItem(double userId, double itemId) { this.addItemToList(userId, itemId, shoppingItemsFileName); }

    public List<Shop> getFavouriteShops(double customer)
    {
        List<Shop> result = new ArrayList<>();

        try
        {
            FileInputStream file = new FileInputStream(customerShopsFileName);
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
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }

        return result;
    }

    public Shop getShop(double id)
    {
        String name = "";
        List<Item> items = null;
        String imageSource = "";
        Color textColor = Color.WHITE;
        double vendor = 0;

        try
        {
            FileInputStream file = new FileInputStream(shopsFileName);
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

            items = getSubItems(id, shopItemsFileName);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }

        return new Shop(id, name, items, imageSource, textColor, vendor);
    }

    public double getShopId(double vendor)
    {
        double id = 0;

        try
        {
            FileInputStream file = new FileInputStream(vendorsShopFileName);
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
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }

        return id;
    }


    private List<Item> getSubItems(double id, String path)
    {
        List<Double> subItemsIds = new ArrayList<>();
        List<Item> subItems = new ArrayList<>();
        try
        {
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

            file = new FileInputStream(itemsFileName);
            workbook = new XSSFWorkbook(file);
            sheet = workbook.getSheetAt (0);

            for (double element: subItemsIds) {
                subItems.add(this.getItem(element, sheet));
            }

            file.close();

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }

        return subItems;
    }

    private Item getItem(double id, XSSFSheet sheet)
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

    private void addItemToList(double userId, double itemId, String path)
    {
        try
        {
            FileInputStream file = new FileInputStream(path);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt (0);

            int row_num = (int)userId - 1;
            Row row = sheet.getRow(row_num);

            if (row == null)
            {
                row = sheet.createRow(row_num);
                Cell cell = row.createCell(0);
                cell.setCellValue(itemId);
            }
            else
            {
                Cell cell = row.createCell(row.getLastCellNum());
                cell.setCellValue(itemId);
            }

            FileOutputStream out = new FileOutputStream(path);
            workbook.write(out);
            out.close();
            file.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }
    }

    public void fillUsersTable(TableView<User> tableView)
    {
        tableView.setItems(FXCollections.observableList(this.getUsers()));
    }

    public List<User> getUsers()
    {
        List<User> users = new ArrayList<>();

        double id = -1;
        String username = "";
        String password = "";
        String email = "";
        String firstName = "";
        String lastName = "";
        AccessType accessType = AccessType.nonuser;

        try {
            FileInputStream file = new FileInputStream(userAccountsFileName);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt (0);

            for (Row row : sheet) {

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

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            exception.getCause();
        }

        return users;
    }
}
