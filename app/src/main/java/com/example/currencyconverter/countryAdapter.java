package com.example.currencyconverter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
public class countryAdapter extends BaseAdapter {
    Context context;
    List<Country> countryList;
    public countryAdapter(Context context,List<Country> countryList)
    {
        this.context=context;
        this.countryList=countryList;
    }
    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public Country getItem(int position) {
        return countryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
