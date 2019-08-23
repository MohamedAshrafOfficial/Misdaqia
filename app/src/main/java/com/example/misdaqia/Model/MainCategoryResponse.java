package com.example.misdaqia.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainCategoryResponse {

    @SerializedName("count of rows")
    @Expose
    private String list_count;

    @SerializedName("data")
    @Expose
    private List<MainCategory> MainCategory;

    public String getList_count() {
        return list_count;
    }

    public void setList_count(String list_count) {
        this.list_count = list_count;
    }

    public List<MainCategory> getMainCategory() {
        return MainCategory;
    }

    public void setMainCategory(List<MainCategory> mainCategory) {
        MainCategory = mainCategory;
    }
}
