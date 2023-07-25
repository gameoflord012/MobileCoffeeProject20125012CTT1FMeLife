package com.example.coffeemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Coffee> coffees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the JSON string `from SharedPreferences
        SharedPreferences sharedPref;
        String personJson;

        // Convert the JSON string back to the object using Gson

        Gson gson = new Gson();
        try {
            sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            personJson = sharedPref.getString("person", "");

            coffees = gson.fromJson(personJson, TypeToken.getParameterized(List.class, Coffee.class).getType());
        }
        catch (JsonSyntaxException | NullPointerException e)
        {
        }

        if(coffees == null)
        {
            coffees = new ArrayList<>();
        }

        coffeeChooser();
    }

    @SuppressLint({"UseSwitchCompatOrMaterialCode", "UseCompatLoadingForDrawables"})
    protected void coffeeCustomizationView(Coffee coffee)
    {
        setContentView(R.layout.coffee_customization);
        initViewContext(new Runnable() {
            @Override
            public void run() {
                coffeeChooser();
            }
        });

        ImageView img = findViewById(R.id.coffeeCusDis);
        img.setImageDrawable(ContextCompat.getDrawable(this, coffee.resourceId));

        TextView textView = findViewById(R.id.coffeeCusName);
        textView.setText(coffee.name);

        Spinner spinner = findViewById(R.id.sizeSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                coffee.customization.size = spinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Switch ice = findViewById(R.id.iceSwitcher);
        ice.setOnCheckedChangeListener((compoundButton, b) ->
        {
            coffee.customization.ice = b;
        });

        Button button = (Button)findViewById(R.id.cusApplyButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coffees.add(coffee);
                coffeeOrder();
            }
        });

        initViewContext(new Runnable() {
            @Override
            public void run() {
                coffeeChooser();
            }
        });
    }

    void coffeeChooser()
    {
        setContentView(R.layout.activity_main);
        initViewContext(null);

        ViewGroup coffeeList = findViewById(R.id.coffeeList);

        int cnt = 0;
        for(int i = 0; i < coffeeList.getChildCount(); i++) {
            LinearLayout lala = (LinearLayout) coffeeList.getChildAt(i);

            for (int j = 0; j < lala.getChildCount(); j++, cnt++)
            {
                ImageButton imgBtn = (ImageButton)((ViewGroup)lala.getChildAt(j)).getChildAt(0);
                TextView txt = (TextView)((ViewGroup)lala.getChildAt(j)).getChildAt(1);

                if(cnt < Global.coffeeList.size())
                {
                    Coffee coffee = Global.coffeeList.get(cnt);
                    imgBtn.setImageDrawable(ContextCompat.getDrawable(this, coffee.resourceId));
                    txt.setText(coffee.name);

                    int finalI = cnt;
                    imgBtn.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {
                            coffeeCustomizationView(coffee);
                        }
                    });
                }
            }
        }
    }

    void coffeeOrder()
    {
        setContentView(R.layout.order_view);
        initViewContext(null);

        LinearLayout parent = findViewById(R.id.orderContainerLayout);

        for(int i = 0; i < coffees.size(); i++)
        {
            Coffee coffee = coffees.get(i);

            LinearLayout newLinearLayout = new LinearLayout(this);
            newLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

            parent.addView(newLinearLayout);

            newLinearLayout.addView(createTextView(Integer.toString(i)));
            newLinearLayout.addView(createTextView(coffee.name));
            newLinearLayout.addView(createTextView(Integer.toString(1)));
            newLinearLayout.addView(createTextView(Integer.toString(coffee.getCostInVND())));
        }
    }

    void coffeeProfileView()
    {
        setContentView(R.layout.profile_view);
        initViewContext(null);
    }

    TextView createTextView(String text)
    {
        TextView newTextView = new TextView(this);
        newTextView.setText(text);
        newTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
        ));

        return newTextView;
    }

    void initViewContext(Runnable goBack)
    {
        Button butt = findViewById(R.id.goBackButton);
        if(butt != null)
        {
            butt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(goBack != null)
                        goBack.run();
                }
            });
        }

        Button buttonMenu = findViewById(R.id.goMenuButton);
        Button buttonOrder = findViewById(R.id.goOrderButton);
        Button buttonProfile = findViewById(R.id.goProfileButton);

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coffeeChooser();
            }
        });

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coffeeOrder();
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coffeeProfileView();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Gson gson = new Gson();
        String personJson = gson.toJson(coffees, TypeToken.getParameterized(List.class, Coffee.class).getType());

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("person", personJson);
        editor.apply();
    }
}