package com.example.test.enums;

public enum OrderInfo
{
    ID(0), STATE(1), PRICE(2), CUSTOMER(3);
    private final int index;
    OrderInfo(int index) { this.index = index; }
    public final int getIndex() { return index; }
}
