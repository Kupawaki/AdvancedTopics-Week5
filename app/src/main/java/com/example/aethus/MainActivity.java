package com.example.aethus;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    public ArrayList<File> listOfUsefulFiles;
    ListView listView;
    String[] itemsToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize variables to avoid the great and terrible NullPointerException
        listOfUsefulFiles = new ArrayList<>();
        listView = findViewById(R.id.songLV);

        //Do the thing
        askForPermission();

        //Make the directory I plan to put all the stuff into.
        File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"Aethus");
        if (directory.exists())
        {
            Log.d("FMS", "Directory Already Exists");
        }
        else
        {
            directory.mkdirs();
            Log.d("FMS", "Directory Created");
        }
    }

    //TODO - Nothing, I am done with this
    public void askForPermission()
    {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener()
        {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse)
            {
                Log.d("PRMS", "Permission to Read Granted");
                displaySongs();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse)
            {
                Log.d("PRMS", "Permission to Read Denied - Preparing to Explode");
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken)
            {
                Log.d("PRMS", "Permission to Read In A Strange State - Will Continue To Ask");
                permissionToken.continuePermissionRequest();
            }
        }).check();

        Dexter.withContext(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener()
        {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse)
            {
                Log.d("PRMS", "Permission to Write Granted");
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse)
            {
                Log.d("PRMS", "Permission to Write Denied - Preparing to Explode");
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken)
            {
                Log.d("PRMS", "Permission to Write In A Strange State - Will Continue To Ask");
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

//    public ArrayList<File> findSong(File file)
//    {
//        ArrayList arrayList = new ArrayList();
//        File[] files = file.listFiles();
//        if (files != null)
//        {
//            for (File singlefile : files)
//            {
//                if (singlefile.isDirectory() && !singlefile.isHidden())
//                {
//
//                    Log.d("BIG SAD", "Name of Directory: " + singlefile.getName().toString());
//                    Log.d("BIG SAD", "Contents of ListFiles: " + files.toString());
//                    arrayList.addAll(findSong(singlefile));
//
//                }
//                else
//                {
//                    if (singlefile.getName().endsWith(".wav"))
//                    {
//                        arrayList.add(singlefile);
//                        Log.d("BIG SAD", "Name of File: " + singlefile.getName().toString());
//                    }
//                    else if (singlefile.getName().endsWith(".mp3"))
//                    {
//                        arrayList.add(singlefile);
//                        Log.d("BIG SAD", "Name of File: " + singlefile.getName().toString());
//                    }
//                    else if (singlefile.getName().endsWith(".ogg"))
//                    {
//                        arrayList.add(singlefile);
//                        Log.d("BIG SAD", "Name of File: " + singlefile.getName().toString());
//                    }
//                }
//            }
//            Log.d("BIG SAD", "Did not return null");
//            return arrayList;
//        }
//        Log.d("BIG SAD", "Returned NULL");
//        return arrayList;
//    }

    public void listFiles(File dir)
    {
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
                listFiles(singleFile);
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
                    listOfUsefulFiles.add(singleFile);
                }
                else if (singleFile.getName().endsWith(".mp3"))
                {
                    Log.d("LFP", "----------------------------------------------------");
                    Log.d("LFP", "------------------------.mp3------------------------");
                    Log.d("LFP", "Found it! Its called: " + singleFile.getName());
                    Log.d("LFP", "------------------------.mp3------------------------");
                    Log.d("LFP", "----------------------------------------------------");
                    listOfUsefulFiles.add(singleFile);
                }
                else if (singleFile.getName().endsWith(".wav"))
                {
                    Log.d("LFP", "----------------------------------------------------");
                    Log.d("LFP", "------------------------.wav------------------------");
                    Log.d("LFP", "Found it! Its called: " + singleFile.getName());
                    Log.d("LFP", "------------------------.wav------------------------");
                    Log.d("LFP", "----------------------------------------------------");
                    listOfUsefulFiles.add(singleFile);
                }
            }
        }
    }

    private void displaySongs()
    {
        File path = new File("/storage/self/primary");
        listFiles(path);

        for (File files:listOfUsefulFiles)
        {
            Log.d("USEFUL FILES", "File: " + files);
        }

        itemsToDisplay = new String[listOfUsefulFiles.size()];
        Log.d("USEFUL FILES", "Items size: " + itemsToDisplay.length);
        for(int i = 0; i < listOfUsefulFiles.size(); i++)
        {
            itemsToDisplay[i] = listOfUsefulFiles.get(i).getName().toString().replace(".mp3", "").replace(".wav", "").replace(".obb", "");
        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemsToDisplay);
        listView.setAdapter(myAdapter);
    }
}
