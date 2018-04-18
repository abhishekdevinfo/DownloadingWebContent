package com.example.root.dwonloadingwebcontent;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    // Google Suggest use different thread for task that take long time. So the main UI Thread won't delay


    // Running Downloading task in different thread.
    //AsyncTask took 3 variables. first - send, second - progress, third - data receive
    @SuppressLint("StaticFieldLeak")
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";     // The actual receive data
            URL url;        // Store the url from urls[0]
            HttpURLConnection urlConnection;     //Its like a browser. So I open a browser and fetch the data with the help of given url

            try {
                url = new URL(urls[0]);     //converting given string to url

                urlConnection = (HttpURLConnection) url.openConnection();   // Now i open the browser window and started to attempt to access the url (in Background)

                InputStream in = urlConnection.getInputStream();    // Its a Stream that hold the input of data

                InputStreamReader reader = new InputStreamReader(in);   // Reading the data

                int data = reader.read();   // Reading the data 1 character at a time. Track on the location where the reader read the data at that instance.

                while (data != -1) {
                    char current = (char) data;     //Current character that will read from file.

                    result += current;  //  making string with char

                    data = reader.read();   // Telling data to move on to the next character
                }

                return result;

            }
            catch (Exception e) {
                e.printStackTrace();

                return "Failed";
            }

        }
    }

    // This is the first Thread that is UI thread.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        String result = null;
        try {
            result = task.execute("https://abhishekdevinfo.github.io/").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.i("Contents of URL", result);

    }
}
