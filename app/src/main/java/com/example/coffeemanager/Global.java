package com.example.coffeemanager;

import java.util.ArrayList;
import java.util.List;

public class Global {
    static List<Coffee> coffeeList = new ArrayList<Coffee>()
    {
        {
            add(new DarkCoffee());
            add(new Coffee()
            {
                {
                    name = "Milk Coffee";
                    resourceId = R.drawable.milk_coffee;
                }
            });
            add(new Coffee()
            {
                {
                    name = "Dout";
                    resourceId = R.drawable.donut;
                }
            });
            add(new Coffee()
            {
                {
                    name = "Sushi";
                    resourceId = R.drawable.sushi;
                }
            });
        }
    };
}
