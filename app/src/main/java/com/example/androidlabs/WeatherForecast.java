package com.example.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class WeatherForecast extends AppCompatActivity {
//    private TextView tvCurTemp;
//    private TextView tvMaxTemp;
//    private TextView tvMinTemp;
//    private ProgressBar progressBar;
//    private ImageView imageViewWeatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ProgressBar progressBar = findViewById(R.id.progressBar_weatherQuery);
        progressBar.setVisibility(View.VISIBLE);
//        tvCurTemp = findViewById(R.id.textView_currentTemp);
//        tvMaxTemp = findViewById(R.id.textView_maxTemp);
//        tvMinTemp = findViewById(R.id.textView_minTemp);
//        imageViewWeatherIcon = findViewById(R.id.imageView_weather);

        Button btnWeatherQuery = findViewById(R.id.button_WeatherQuery);
        btnWeatherQuery.setOnClickListener(v -> {
            ForecastQuery forecastQuery = new ForecastQuery();
            forecastQuery.execute();
        });
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String windSpeed;
        private String minTemp;
        private String maxTemp;
        private String currentTemp;
        private Bitmap bmpWeather;

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         *
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param s The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(String s) {
//            tvCurTemp.setText(currentTemp);
//            tvMaxTemp.setText(maxTemp);
//            tvMinTemp.setText(minTemp);
//            progressBar.setVisibility(View.INVISIBLE);
//            imageViewWeatherIcon.setImageBitmap(bmpWeather);
            TextView textView = findViewById(R.id.textView_currentTemp);
            textView.setText("Current Temperature:\t" + currentTemp);

            textView = findViewById(R.id.textView_maxTemp);
            textView.setText("Max Temperature:\t" + maxTemp);

            textView = findViewById(R.id.textView_minTemp);
            textView.setText("Max Temperature:\t" + minTemp);

            ImageView imageView = findViewById(R.id.imageView_weather);
            imageView.setImageBitmap(bmpWeather);

            ProgressBar progressBar = findViewById(R.id.progressBar_weatherQuery);
            progressBar.setVisibility(View.INVISIBLE);
        }

        /**
         * Runs on the UI thread after {@link #publishProgress} is invoked.
         * The specified values are the values passed to {@link #publishProgress}.
         *
         * @param values The values indicating progress.
         * @see #publishProgress
         * @see #doInBackground
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            ProgressBar progressBar = findViewById(R.id.progressBar_weatherQuery);
            progressBar.setProgress(values[0]);
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param strings The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(String... strings) {

            try {
                //create the network connection:
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=shenzhen,cn&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();


                //create a pull parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inStream, "UTF-8");  //inStream comes from line 46

                //now loop over the XML:
                while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (xpp.getEventType() == XmlPullParser.START_TAG) {
                        String tagName = xpp.getName(); //get the name of the starting tag: <tagName>
                        if (tagName.equals("temperature")) {
                            currentTemp = xpp.getAttributeValue(null, "value");
                            Log.i("Weather Query AsyncTask", "Found parameter temperature: " + currentTemp);
                            publishProgress(25);
                            SystemClock.sleep(500);

                            minTemp = xpp.getAttributeValue(null, "min");
                            Log.i("Weather Query AsyncTask", "Found parameter temperature: " + minTemp);
                            publishProgress(50);
                            SystemClock.sleep(500);

                            maxTemp = xpp.getAttributeValue(null, "min");
                            Log.i("Weather Query AsyncTask", "Found parameter temperature: " + maxTemp);
                            publishProgress(75);
                            SystemClock.sleep(500);
                        }
                        else if (tagName.equals("weather")) {
                            String iconName = xpp.getAttributeValue(null, "icon");
                            Log.i("Weather Query AsyncTask", "Found parameter weather: " + iconName);
                            String iconFile = iconName + ".png";
                            if (fileExistance(iconFile)) {
                                Log.i("Weather Query AsyncTask", "IconFile[" + iconName + "] already downloaded!");
                                // file exists
                                FileInputStream fis = null;
                                try {
                                    fis = openFileInput(iconFile);
                                }
                                catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                bmpWeather = BitmapFactory.decodeStream(fis);

                            }
                            else {
                                // file doesn't exist, download it from internet
                                Log.i("Weather Query AsyncTask", "IconFile[" + iconFile + "] not found, download it from internet!");
                                String weatherIconRequestURL = "http://openweathermap.org/img/w/" + iconFile;

                                URL urlWeatherIconServer = new URL(weatherIconRequestURL);
                                URLConnection connection = urlWeatherIconServer.openConnection();
                                connection.connect();
                                int responseCode = ((HttpURLConnection) connection).getResponseCode();
                                if (responseCode == 200) {
                                    bmpWeather = BitmapFactory.decodeStream(connection.getInputStream());
                                    //save locally
                                    FileOutputStream outputStream = openFileOutput( iconFile, Context.MODE_PRIVATE);
                                    bmpWeather.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                }
                            }
                            publishProgress(100);
                        }
                    }
                    xpp.next(); //advance to next XML event
                }

                // get uv index
                url = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                urlConnection = (HttpURLConnection) url.openConnection();
                inStream = urlConnection.getInputStream();



            } catch (Exception ex) {
                Log.e("Crash!!", ex.getMessage());
            }

            //return type 3, which is String:
            return "Finished task";

        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

    }
}
