package com.example.finalproject_android;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {




    private static final String TRACKERAPI = "https://aviation-edge.com/v2/public/flights?key=712c25-205c0a";
    private static final String SECOND = "&depIata=";


    public static URL generateURL(String sm){


        Uri builtUri = Uri.parse(TRACKERAPI + SECOND + sm);

        URL url = null;

        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }






    }

