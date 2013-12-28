package com.sihrc.quit;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {
    /**
     * Views
     */
    ResizableImageView image;
    Button button;

    /**
     * Retrieving the GIF
     */
    DBHandler db;
    GIF gif;
    ArrayList<String> urls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Grab the GIF
         */
        db = new DBHandler(this);
        db.open();



        image = (ResizableImageView)findViewById(R.id.activity_main_image);
        button = (Button)findViewById(R.id.activity_main_quit_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextImage();
            }
        });
    }

    /**
     * Advance the GIF
     */
    private void goToNextImage(){

    }
    private void getGIF(){
        gif.url = urls.get(getURLIndex());
        gif.images = db.getImagesForGIF(urls.get(getURLIndex()));
        if (gif.images.size() == 0){
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {

                    return null;
                }
            };
        }
    }


    /**
     * SharedPreferences
     */
    private void getURLS(){
        urls.clear();
        urls.addAll(Arrays.asList(getSharedPreferences("QUIT", MODE_PRIVATE).getString("urls", getResources().getString(R.string.urls)).split(",")));
    }
    private int getURLIndex(){
        return getSharedPreferences("QUIT", MODE_PRIVATE).getInt("urlIndex", 0);
    }
    private void saveURLIndex(int value){
        getSharedPreferences("QUIT", MODE_PRIVATE).edit().putInt("urlIndex", 0).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
