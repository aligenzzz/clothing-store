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
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DatabaseConnector
{
    static String accessToken = "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";
    static String repositoryOwner = "aligenzzz";
    static String repositoryName = "clothing-store-database";
    static GHRepository repository;
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
    private GHContent userAccountsFileContent;
    private GHContent itemsFileContent;
    private GHContent favouriteItemsFileContent;
    private GHContent purchasedItemsFileContent;
    private GHContent shoppingItemsFileContent;
    private GHContent shopsFileContent;
    private GHContent shopItemsFileContent;
    private GHContent customerShopsFileContent;
    private GHContent ordersFileContent;
    private GHContent orderItemsFileContent;
    private GHContent requestsFileContent;
    private DatabaseConnector() throws IOException
    {
        GitHub github = new GitHubBuilder().withOAuthToken(accessToken).build();
        repository = github.getRepository(repositoryOwner + "/" + repositoryName);

        Update();
    }

    private void Update() throws IOException
    {
        this.userAccountsFileContent = repository.getFileContent(Constants.USERACCOUNTS);
        this.itemsFileContent = repository.getFileContent(Constants.ITEMS);
        this.favouriteItemsFileContent = repository.getFileContent(Constants.FAVOURITEITEMS);
        this.purchasedItemsFileContent = repository.getFileContent(Constants.PURCHASEDITEMS);
        this.shoppingItemsFileContent = repository.getFileContent(Constants.SHOPPINGITEMS);
        this.shopsFileContent = repository.getFileContent(Constants.SHOPS);
        this.shopItemsFileContent = repository.getFileContent(Constants.SHOPITEMS);
        this.customerShopsFileContent = repository.getFileContent(Constants.CUSTOMERSHOPS);
        this.ordersFileContent = repository.getFileContent(Constants.ORDERS);
        this.orderItemsFileContent = repository.getFileContent(Constants.ORDERITEMS);
        this.requestsFileContent = repository.getFileContent(Constants.REQUESTS);
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

        InputStream inputStream = userAccountsFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
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

        InputStream inputStream = userAccountsFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
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
        inputStream.close();

        return users;
    }
    public void AddUser(@NotNull User user) throws IOException
    {
        InputStream inputStream = userAccountsFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
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

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        userAccountsFileContent.update(updatedContent, "Update");
        Update();

        inputStream.close();
        outputStream.close();
    }
    public boolean isFoundUser(String parameter, UserAccount property) throws IOException
    {
        InputStream inputStream = userAccountsFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
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
        inputStream.close();
        return false;
    }
    public List<Item> getItems() throws IOException
    {
        List<Item> items = new ArrayList<>();

        InputStream inputStream = itemsFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
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
        inputStream.close();

        return items;
    }
    public @NotNull Item getItem(double id) throws IOException
    {
        String name = "";
        String imageSource = "";
        double price = 0;
        double shop = 0;

        InputStream inputStream = itemsFileContent.read();
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
    public List<Item> getFavouriteItems(double customer) throws IOException { return this.getSubItems(customer, this.favouriteItemsFileContent); }
    public List<Item> getPurchasedItems(double customer) throws IOException { return this.getSubItems(customer, this.purchasedItemsFileContent); }
    public List<Item> getShoppingItems(double customer) throws IOException { return this.getSubItems(customer, this.shoppingItemsFileContent); }
    public void addFavouriteItem(double customer, double item) throws IOException { this.addItemToList(customer, item, this.favouriteItemsFileContent); }
    public void addPurchasedItem(double customer, double item) throws IOException { this.addItemToList(customer, item, this.purchasedItemsFileContent); }
    public void addShoppingItem(double customer, double item) throws IOException { this.addItemToList(customer, item, this.shoppingItemsFileContent); }
    public void deleteFavouriteItem(double customer, double item) throws IOException { this.deleteItemFromList(customer, item, this.favouriteItemsFileContent); }
    public void deleteShoppingItem(double customer, double item) throws IOException { this.deleteItemFromList(customer, item, this.shoppingItemsFileContent); }
    public void deleteAllShoppingItems(double customer) throws IOException
    {
        InputStream inputStream = shoppingItemsFileContent.read();
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

        shoppingItemsFileContent.update(updatedContent, "Update");
        Update();

        inputStream.close();
        outputStream.close();
    }
    public List<Shop> getFavouriteShops(double customer) throws IOException
    {
        List<Shop> result = new ArrayList<>();

        InputStream inputStream = customerShopsFileContent.read();
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
    public void deleteFavouriteShop(double customer, double shop) throws IOException { this.deleteItemFromList(customer, shop, this.customerShopsFileContent); }
    public Shop getShop(double id) throws IOException
    {
        String name = "";
        List<Item> items;
        String imageSource = "";
        Color textColor = Color.WHITE;
        double vendor = 0;

        InputStream inputStream = shopsFileContent.read();
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

        items = getSubItems(id, this.shopItemsFileContent);

        return new Shop(id, name, items, imageSource, textColor, vendor);
    }
    public double getShopId(double vendor) throws IOException
    {
        double id = 0;

        InputStream inputStream = shopsFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        for (Row row : sheet)
        {
            if (row.getCell(ShopInfo.VENDOR.getIndex()).getNumericCellValue() == vendor)
            {
                id = row.getCell(ShopInfo.ID.getIndex()).getNumericCellValue();
                break;
            }
        }
        inputStream.close();

        return id;
    }

    private double getVendorId(double item) throws IOException
    {
        double shop = 0;

        InputStream inputStream = shopItemsFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        boolean was = false;
        for (Row row : sheet)
        {
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

        inputStream = shopsFileContent.read();
        workbook = new XSSFWorkbook(inputStream);
        sheet = workbook.getSheetAt (0);

        for (Row row : sheet)
        {
            if (row.getCell(ShopInfo.ID.getIndex()).getNumericCellValue() == shop)
            {
                id = row.getCell(ShopInfo.VENDOR.getIndex()).getNumericCellValue();
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

        InputStream inputStream = ordersFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
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
        InputStream inputStream = ordersFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
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

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        ordersFileContent.update(updatedContent, "Update");
        Update();

        inputStream.close();
        outputStream.close();

        inputStream = orderItemsFileContent.read();
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

        orderItemsFileContent.update(updatedContent, "Update");
        Update();

        inputStream.close();
        outputStream.close();
    }

    public @NotNull List<OrderItem> getOrderItems(double order) throws IOException
    {
        List<OrderItem> items = new ArrayList<>();

        InputStream inputStream = orderItemsFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        boolean was = false;
        for (Row row : sheet)
        {
            if (row.getCell(OrderItemInfo.ORDER.getIndex()).getNumericCellValue() == order)
            {
                Item item = this.getItem(row.getCell(OrderItemInfo.ITEM.getIndex()).getNumericCellValue());
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

        InputStream inputStream = orderItemsFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        double shop = this.getShopId(vendor);

        for (Row row : sheet)
        {
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

    public List<OrderItem> getOrderItems() throws IOException
    {
        List<OrderItem> items = new ArrayList<>();

        InputStream inputStream = orderItemsFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        for (Row row : sheet)
        {
            double shop = row.getCell(OrderItemInfo.SHOP.getIndex()).getNumericCellValue();
            double order = row.getCell(OrderItemInfo.ORDER.getIndex()).getNumericCellValue();
            Item item = this.getItem(row.getCell(OrderItemInfo.ITEM.getIndex()).getNumericCellValue());
            OrderState state = OrderState.valueOf(row.getCell(OrderItemInfo.STATE.getIndex()).getStringCellValue());
            items.add(new OrderItem(row.getRowNum() + 1, order, item, shop, state));
        }
        inputStream.close();

        return items;
    }

    public void changeOrderState(double orderItem, @NotNull OrderState state) throws IOException
    {
        InputStream inputStream = orderItemsFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        Row row = sheet.getRow((int) (orderItem - 1));
        row.getCell(OrderItemInfo.STATE.getIndex()).setCellValue(state.toString());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        orderItemsFileContent.update(updatedContent, "Update");
        Update();

        inputStream.close();
        outputStream.close();
    }

    public void addRequest(@NotNull Request request) throws IOException
    {
        InputStream inputStream = requestsFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        int row_num = sheet.getLastRowNum();
        Row row = sheet.createRow(row_num + 1);

        Cell cell = row.createCell(RequestInfo.SUBJECT.getIndex());
        cell.setCellValue(request.getSubject());
        cell = row.createCell(RequestInfo.MESSAGE.getIndex());
        cell.setCellValue(request.getMessage());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] updatedContent = outputStream.toByteArray();

        requestsFileContent.update(updatedContent, "Update");
        Update();

        inputStream.close();
        outputStream.close();
    }

    public List<Request> getRequests() throws IOException
    {
        List<Request> requests = new ArrayList<>();

        InputStream inputStream = requestsFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt (0);

        for (Row row : sheet)
        {
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

    private boolean isFoundUser(String username, String password) throws IOException
    {
        InputStream inputStream = userAccountsFileContent.read();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
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
                inputStream.close();
                return true;
            }
        }
        inputStream.close();
        return false;
    }
    private @Nullable List<Item> getSubItems(double id, @NotNull GHContent content) throws IOException
    {
        List<Double> subItemsIds = new ArrayList<>();
        List<Item> subItems = new ArrayList<>();

        InputStream inputStream = content.read();
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

        inputStream = itemsFileContent.read();
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
    private void addItemToList(double user, double item, @NotNull GHContent content) throws IOException
    {
        InputStream inputStream = content.read();
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

        content.update(updatedContent, "Add item");
        Update();

        inputStream.close();
        outputStream.close();
    }

    private void deleteItemFromList(double user, double item, @NotNull GHContent content) throws IOException
    {
        InputStream inputStream = content.read();
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

        content.update(updatedContent, "Update");
        Update();

        inputStream.close();
        outputStream.close();
    }
}
