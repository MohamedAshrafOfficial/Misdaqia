package com.example.misdaqia.View;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.misdaqia.Helper.HeaderFontTextview;
import com.example.misdaqia.Helper.MainFontButton;
import com.example.misdaqia.Helper.MainFontTextview;
import com.example.misdaqia.Model.Category;
import com.example.misdaqia.R;

public class CategoryItemDetailsActivity extends AppCompatActivity {

    HeaderFontTextview itemName;
    MainFontTextview sellerName;
    MainFontTextview priceItem;
    MainFontTextview itemType;
    MainFontTextview createdAt;
    MainFontTextview itemYear;
    MainFontTextview itemColor;
    MainFontTextview itemModel;
    MainFontButton btnMozayda;
    MainFontButton btnBuy;
    Dialog dialog;
    Category currentCategory;
    private static final String TAG = "CategoryItemDetailsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item_details);

        initViews();
        currentCategory = (Category) getIntent().getSerializableExtra("category");
        handleViews();
        Log.d(TAG, "onCreate: " + currentCategory.getPrice());
    }

    private void initViews(){
        itemName = findViewById(R.id.itemName);
        sellerName  = findViewById(R.id.sellerName);
        priceItem = findViewById(R.id.priceItem);
        itemType = findViewById(R.id.itemType);
        createdAt = findViewById(R.id.createdAt);
        itemYear = findViewById(R.id.itemYear);
        itemModel = findViewById(R.id.itemModel);
        itemColor = findViewById(R.id.itemColor);
        btnMozayda = findViewById(R.id.btnMozayda);
        btnBuy = findViewById(R.id.btnBuy);
    }

    private void handleViews(){
        itemName.setText(currentCategory.getModel());
        sellerName.setText(currentCategory.getVendor());
        priceItem.setText(currentCategory.getPrice()+"");
        itemType.setText(currentCategory.getType());
        itemYear.setText(currentCategory.getYear()+"");
        itemModel.setText(currentCategory.getModel());
        itemColor.setText(currentCategory.getColor());
        createdAt.setText(currentCategory.getCreated_at());
        btnMozayda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(CategoryItemDetailsActivity.this);
                dialog.setContentView(R.layout.mozayda_card);
                dialog.show();
            }
        });
    }
}