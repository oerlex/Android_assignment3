package com.example.oerlex.android_assignment3.phone;

import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.oerlex.android_assignment3.R;

import java.io.IOException;
import java.util.ArrayList;

public class PhoneActivity extends AppCompatActivity {

    private ListView listView;
    private WriteReadCalls writeReadCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        writeReadCalls = new WriteReadCalls();
        ArrayList<String> calls = null;
        calls = writeReadCalls.readFile(this);
        listView = (ListView)findViewById(R.id.listViewCalls);

        ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, calls);
        listView.setAdapter(listAdapter);
        registerForContextMenu(listView);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextmenu, menu);


    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String listviewItem = listView.getItemAtPosition(info.position).toString();

        String number = writeReadCalls.getPhoneNumber(listviewItem);
        String dialing = "tel:" + number;
        String message = "sms:" + number;
        switch (item.getItemId()) {
            case R.id.call:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dialing)));
                return true;
            case R.id.message :
                Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse(message));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, number);
                Intent chooser = Intent.createChooser(intent, "Send with..");
            // Verify the intent will resolve to at least one activity
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
                return super.onContextItemSelected(item);
            default:
                return super.onContextItemSelected(item);
        }
    }
}
