package com.example.test.enums;

public enum UserAccount
{
    ID(0), USERNAME(1), PASSWORD(2),
    EMAIL(3), FIRSTNAME(4), LASTNAME(5), ACCESSTYPE(6);

    private final int index;
    UserAccount(int index) { this.index = index; }
    public final int getIndex() { return index; }
}
