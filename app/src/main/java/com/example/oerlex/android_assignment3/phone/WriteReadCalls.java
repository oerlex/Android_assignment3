package com.example.oerlex.android_assignment3.phone;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.oerlex.android_assignment3.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


/**
 * Created by oerlex on 06.10.2016.
 */
public class WriteReadCalls {


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public ArrayList<String> readFile(Context context){
        ArrayList<String> calls = new ArrayList<>();

        try {
            String aDataRow = "";
            String aBuffer = "";
            File myFile = new File("/sdcard/"+"calls.txt");
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));

            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow + "\n";
                calls.add(aDataRow);
            }
            myReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  calls;
    }

    public void saveCalls(Context context, String item){
        FileOutputStream fos;
        try {
            File myFile = new File("/sdcard/"+"calls.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(item);
            myOutWriter.close();
            fOut.close();

            Toast.makeText(context.getApplicationContext(),item + "     saved",Toast.LENGTH_LONG).show();


        } catch (IOException e) {e.printStackTrace();}

    }




    //TODO this one throws the following exception : java.io.FileNotFoundException: /data/user/0/com.example.oerlex.android_assignment3/files/calls: open failed: ENOENT (No such file or directory)
   /* public void saveCalls(Context context, String item) {
        FileOutputStream outputStream;
        try {
            File f = context.getResources().openRawResource(>R)
            outputStream = context.getResources().openRawResource(R.raw.calls);
            //OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("lines.txt", Context.MODE_APPEND));
            outputStream.write((item + "\n").getBytes());
            outputStream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }*/

    public String getPhoneNumber(String item) {
        return item.split(" ")[1];
    }

    /*public ArrayList<String> readCalls(Context context){
        ArrayList<String> items = new ArrayList<>();

        File file = new File(context.getFilesDir(), "lines.txt");
        System.out.println("FILEPATH : "+file.getPath());
        if(file.exists()){
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
        }

        return items;
    }*/

  /*  public ArrayList<String> readCalls(Context context) throws IOException {
        ArrayList<String> calls = new ArrayList<>();
        InputStream inputStream = context.getResources().openRawResource(R.raw.calls);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String item;
        while(true){
            item = bufferedReader.readLine();
            if(item != null){
                calls.add(item);
            }else{
                break;
            }
        }
        inputStream.close();
        bufferedReader.close();
        return calls;
    }*/

   /* public void saveCalls(Context context, String item) throws IOException {
        FileOutputStream fOut = context.openFileOutput("calls.txt", Context.MODE_APPEND);
        OutputStreamWriter osw = new OutputStreamWriter(fOut);

        // Write the string to the file
        osw.write(item);

       *//* ensure that everything is
        * really written out and close *//*
        osw.flush();
        osw.close();
    }


    public ArrayList<String> readCalls(Context context) throws IOException {
        FileInputStream fIn = context.openFileInput("calls.txt");
        ArrayList<String> calls = new ArrayList<>();
        InputStreamReader isr = new InputStreamReader(fIn);
        BufferedReader bufferedReader = new BufferedReader(isr);
        String item;

        *//* Prepare a char-Array that will
         * hold the chars we read back in. *//*
        char[] inputBuffer = new char[1024];

        // Fill the Buffer with data from the file
        isr.read(inputBuffer);
        while((item = bufferedReader.readLine()) != null){
            calls.add(item);
        }
        // Transform the chars to a String
        String readString = new String(inputBuffer);
        isr.close();
        bufferedReader.close();

        return calls;

    }
*/

}