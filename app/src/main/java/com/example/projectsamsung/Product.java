package com.example.projectsamsung;

public class Product {
    public String id;
    public String name;
    public String image;
    public String time;
    public String steps;
    public String code;
    Product(){};
    Product(String name,String image,String time,String steps,String code){
        this.name=name;
        this.code=code;
        this.image=image;
        this.steps=steps;
        this.time=time;
    }
    String getRecipe(){return steps;}
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
        return time;
    }
    String getCode()
    {
        return code;
    }
    String getSteps()
    {
        return steps;
    }
}
