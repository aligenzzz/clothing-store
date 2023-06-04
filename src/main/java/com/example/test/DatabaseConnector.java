package com.example.test;

import com.example.test.entities.*;
import com.example.test.enums.*;
import com.example.test.interfaces.User;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import javafx.scene.paint.Color;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

public class DatabaseConnector
{
    private static DatabaseConnector instance;
    public static synchronized DatabaseConnector getInstance()
    {
        if (instance == null)
        {
            try
            {
                instance = new DatabaseConnector();
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
                exception.getCause();
            }

        }
        return instance;
    }

    private final String userAccountsFileId;
    private final String itemsFileId;
    private final String favouriteItemsFileId;
    private final String purchasedItemsFileId;
    private final String shoppingItemsFileId;
    private final String shopsFileId;
    private final String shopItemsFileId;
    private final String customerShopsFileId;
    private final String ordersFileId;
    private final String orderItemsFileId;
    private final String requestsFileId;
    private DatabaseConnector()
    {
        try { DriveHandler.getService(); }
        catch (IOException | GeneralSecurityException e)
        {
            e.printStackTrace();
            e.getCause();
        }

        this.userAccountsFileId = "1fP_qCuCajroWcIheSJghdjF1VbKWaksa";
        this.itemsFileId = "1Tbf3fL33tBeJQ_gekqZZCRRNMhwL0OdL";
        this.favouriteItemsFileId = "1nbWdeQYFaAhO7V3goFzf1UKE-m_bLNHZ";
        this.purchasedItemsFileId = "1HRJe6MqTvqrOa5alV9nwjMEhDErdP1Z6";
        this.shoppingItemsFileId = "1IsNo5V0Ly5lJmyuoWdXgHj7pL-HdLhuA";
        this.shopsFileId = "1AfxWqn-tV61ygULiJLM6DDxRs-03501q";
        this.shopItemsFileId = "1EkmSDRYQlg1u7YhNfKhrkKIDiP_eSYWC";
        this.customerShopsFileId = "1Ih2BZzMGNw38P6doTnzWTTwhp-YUWGDi";
        this.ordersFileId = "1sjAfvtoLLKV5QTO-LIPob0swNj8Qc6GS";
        this.orderItemsFileId = "1ACJvVgxL2zonkL5HxNY2gVjPskJ46fFB";
        this.requestsFileId = "1Hv9TqH95coqmGIbGrILMV_spG85Zidz1";
    }

    @Contract("_ -> new")
    public static @NotNull ByteArrayInputStream getInputStream(String fileId)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try
        {
            DriveHandler.getService().files().get(fileId)
                    .executeMediaAndDownloadTo(outputStream);
        } catch (IOException | GeneralSecurityException e) { throw new RuntimeException(e); }

        byte[] fileBytes = outputStream.toByteArray();

        return new ByteArrayInputStream(fileBytes);
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

        ByteArrayInputStream inputStream = getInputStream(this.userAccountsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);

        for (Row row : sheet)
        {
            if (row.getRowNum() >= row_num)
                break;

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
        inputStream.close();

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

        ByteArrayInputStream inputStream = getInputStream(this.userAccountsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);

        for (Row row : sheet)
        {
            if (row.getRowNum() >= row_num)
                break;

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
        inputStream.close();

        return users;
    }
    public void addUser(@NotNull User user) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(this.userAccountsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);
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

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(this.userAccountsFileId, updatedContent);
    }
    public boolean isFoundUser(String parameter, UserAccount property) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(this.userAccountsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);

