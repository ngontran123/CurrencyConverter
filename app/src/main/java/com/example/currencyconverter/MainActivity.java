package com.example.currencyconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.*;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();
    EditText res;
    ArrayList<String>t=new ArrayList<>();
    String url = "https://usd.fxexchangerate.com/rss.xml";
    Handler messageHandler=new Handler();
    EditText amountInput;
    Spinner from;
    Spinner to;
    Button convert;
    Button swapButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         res = (EditText) this.findViewById(R.id.converted);
         amountInput=(EditText)this.findViewById(R.id.amountInput);
         from=(Spinner)this.findViewById(R.id.fromCurrency);
         to=(Spinner)this.findViewById(R.id.toCurrency);
         convert=(Button)this.findViewById(R.id.convertButton);
         amountInput.setText("1");
         swapButton=(Button)this.findViewById(R.id.swapButton);
       getCountry(url);
     }
     public float convertAmount(int value,float from,float to)
     {
         float t1=value/from;
         float t2=t1*to;
         DecimalFormat df=new DecimalFormat();
         df.setMaximumFractionDigits(2);
         float t3=Float.parseFloat(df.format(t2));
         return t3;
     }
          public void getCountry(String url)
          {
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Country d=new Country();
                        Log.d("onReponse", "Yeah");
                        ArrayList<String> titles = new ArrayList<>();
                        String category = "";
                        ArrayList<String> links = new ArrayList<>();
                        ArrayList<String> descriptions = new ArrayList<>();
                        try {
                            XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
                            xmlFactory.setNamespaceAware(true);
                            XmlPullParser xml = xmlFactory.newPullParser();
                            xml.setInput(response.body().byteStream(), "UTF-8");
                            boolean isInItem = false;
                            int eventType = xml.getEventType();
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                if (eventType == XmlPullParser.START_TAG) {
                                    if (xml.getName().equalsIgnoreCase("item")) {
                                        isInItem = true;
                                    } else if (xml.getName().equalsIgnoreCase("title")) {
                                        if (isInItem) {
                                            String title = xml.nextText();
                                            titles.add(title);
                                        }
                                    } else if (xml.getName().equalsIgnoreCase("category") && category == "") {
                                        if (isInItem) {
                                            category = xml.nextText();
                                            Log.d("category", category);
                                        }
                                    } else if (xml.getName().equalsIgnoreCase("description")) {
                                        if (isInItem) {
                                            String description = xml.nextText();
                                            descriptions.add(description);
                                        }
                                    }
                                } else if (eventType == XmlPullParser.END_TAG && xml.getName().equalsIgnoreCase("item")) {
                                    isInItem = false;
                                }
                                eventType = xml.next();
                            }
                            d.setCategory(category);
                            d.setTitles(titles);
                            d.setDescriptions(descriptions);
                            Log.d("fuck",d.getCategory());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                        messageHandler.post(new Runnable() {
                            @Override
                            public void run() {
                           t.add("United States Dollar(USD)");
                           for(int i=0;i<d.getTitles().size();i++)
                           {
                              String title=d.getTitles().get(i);
                              String[] name=title.split("/");
                              String countryName=name[1];
                              t.add(countryName);
                           }
                           ArrayAdapter ad=new ArrayAdapter<String>(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,t);
                           from.setAdapter(ad);
                           to.setAdapter(ad);
                             int value=Integer.parseInt(amountInput.getText().toString());
                          convert.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  String fromChecked=from.getSelectedItem().toString();
                                  String toChecked=to.getSelectedItem().toString();
                                  float fromval=0;
                                  float toval=0;
                                  String tt="";
                                  for(int i=0;i<d.getTitles().size();i++)
                                  {
                                      String title=d.getTitles().get(i);
                                      String[] name=title.split("/");
                                      String countryName=name[1];
                                      if(fromChecked.equalsIgnoreCase(countryName))
                                      {
                                          String des=d.getDescriptions().get(i);
                                          String[] dess=des.split("=");
                                          String num=dess[1].replaceAll("[^0-9.]","");
                                          fromval=Float.parseFloat(num);
                                      }
                                      if(toChecked.equalsIgnoreCase(countryName))
                                      {
                                          String des=d.getDescriptions().get(i);
                                          String[] dess=des.split("=");
                                          String num=dess[1].replaceAll("[^0-9.]","");
                                          toval=Float.parseFloat(num);
                                      }
                                  }
                                  String unitFrom=fromChecked;
                                  String unitTo=toChecked;

                                      int value = Integer.parseInt(amountInput.getText().toString());
                                      float converted = convertAmount(value, fromval, toval);
                                      res.setText(Integer.toString(value) + " " + unitFrom + "=" + Float.toString(converted) + " " + unitTo);
                                  }
                          });
                          swapButton.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  String fromChecked=from.getSelectedItem().toString();
                                  String toChecked=to.getSelectedItem().toString();
                                  int posfrom=0;
                                  int postTo=0;
                                  for(int i=0;i<d.getTitles().size();i++)
                                  {
                                      String title=d.getTitles().get(i);
                                      String[] name=title.split("/");
                                      String countryName=name[1];
                                      if(fromChecked.equalsIgnoreCase(countryName))
                                      {
                                          posfrom=i+1;

                                      }
                                      if(toChecked.equalsIgnoreCase(countryName))
                                      {
                                          postTo=i+1;
                                      }
                                  }
                                  from.setSelection(postTo);
                                  to.setSelection(posfrom);
                              }
                          });
                            }
                        });
                    } else {
                        ResponseBody responseBody = response.body();
                        String body = responseBody.string();
                        Log.d("onRespone", body);
                    }
                }
            });
        }
}
