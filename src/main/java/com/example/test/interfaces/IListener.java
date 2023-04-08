package com.example.test.interfaces;

import com.example.test.entities.Item;

import java.io.IOException;

public interface IListener
{
    void onClickListener(Item item) throws IOException;
}
