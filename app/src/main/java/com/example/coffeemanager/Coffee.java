package com.example.coffeemanager;

public class Coffee {
    int resourceId;
    String name;
    Customization customization;

    public Coffee()
    {
        customization = new Customization();
    }

    public Coffee(Customization customization)
    {
        this.customization = customization;
    }

    public int getCostInVND()
    {
        return 100000;
    }
}
