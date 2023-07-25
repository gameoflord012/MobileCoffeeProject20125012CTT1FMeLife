package com.example.coffeemanager;

public class DarkCoffee extends Coffee{
    public DarkCoffee()
    {
        name = "Dark Coffee";
        resourceId = R.drawable.coffee_1;
    }
    @Override
    public int getCostInVND() {
        return 10000;
    }
}
