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
