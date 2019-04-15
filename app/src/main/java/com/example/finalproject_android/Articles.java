package com.example.finalproject_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Articles {
   private String articles;
   private Boolean save;
   private int id;

    public Articles(){}

    public Articles(String articles, boolean save, int id){
       this.articles=articles;
       this.save = save;
       this.id = id;

    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id =id;
    }

    public Boolean getSave(){
        return save;
    }
    public void setSave(Boolean save){
        this.save =save;
    }

    public String getArticles(){
        return articles;
    }
    public void setArticles(String articles){
        this.articles =articles;
    }

    @Override
    public String toString(){
        return articles + " ";
    }
    }

