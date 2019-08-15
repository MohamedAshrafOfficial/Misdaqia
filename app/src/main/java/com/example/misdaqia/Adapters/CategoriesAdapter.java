package com.example.misdaqia.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.misdaqia.Helper.MainFontTextview;
import com.example.misdaqia.Model.Category;
import com.example.misdaqia.R;
import com.example.misdaqia.View.CategoryItemDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {

    ArrayList<Category> categories;
    Context context;
    Intent intent;

    public CategoriesAdapter(ArrayList<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.main_sold_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {

        holder.mainprojectname.setText(categories.get(i).getVendor());
        holder.itemCategoryPrice.setText(categories.get(i).getPrice()+"");
        holder.fe2aType.setText(categories.get(i).getType());
        holder.categoryDate.setText(categories.get(i).getCreated_at());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, CategoryItemDetailsActivity.class);
                intent.putExtra("category", categories.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView        imageHeader;
        MainFontTextview mainprojectname;
        MainFontTextview itemCategoryPrice;
        MainFontTextview mzadType;
        MainFontTextview fe2aType;
        MainFontTextview mzadLocation;
        MainFontTextview categoryDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mainprojectname = itemView.findViewById(R.id.mainprojectname);
            imageHeader = itemView.findViewById(R.id.imageHeader);
            itemCategoryPrice = itemView.findViewById(R.id.itemCategoryPrice);
            mzadType = itemView.findViewById(R.id.mzadType);
            fe2aType = itemView.findViewById(R.id.fe2aType);
            mzadLocation = itemView.findViewById(R.id.mzadLocation);
            categoryDate = itemView.findViewById(R.id.categoryDate);
        }
    }
}
