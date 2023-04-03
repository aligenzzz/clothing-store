package com.example.test;

import com.example.test.entities.Item;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class DatabaseConnector
{
    private final String userAccountsFileName;
    private final String itemsFileName;
    public DatabaseConnector()
    {
        userAccountsFileName = Constants.USERACCOUNTS;
        itemsFileName = Constants.ITEMS;
    }
    public boolean isFoundUser(String username, String password) throws IOException
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

                    if (column == 1 && data.equals(username)) result1 = true;
                    else if (column == 2 && result1 && data.equals(password)) result2 = true;

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
    public void AddUser(String username, String email, String firstname, String lastname, String password)
    {
        try
        {
            FileInputStream file = new FileInputStream(userAccountsFileName);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt (0);

            int row_num = sheet.getLastRowNum();
            System.out.println(row_num);
            row_num++;
            Row row = sheet.createRow(row_num);
            row_num++;

            Cell cell = row.createCell(0);
            cell.setCellValue(row_num);
            cell = row.createCell(1);
            cell.setCellValue(username);
            cell = row.createCell(2);
            cell.setCellValue(password);
            cell = row.createCell(3);
            cell.setCellValue(email);
            cell = row.createCell(4);
            cell.setCellValue(firstname);
            cell = row.createCell(5);
            cell.setCellValue(lastname);

            FileOutputStream out = new FileOutputStream(userAccountsFileName);
            workbook.write(out);
            out.close();
            file.close();
        }
        catch (Exception exception) { exception.printStackTrace(); }
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
                String image_source = "";
                String name = "";
                String price = "";
                String shop = "";

                Iterator<Cell> cellIterator = row.cellIterator();
                int column = 0;
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    String data;
                    if(cell.getCellType() == CellType.STRING) data = cell.getStringCellValue();
                    else data = Double.toString(cell.getNumericCellValue());
                    switch (column)
                    {
                        case 1 -> image_source = data;
                        case 2 -> name = data;
                        case 3 -> price = data;
                        case 4 -> shop = data;
                    }
                    column++;
                }
                itemList.add(new Item(name, image_source, price, shop));
            }
            file.close();
        }
        catch (Exception exception) { exception.printStackTrace(); }
    }
}