        for (Row row : sheet)
        {
            if (row.getRowNum() >= row_num)
                break;

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
        inputStream.close();
        return false;
    }
    public void editUser(@NotNull User user) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(this.userAccountsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = (int) user.getId() - 1;
        Row row = sheet.getRow(row_num);

        Cell cell = row.createCell(UserAccount.USERNAME.getIndex());
        cell.setCellValue(user.getUsername());
        cell = row.createCell(UserAccount.PASSWORD.getIndex());
        cell.setCellValue(user.getPassword());
        cell = row.createCell(UserAccount.EMAIL.getIndex());
        cell.setCellValue(user.getEmail());
        cell = row.createCell(UserAccount.FIRSTNAME.getIndex());
        cell.setCellValue(user.getFirstName());
        cell = row.createCell(UserAccount.LASTNAME.getIndex());
        cell.setCellValue(user.getLastName());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(this.userAccountsFileId, updatedContent);
    }

    public void editItem(@NotNull Item item) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(this.itemsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = (int) item.getId() - 1;
        Row row = sheet.getRow(row_num);

        Cell cell = row.createCell(ItemInfo.IMAGESOURCE.getIndex());
        cell.setCellValue(item.getImageSource());
        cell = row.createCell(ItemInfo.NAME.getIndex());
        cell.setCellValue(item.getName());
        cell = row.createCell(ItemInfo.PRICE.getIndex());
        cell.setCellValue(String.valueOf(item.getPrice()).replaceAll(",", ".") + " $");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(this.itemsFileId, updatedContent);
    }

    public double addItem(@NotNull Item item) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(this.itemsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);
        Row row = sheet.createRow(row_num);
        row_num++;

        Cell cell = row.createCell(ItemInfo.ID.getIndex());
        cell.setCellValue(row_num);
        cell = row.createCell(ItemInfo.IMAGESOURCE.getIndex());
        cell.setCellValue(item.getImageSource());
        cell = row.createCell(ItemInfo.NAME.getIndex());
        cell.setCellValue(item.getName());
        cell = row.createCell(ItemInfo.PRICE.getIndex());
        cell.setCellValue(String.valueOf(item.getPrice()).replaceAll(",", ".") + " $");
        cell = row.createCell(ItemInfo.SHOP.getIndex());
        cell.setCellValue(item.getShop());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(this.itemsFileId, updatedContent);

        this.addItemToList(item.getShop(), row_num, this.shopItemsFileId);

