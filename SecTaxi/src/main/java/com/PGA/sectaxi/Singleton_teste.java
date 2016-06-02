package com.PGA.sectaxi;

/**
 * Created by root on 24/05/16.
 */
public class Singleton_teste {
    private static Singleton_teste mInstance = null;

    private String mString;
    private boolean checa_bt;

    private Singleton_teste(){
        mString = "vazio";
        checa_bt = false;
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
    public boolean getstatus() {
        return this.checa_bt;
    }

    public void setString(String value){
        mString = value;
    }
    public void setstatus(boolean status) {
        checa_bt = status;
    }

}