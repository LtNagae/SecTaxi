package com.PGA.sectaxi;

/**
 * Created by root on 24/05/16.
 */
public class Singleton_teste {
    private static Singleton_teste mInstance = null;

    private String mString;

    private Singleton_teste(){
        mString = "vazio";
    }

    public static Singleton_teste getInstance(){
        if(mInstance == null)
        {
            mInstance = new Singleton_teste();
        }
        return mInstance;
    }

    public String getString(){
        return this.mString;
    }

    public void setString(String value){
        mString = value;
    }
}