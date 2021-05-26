package com.example.projectsamsung;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

@Entity
@TypeConverters({com.example.projectsamsung.ListConverter.class})
public class Product {
    public String name;
    @PrimaryKey
    @NonNull
    public String image;
    public String timeString;
    public ArrayList<String> steps;
    public String code;
    public int timeInt;
    public String category;
    public ArrayList<String> ings;
    public Product(){};
    public Product(String name,String image,String timeString,ArrayList<String>  steps,String code,String category){
        this.name=name;
        this.code=code;
        this.image=image;
        this.steps=steps;
        this.timeString=timeString;
        this.category=category;
    }
    public String getCategory()
    {
        return category;
    }
    public ArrayList<String>  getRecipe(){return steps;}
    public String getName()
    {
        return name;
    }
    public String getImage()
    {
        return image;
    }
    public String getTimeString()
    {
        return timeString;
    }
    public void setIngs(ArrayList<String> ings)
    {
        this.ings=ings;
    }
    public ArrayList<String>getIngs(){return ings;}
    public void setTimeInt(int timeInt)
    {
        this.timeInt=timeInt;
    }
    public int getTimeInt()
    {
        return timeInt;
    }
    public String getCode()
    {
        return code;
    }
}
