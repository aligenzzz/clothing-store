package com.example.test.enums;

public enum OrderItemInfo
{
    ORDER(0), ITEM(1), SHOP(2), STATE(3);
    private final int index;
    OrderItemInfo(int index) { this.index = index; }
    public final int getIndex() { return index; }
}
