package com.example.oerlex.android_assignment3.phone;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by oerlex on 06.10.2016.
 */
public class WriteReadCalls {

    //TODO this one throws the following exception : java.io.FileNotFoundException: /data/user/0/com.example.oerlex.android_assignment3/files/calls: open failed: ENOENT (No such file or directory)
    public void saveCalls(Context context, String item) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput("lines.txt", Context.MODE_APPEND);
            //OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("lines.txt", Context.MODE_APPEND));
            outputStream.write((item + "\n").getBytes());
            outputStream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public String getPhoneNumber(String item) {
        return item.split("  ")[1];
    }

    public ArrayList<String> readCalls(Context context){
        ArrayList<String> items = new ArrayList<>();

        File file = new File(context.getFilesDir(), "lines.txt");
        System.out.println("FILEPATH : "+file.getPath());
        try {
            FileInputStream fileInputStream = context.openFileInput("lines.txt");
            if(fileInputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String item;

                while((item = bufferedReader.readLine()) != null){
                    items.add(item);
                }
                fileInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

}