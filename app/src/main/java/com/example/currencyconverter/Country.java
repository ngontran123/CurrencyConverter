package com.example.currencyconverter;
import android.view.*;
import android.os.*;

import java.util.ArrayList;

public class Country {
    public ArrayList<String> titles=new ArrayList<>();
    public  ArrayList<String> descriptions=new ArrayList<>();
    public String category;
    public ArrayList<String> links=new ArrayList<>();
    public ArrayList<String> getTitles() {
        return titles;
    }
    public void setTitles(ArrayList<String> titles)
    {
        this.titles=titles;
    }
    public ArrayList<String>  getDescriptions()
    {
        return descriptions;
    }
    public void setDescriptions(ArrayList<String> descriptions) {
        this.descriptions = descriptions;
    }
    public ArrayList<String> getLinks()
    {
        return links;
    }
    public void setLinks(ArrayList<String> links)
    {
        this.links=links;
    }
    public String getCategory()
    {
        return category;
    }
    public void setCategory(String category)
    {
        this.category=category;
    }
}
