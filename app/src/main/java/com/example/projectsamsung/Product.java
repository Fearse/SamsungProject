package com.example.projectsamsung;

import java.util.ArrayList;

public class Product {
    public String name;
    public String image;
    public String timeString;
    public ArrayList<String> steps;
    public String code;
    public int timeInt;
    public String category;
    public ArrayList<String> ings;
    Product(){};
    Product(String name,String image,String time,ArrayList<String>  steps,String code,String category){
        this.name=name;
        this.code=code;
        this.image=image;
        this.steps=steps;
        this.timeString=time;
        this.category=category;
    }
    String getCategory()
    {
        return category;
    }
    void setCategory(String category)
    {
        this.category=category;
    }
    ArrayList<String>  getRecipe(){return steps;}
    String getName()
    {
        return name;
    }
    String getImage()
    {
        return image;
    }
    String getTime()
    {
        return timeString;
    }
    void setIngs(ArrayList<String> ings)
    {
        this.ings=ings;
    }
    ArrayList<String>getIngs(){return ings;}
    void setTime(int time)
    {
        timeInt=time;
    }
    int getTimeInt()
    {
        return timeInt;
    }
    String getCode()
    {
        return code;
    }
}
