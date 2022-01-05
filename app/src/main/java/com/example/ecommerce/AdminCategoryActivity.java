package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {


    private ImageView tShirts, sportsTShirts, femaleDresses, sweaters;
    private ImageView glasses, hatCaps, walletsBagsPurses, shoes;
    private ImageView headphones, laptops, watches, mobilePhones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        initializeVariables();

        tShirts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "tShirts");
            startActivity(intent);
        });


        sportsTShirts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Sports tShirts");
            startActivity(intent);
        });

        femaleDresses.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Female Dresses");
            startActivity(intent);
        });

        sweaters.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Sweathers");
            startActivity(intent);
        });

        glasses.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Glasses");
            startActivity(intent);
        });

        hatCaps.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Hats Caps");
            startActivity(intent);
        });

        walletsBagsPurses.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Wallets Bags Purses");
            startActivity(intent);
        });

        shoes.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Shoes");
            startActivity(intent);
        });

        headphones.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Headphones");
            startActivity(intent);
        });

        laptops.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Laptops");
            startActivity(intent);
        });

        watches.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Watches");
            startActivity(intent);
        });

        mobilePhones.setOnClickListener(v -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Mobile Phones");
            startActivity(intent);
        });


    }

    private void initializeVariables() {
        tShirts = (ImageView) findViewById(R.id.t_shirts);
        sportsTShirts = (ImageView) findViewById(R.id.sport_t_shirts);
        femaleDresses = (ImageView) findViewById(R.id.female_dress);
        sweaters = (ImageView) findViewById(R.id.sweaters);
        glasses = (ImageView) findViewById(R.id.glasses);
        hatCaps = (ImageView) findViewById(R.id.hats_caps);
        walletsBagsPurses = (ImageView) findViewById(R.id.purses_bags_wallets);
        shoes = (ImageView) findViewById(R.id.shoes);
        headphones = (ImageView) findViewById(R.id.headphones);
        laptops = (ImageView) findViewById(R.id.laptops_pc);
        watches = (ImageView) findViewById(R.id.watches);
        mobilePhones = (ImageView) findViewById(R.id.mobilePhones);
    }
}