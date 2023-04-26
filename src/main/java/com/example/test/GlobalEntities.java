package com.example.test;

import com.example.test.entities.Order;
import com.example.test.entities.Shop;
import com.example.test.enums.CustomerChoice;
import com.example.test.interfaces.User;

public class GlobalEntities
{
    public static User USER;
    public static Shop SHOP;
    public static Order ORDER;
    public static CustomerChoice CHOICE;

    public static boolean EXPANDEDORDER = false;
}
