package com.mobile.justmobiledev.firebasestoragestarter;

import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // uploadFile();
        // uploadFileBytes();
        //getFileMetaData();
        downloadFile();
    }

    private void uploadFile()
    {
        try {
            // Create Reference
            FirebaseStorage storage = FirebaseStorage.getInstance();

            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();

            String fileName = "images/test.jpg";

            // Child references can also take paths
            StorageReference testRef = storageRef.child(fileName);

            Log.d(TAG, "Path: "+testRef.getPath());
            Log.d(TAG, "Name: "+testRef.getName());
            Log.d(TAG, "Bucket: "+testRef.getBucket());

            AssetManager assetManager = getAssets();
            InputStream testFileStream = assetManager.open(fileName);

            // Upload to Firebase
            UploadTask uploadTask = testRef.putStream(testFileStream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception ex) {
                    // Handle unsuccessful uploads
                    Log.e(TAG, "An error occurred during upload: "+ex.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e(TAG, "Successful upload");
                }
            });
        }
        catch(Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void uploadFileBytes()
    {
        try {
            // Create Reference
            FirebaseStorage storage = FirebaseStorage.getInstance();

            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();

            String fileName = "images/test1.jpg";

            // Child references can also take paths
            StorageReference testRef = storageRef.child(fileName);

            AssetManager assetManager = getAssets();
            InputStream testFileStream = assetManager.open(fileName);
            byte[] data = new byte[testFileStream.available()];
            DataInputStream dataInputStream = new DataInputStream(testFileStream);

            // Read stream into byte array
            dataInputStream.readFully(data);
            dataInputStream.close();

            // Upload to Firebase
            UploadTask uploadTask = testRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception ex) {
                    // Handle unsuccessful uploads
                    Log.e(TAG, "An error occurred during upload: "+ex.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e(TAG, "Successful upload");
                }
            });
        }
        catch(Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void getFileMetaData(){
        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Get reference to the file
        StorageReference testRef = storageRef.child("images/test.jpg");

        testRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetaData) {
                Log.d(TAG, "Bucket: "+storageMetaData.getBucket());
                Log.d(TAG, "ContentType: "+storageMetaData.getContentType());
                Log.d(TAG, "Name: "+storageMetaData.getName());
                Log.d(TAG, "getPath: "+storageMetaData.getPath());
                Log.d(TAG, "SizeBytes: "+storageMetaData.getSizeBytes());
                Log.d(TAG, "DownloadUrl: "+storageMetaData.getDownloadUrl());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        });
    }

    private void downloadFile()
    {
        try {
            // Create Reference
            FirebaseStorage storage = FirebaseStorage.getInstance();

            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();

            String fileName = "images/test.jpg";

            // Child references can also take paths
            StorageReference testFileRef = storageRef.child(fileName);

            File localFile = File.createTempFile("test", "jpg");

            testFileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    Log.d(TAG, "Successfully downloaded");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception ex) {
                    // Handle any errors
                    Log.e(TAG, ex.getMessage());
                }
            });
        }
        catch(Exception ex)
        {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
