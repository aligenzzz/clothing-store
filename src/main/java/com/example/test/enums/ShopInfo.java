package com.example.test.enums;

public enum ShopInfo
{
    ID(0), NAME(1), IMAGESOURCE(2), TEXTCOLOR(3), VENDOR(4);
    private final int index;
    ShopInfo(int index) { this.index = index; }
    public final int getIndex() { return index; }
}
