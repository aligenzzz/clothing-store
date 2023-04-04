package com.example.test.enums;

public enum ItemInfo
{
    ID(0), NAME(2), IMAGESOURCE(1),
    PRICE(3), SHOP(4);

    private final int index;
    ItemInfo(int index) { this.index = index; }
    public final int getIndex() { return index; }
}
