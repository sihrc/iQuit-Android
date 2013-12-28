package com.sihrc.quit;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class MainActivity extends Activity {
    /**
     * Views
     */
    ResizableImageView image;
    Button button;

    /**
     * Retrieving the GIF
     */
    String serverURL = "https://sihrc.github.io/views/quit.html";
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
        loadRandomGIF();



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
        gif.images = db.getImagesForGIF(gif.url);
        if (gif.length == 0){
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    DefaultHttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet(gif.url);
                    try {
                        HttpResponse response = client.execute(request);
                        BufferedReader buf = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        gif.length = Integer.parseInt(buf.readLine().replace("\\s", ""));
                        gif.ext = buf.readLine().replace("\\s", "");
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    for (int i = 0; i < gif.length; i++){
                        getGIFImages(i);
                    }
                }
            }.execute();
        }
    }

    private void getGIFImages(final int index){
        new AsyncTask<Void, Void, byte[]>(){
            @Override
            protected byte[] doInBackground(Void... params) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(gif.url + "/" + index + gif.ext);
                try{
                    //Request google image search results
                    HttpResponse response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    int imageLength = (int)(entity.getContentLength());
                    InputStream is = entity.getContent();
                    byte[] imageBlob = new byte[imageLength];
                    int bytesRead = 0;
                    //Pull the image's byte array
                    while (bytesRead < imageLength) {
                        int n = is.read(imageBlob, bytesRead, imageLength - bytesRead);
                        bytesRead += n;
                    }
                    return imageBlob;
                } catch (Exception e) {
                    e.printStackTrace();
                    return new byte[0];
                }
            }

            @Override
            protected void onPostExecute(byte[] bytes) {
                super.onPostExecute(bytes);
                gif.images.add(bytes);
                if (gif.images.size() == gif.length){
                    db.addGIFtoDatabase(gif);
                }
            }
        }.execute();
    }


    /**
     * Accessing the Server
     */
    private void loadRandomGIF(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(serverURL);
                try {
                    HttpResponse response = client.execute(request);
                    BufferedReader buf = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line;
                    while ((line=buf.readLine())!=null){
                        urls.add(line.replace("\\s", ""));
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    Log.d("DEBUGGER", "FAILED");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                HashSet<String> urlUnique = new HashSet<String>();
                urlUnique.addAll(urls);
                urls.clear();
                urls.addAll(urlUnique);
                getGIF();
            }
        }.execute();
    }
    private int getURLIndex(){
        Random rand = new Random(System.currentTimeMillis());
        Log.d("DEBUGGER", urls.size() + "");
        return rand.nextInt(urls.size());
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
