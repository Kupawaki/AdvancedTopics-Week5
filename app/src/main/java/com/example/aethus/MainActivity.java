package com.example.aethus;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    public ListView listView;
    public String[] items;
    public ArrayList<File> mySongs;
    public ArrayList<File> mySongsSorted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize arrayList and set a layout for the listView
        mySongs = new ArrayList<>();
        mySongsSorted = new ArrayList<>();
        listView = findViewById(R.id.songLV);

        //As for Permission
        runtimePermission();
    }

    //Asking for permission with Dexter
    public void runtimePermission()
    {
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener()
                {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport)
                    {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken)
                    {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }

    //Custom Adapter Class for the ListView items
    class customAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            View myView = getLayoutInflater().inflate(R.layout.list_item, null);
            TextView textsong = myView.findViewById(R.id.songNameTV);
            textsong.setSelected(true);
            textsong.setText(items[i]);

            return myView;
        }
    }

    //This calls the findSong method, remove the extensions, sets the adapter, sets the onClickListener, and finally starts the PlayerActivity on click
    void displaySongs()
    {
        File path = new File("/storage/self/primary");
        findSong(path);
        test();
        Log.d("LFP", "Size: " + mySongs.size());

        //Rename the songs (remove the extensions)
        items = new String[mySongs.size()];
        for (int i = 0; i<mySongs.size();i++)
        {
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }

        //Set the adapter
        customAdapter customAdapter = new customAdapter();
        listView.setAdapter(customAdapter);

        //How the song is played when it is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                String songName = (String) listView.getItemAtPosition(i);
                startActivity(new Intent(getApplicationContext(), PlayerActivity.class)
                        .putExtra("songs", mySongs)
                        .putExtra("songname", songName)
                        .putExtra("pos", i));
            }
        });
    }

    //This uses recursion to find files that have a specific extension and them add them to the arrayList, so they can then be added to the ListView
    public void findSong(File dir)
    {
        Log.d("TESTING", "Call recieved");

        File[] files = dir.listFiles();
        assert files != null;
        for (File singleFile:files)
        {
            if (singleFile.getName().equals("Aethus"))
            {
                Log.d("LFP", "Tried to Read the Aethus Folder - Access Denied");
            }
            else if (singleFile.isDirectory() && !singleFile.isHidden())
            {
                Log.d("LFP", "Directory --> Path of Directory: " + singleFile);
                findSong(singleFile);
            }
            else
            {
                Log.d("LFP", "File --> Name of File: " + singleFile);
                if (singleFile.getName().endsWith(".ogg"))
                {
                    Log.d("LFP", "----------------------------------------------------");
                    Log.d("LFP", "------------------------.ogg------------------------");
                    Log.d("LFP", "Found it! Its called: " + singleFile.getName());
                    Log.d("LFP", "------------------------.ogg------------------------");
                    Log.d("LFP", "----------------------------------------------------");
                    mySongs.add(singleFile);
                }
                else if (singleFile.getName().endsWith(".mp3"))
                {
                    Log.d("LFP", "----------------------------------------------------");
                    Log.d("LFP", "------------------------.mp3------------------------");
                    Log.d("LFP", "Found it! Its called: " + singleFile.getName());
                    Log.d("LFP", "------------------------.mp3------------------------");
                    Log.d("LFP", "----------------------------------------------------");
                    mySongs.add(singleFile);
                }
                else if (singleFile.getName().endsWith(".wav"))
                {
                    Log.d("LFP", "----------------------------------------------------");
                    Log.d("LFP", "------------------------.wav------------------------");
                    Log.d("LFP", "Found it! Its called: " + singleFile.getName());
                    Log.d("LFP", "------------------------.wav------------------------");
                    Log.d("LFP", "----------------------------------------------------");
                    mySongs.add(singleFile);
                }
            }
        }
    }

    public void test()
    {
        for (int i = 0; i < mySongs.size(); i++)
        {
            Log.d("TEST", mySongs.get(i).getName());
        }
        Log.d("TEST", "----------------------------------------------");

        Collections.sort(mySongs);
        for (int i = 0; i < mySongs.size(); i++)
        {
            Log.d("TEST", mySongs.get(i).getName());
        }
    }

//    if (string1 > string2) it returns a positive value.
//    i.e.(string1 == string2) it returns 0.
//    if (string1 < string2) it returns a negative value.
}
