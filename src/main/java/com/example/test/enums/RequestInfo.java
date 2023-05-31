package com.example.test.enums;

public enum RequestInfo
{
    SUBJECT(0), MESSAGE(1);
    private final int index;
    RequestInfo(int index) { this.index = index; }
    public final int getIndex() { return index; }
}