        return row_num;
    }

    public void deleteItem(@NotNull Item item) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(this.itemsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);

        int row_num = (int) item.getId() - 1;
        Row row = sheet.getRow(row_num);
        for (int i = 0; i < row.getLastCellNum(); i++)
        {
            Cell cell = row.getCell(i);
            if (cell != null) row.removeCell(cell);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(this.itemsFileId, updatedContent);

        this.deleteItemFromList(item.getShop(), item.getId(), this.shopItemsFileId);
    }

    public List<Item> getItems() throws IOException
    {
        List<Item> items = new ArrayList<>();

        ByteArrayInputStream inputStream = getInputStream(this.itemsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);

        for (Row row : sheet)
        {
            if (row.getRowNum() >= row_num)
                break;

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
        inputStream.close();

        return items;
    }
    public @NotNull Item getItem(double id) throws IOException
    {
        String name = "";
        String imageSource = "";
        double price = 0;
        double shop = 0;

        ByteArrayInputStream inputStream = getInputStream(this.itemsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
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
        inputStream.close();

        return new Item(id, name, imageSource, price, shop);
    }
    public List<Item> getFavouriteItems(double customer) throws IOException { return this.getSubItems(customer, this.favouriteItemsFileId); }
    public List<Item> getPurchasedItems(double customer) throws IOException { return this.getSubItems(customer, this.purchasedItemsFileId); }
    public List<Item> getShoppingItems(double customer) throws IOException { return this.getSubItems(customer, this.shoppingItemsFileId); }
    public void addFavouriteItem(double customer, double item) throws IOException { this.addItemToList(customer, item, this.favouriteItemsFileId); }
    public void addShoppingItem(double customer, double item) throws IOException { this.addItemToList(customer, item, this.shoppingItemsFileId); }
    public void addPurchasedItem(double customer, double item) throws IOException { this.addItemToList(customer, item, this.purchasedItemsFileId); }
    public void deleteFavouriteItem(double customer, double item) throws IOException { this.deleteItemFromList(customer, item, this.favouriteItemsFileId); }
    public void deleteShoppingItem(double customer, double item) throws IOException { this.deleteItemFromList(customer, item, this.shoppingItemsFileId); }
    public void deleteAllShoppingItems(double customer) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(this.shoppingItemsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);

        int row_num = (int) customer - 1;
        Row row = sheet.getRow(row_num);
        for (int i = 0; i < row.getLastCellNum(); i++)
        {
            Cell cell = row.getCell(i);
            if (cell != null) row.removeCell(cell);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(this.shoppingItemsFileId, updatedContent);
    }
    public List<Shop> getFavouriteShops(double customer) throws IOException
    {
        List<Shop> result = new ArrayList<>();

        ByteArrayInputStream inputStream = getInputStream(this.customerShopsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        Row row = sheet.getRow((int)customer - 1);

        Iterator<Cell> cellIterator = row.cellIterator();

        while (cellIterator.hasNext())
        {
            Cell cell = cellIterator.next();
            result.add(this.getShop(cell.getNumericCellValue()));
        }
        inputStream.close();

        return result;
    }
    public void addFavouriteShop(double customer, double shop) throws IOException { this.addItemToList(customer, shop, this.customerShopsFileId); }
    public void deleteFavouriteShop(double customer, double shop) throws IOException { this.deleteItemFromList(customer, shop, this.customerShopsFileId); }
    public Shop getShop(double id) throws IOException
    {
        if (id == 0) return null;

        String name = "";
        List<Item> items;
        String imageSource = "";
        Color textColor = Color.WHITE;
        double vendor = 0;

        ByteArrayInputStream inputStream = getInputStream(this.shopsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
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
        inputStream.close();

        items = getSubItems(id, this.shopItemsFileId);


        return new Shop(id, name, items, imageSource, textColor, vendor);
    }
    public Map<Double, String> getShops() throws IOException
    {
        Map<Double, String> shops = new HashMap<>();

        ByteArrayInputStream inputStream = getInputStream(this.shopsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);

        for (Row row : sheet)
        {
            if (row.getRowNum() >= row_num)
                break;

            double id = 0;
            String name = "";

            Iterator<Cell> cellIterator = row.cellIterator();
            int column = 0;
            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();
                if (column == ShopInfo.ID.getIndex()) id = cell.getNumericCellValue();
                else if (column == ShopInfo.NAME.getIndex()) name = cell.getStringCellValue();
                column++;
            }

            shops.put(id, name);
        }
        inputStream.close();

        return shops;
    }
    public double getShopId(double vendor) throws IOException
    {
        double id = 0;

        ByteArrayInputStream inputStream = getInputStream(this.shopsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);

        for (Row row : sheet)
        {
            if (row.getRowNum() >= row_num)
                break;

            if (row.getCell(ShopInfo.VENDOR.getIndex()).getNumericCellValue() == vendor)
            {
                id = row.getCell(ShopInfo.ID.getIndex()).getNumericCellValue();
                break;
            }
        }
        inputStream.close();

        return id;
    }
    public List<Order> getOrders(double customer) throws IOException
    {
        List<Order> orders = new ArrayList<>();

        double id = -1;
        OrderState state = OrderState.booked;
        double price = 0;

        ByteArrayInputStream inputStream = getInputStream(this.ordersFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);

        for (Row row : sheet)
        {
            if (row.getRowNum() >= row_num)
                break;

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
                List<OrderItem> items = this.getOrderItems(id);
                orders.add(new Order(id, state, price, customer));
                orders.get(orders.size() - 1).setItems(items);
            }
        }
        inputStream.close();

        return orders;
    }
    public List<Order> getSpecificOrders(String choice) throws IOException
    {
        List<Order> orders = new ArrayList<>();

        double id = -1;
        OrderState state = OrderState.booked;
        double price = 0;
        double customer = -1;

        ByteArrayInputStream inputStream = getInputStream(this.ordersFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);

        for (Row row : sheet)
        {
            Iterator<Cell> cellIterator = row.cellIterator();

            if (row.getRowNum() >= row_num)
                break;

            int column = 0;
            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();

                if (column == OrderInfo.ID.getIndex()) id = cell.getNumericCellValue();
                else if (column == OrderInfo.STATE.getIndex()) state = OrderState.valueOf(cell.getStringCellValue());
                else if (column == OrderInfo.PRICE.getIndex()) price = cell.getNumericCellValue();
                else if (column == OrderInfo.CUSTOMER.getIndex()) customer = cell.getNumericCellValue();

                column++;
            }

            if (Objects.equals(choice, "approved") && state == OrderState.approved)
            {
                List<OrderItem> items = this.getOrderItems(id);
                orders.add(new Order(id, state, price, customer));
                orders.get(orders.size() - 1).setItems(items);
            }
        }
        inputStream.close();

        return orders;
    }
    public void addOrder(@NotNull Order order) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(this.ordersFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);
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

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(this.ordersFileId, updatedContent);

        inputStream = getInputStream(this.orderItemsFileId);
        workbook = new XSSFWorkbook(inputStream);
        sheet = workbook.getSheetAt (0);

        for (OrderItem item : order.getItems())
        {
            row_num = sheet.getLastRowNum();
            row_num++;
            row = sheet.createRow(row_num);

            cell = row.createCell(OrderItemInfo.ORDER.getIndex());
            cell.setCellValue(order.getId());
            cell = row.createCell(OrderItemInfo.ITEM.getIndex());
            cell.setCellValue(item.getItem().getId());
            cell = row.createCell(OrderItemInfo.SHOP.getIndex());
            cell.setCellValue(this.getVendorId(item.getItem().getId()));
            cell = row.createCell(OrderItemInfo.STATE.getIndex());
            cell.setCellValue(order.getState().toString());
        }

        outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(this.orderItemsFileId, updatedContent);
    }
    public @NotNull List<OrderItem> getOrderItems(double order) throws IOException
    {
        List<OrderItem> items = new ArrayList<>();

        ByteArrayInputStream inputStream = getInputStream(this.orderItemsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);

        boolean was = false;
        for (Row row : sheet)
        {
            if (row.getRowNum() >= row_num)
                break;

            if (row.getCell(OrderItemInfo.ORDER.getIndex()).getNumericCellValue() == order)
            {
                // Item item = this.getItem(row.getCell(OrderItemInfo.ITEM.getIndex()).getNumericCellValue());
                Item item = new Item();
                item.setId(row.getCell(OrderItemInfo.ITEM.getIndex()).getNumericCellValue());
                double shop = row.getCell(OrderItemInfo.SHOP.getIndex()).getNumericCellValue();
                OrderState state = OrderState.valueOf(row.getCell(OrderItemInfo.STATE.getIndex()).getStringCellValue());

                items.add(new OrderItem(row.getRowNum() + 1, order, item, shop, state));
                was = true;
            }
            else if (was) break;
        }
        inputStream.close();

        return items;
    }
    public List<OrderItem> getVendorOrders(double vendor) throws IOException
    {
        List<OrderItem> items = new ArrayList<>();

        ByteArrayInputStream inputStream = getInputStream(this.orderItemsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        double shop = this.getShopId(vendor);
        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);

        for (Row row : sheet)
        {
            if (row.getRowNum() >= row_num)
                break;

            if (row.getCell(OrderItemInfo.SHOP.getIndex()).getNumericCellValue() == shop)
            {
                double order = row.getCell(OrderItemInfo.ORDER.getIndex()).getNumericCellValue();
                Item item = this.getItem(row.getCell(OrderItemInfo.ITEM.getIndex()).getNumericCellValue());
                OrderState state = OrderState.valueOf(row.getCell(OrderItemInfo.STATE.getIndex()).getStringCellValue());
                items.add(new OrderItem(row.getRowNum() + 1, order, item, shop, state));
            }
        }
        inputStream.close();

        return items;
    }
    public void changeOrderItemsState(double order, @NotNull OrderState state) throws IOException
    {
        double customer = 0;
        if (state == OrderState.finished)
        {
            ByteArrayInputStream inputStream = getInputStream(this.ordersFileId);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt (0);

            Row row = sheet.getRow((int) (order - 1));
            customer = row.getCell(OrderInfo.CUSTOMER.getIndex()).getNumericCellValue();

            inputStream.close();
        }

        ByteArrayInputStream inputStream = getInputStream(this.orderItemsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);

        boolean was = false;
        for (Row row : sheet)
        {
            if (row.getRowNum() >= row_num)
                break;

            if (row.getCell(OrderItemInfo.ORDER.getIndex()).getNumericCellValue() == order)
            {
                row.getCell(OrderItemInfo.STATE.getIndex()).setCellValue(state.toString());
                was = true;

                if (customer != 0)
                    this.addPurchasedItem(customer, row.getCell(OrderItemInfo.ITEM.getIndex()).getNumericCellValue());
            }
            else if (was) break;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(this.orderItemsFileId, updatedContent);
    }
    public void changeOrderState(double order, @NotNull OrderState state) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(this.ordersFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        Row row = sheet.getRow((int) (order - 1));
        row.getCell(OrderInfo.STATE.getIndex()).setCellValue(state.toString());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(this.ordersFileId, updatedContent);
    }
    public void changeOrderItemState(double orderItem, @NotNull OrderState state) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(this.orderItemsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        Row row = sheet.getRow((int) (orderItem - 1));
        row.getCell(OrderItemInfo.STATE.getIndex()).setCellValue(state.toString());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(this.orderItemsFileId, updatedContent);
    }
    public void addRequest(@NotNull Request request) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(this.requestsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);
        Row row = sheet.createRow(row_num);

        Cell cell = row.createCell(RequestInfo.SUBJECT.getIndex());
        cell.setCellValue(request.getSubject());
        cell = row.createCell(RequestInfo.MESSAGE.getIndex());
        cell.setCellValue(request.getMessage());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(this.requestsFileId, updatedContent);
    }
    public List<Request> getRequests() throws IOException
    {
        List<Request> requests = new ArrayList<>();

        ByteArrayInputStream inputStream = getInputStream(this.requestsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);

        for (Row row : sheet)
        {
            if (row.getRowNum() >= row_num)
                break;

            double id = row.getRowNum() + 1;
            String subject = "";
            String message = "";

            Iterator<Cell> cellIterator = row.cellIterator();
            int column = 0;
            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();

                if (column == RequestInfo.SUBJECT.getIndex()) subject = cell.getStringCellValue();
                else if (column == RequestInfo.MESSAGE.getIndex()) message = cell.getStringCellValue();

                column++;
            }

            requests.add(new Request(id, subject, message));
        }
        inputStream.close();

        return requests;
    }
    public void deleteRequest(@NotNull Request request) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(this.requestsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);

        int row_num = (int) request.getId() - 1;
        Row row = sheet.getRow(row_num);
        for (int i = 0; i < row.getLastCellNum(); i++)
        {
            Cell cell = row.getCell(i);
            if (cell != null) row.removeCell(cell);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(this.requestsFileId, updatedContent);
    }
    public boolean editRole(double user, AccessType accessType) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(this.userAccountsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int allRow = this.getLastRowNum(sheet.getLastRowNum(), sheet);
        if (user == 0 || user > allRow) return false;

        int row_num = (int) user - 1;
        Row row = sheet.getRow(row_num);

        Cell cell = row.getCell(UserAccount.ACCESSTYPE.getIndex());
        cell.setCellValue(accessType.toString());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(this.userAccountsFileId, updatedContent);

        return true;
    }

    private boolean isFoundUser(String username, String password) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(this.userAccountsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);

        for (Row row : sheet)
        {
            if (row.getRowNum() >= row_num)
                break;

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
                inputStream.close();
                return true;
            }
        }
        inputStream.close();
        return false;
    }
    private @Nullable List<Item> getSubItems(double id, String fileId) throws IOException
    {
        List<Double> subItemsIds = new ArrayList<>();
        List<Item> subItems = new ArrayList<>();

        ByteArrayInputStream inputStream = getInputStream(fileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        Row row = sheet.getRow((int)id - 1);

        if (row == null) return null;

        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext())
        {
            Cell cell = cellIterator.next();
            subItemsIds.add(cell.getNumericCellValue());
        }
        inputStream.close();

        inputStream = getInputStream(this.itemsFileId);
        workbook = new XSSFWorkbook(inputStream);
        sheet = workbook.getSheetAt (0);

        for (double element: subItemsIds)
            subItems.add(this.getItem(element, sheet));

        inputStream.close();

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
    private void addItemToList(double user, double item, String fileId) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(fileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = (int)user - 1;
        Row row = sheet.getRow(row_num);

        if (row == null || row.getLastCellNum() == -1)
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

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(fileId, updatedContent);
    }
    private void deleteItemFromList(double user, double item, String fileId) throws IOException
    {
        ByteArrayInputStream inputStream = getInputStream(fileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);

        int row_num = (int) user - 1;
        Row row = sheet.getRow(row_num);

        Iterator<Cell> cellIterator = row.cellIterator();
        int column = 0;
        while (cellIterator.hasNext())
        {
            Cell cell = cellIterator.next();
            if (item == cell.getNumericCellValue())
            {
                column = cell.getColumnIndex();
                break;
            }
        }

        if (row.getLastCellNum() == column + 1)  row.removeCell(row.getCell(column));
        else
        {
            row.removeCell(row.getCell(column));
            row.shiftCellsLeft(column + 1, row.getLastCellNum() - 1, 1);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        inputStream.close();
        outputStream.close();

        this.UpdateContent(fileId, updatedContent);
    }
    private void UpdateContent(String fileId, byte[] updatedFileBytes)
    {
        ByteArrayContent content = new ByteArrayContent(null, updatedFileBytes);

        try
        {
            Drive.Files.Update request = DriveHandler.getService().files().update(fileId, null, content);
            request.getMediaHttpUploader().setDirectUploadEnabled(true);
            request.execute();
        }
        catch(IOException | GeneralSecurityException e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }
    private double getVendorId(double item) throws IOException
    {
        double shop = 0;

        ByteArrayInputStream inputStream = getInputStream(this.shopItemsFileId);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = getLastRowNum(sheet.getLastRowNum(), sheet);

        boolean was = false;
        for (Row row : sheet)
        {
            if (row.getRowNum() >= row_num)
                break;

            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();
                if (cell.getNumericCellValue() == item)
                {
                    shop = row.getRowNum() + 1;
                    was = true;
                    break;
                }
            }

            if (was) break;
        }

        double id = 0;

        inputStream = getInputStream(this.shopsFileId);
        workbook = new XSSFWorkbook(inputStream);
        sheet = workbook.getSheetAt (0);

        for (Row row : sheet)
        {
            if (row.getRowNum() == row_num)
                break;

            if (row.getCell(ShopInfo.ID.getIndex()).getNumericCellValue() == shop)
            {
                id = row.getCell(ShopInfo.VENDOR.getIndex()).getNumericCellValue();
                break;
            }
        }
        inputStream.close();

        return id;
    }
    private int getLastRowNum(int row_num,  XSSFSheet sheet)
    {
        for (int i = row_num; i >= 0; i--)
        {
            if(sheet.getRow(i) == null || sheet.getRow(i).getCell(0) == null)
                continue;
            else
                return i + 1;
        }
        return 0;
    }
}
