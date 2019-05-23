package com.example.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MovieDetail extends AppCompatActivity {
    Integer id;
    TextView title, vote_average, release_date, synopsis;
    ImageView movie_image;


    String api_key = "f20d6b74956af8e8c153eb39ac133525";

  @Override
  protected void onCreate(Bundle savedInstanceState){
      super.onCreate(savedInstanceState);
      setContentView(R.layout.movie_detail);

      title = (TextView) findViewById(R.id.title);
      vote_average = (TextView) findViewById(R.id.vote_average);
      release_date = (TextView) findViewById(R.id.release_date);

      synopsis = (TextView) findViewById(R.id.synopsis);
      movie_image = (ImageView) findViewById(R.id.movie_image);
      Intent intent = getIntent();
      id = intent.getIntExtra("Movie ID", 0);

      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      GetMovieDetails getMovieDetails = new GetMovieDetails();
      getMovieDetails.execute();
  }
    public class GetMovieDetails extends AsyncTask<Void,Void,Void>{
        String LOG_TAG = "GetMovieDetails";
        Double votes;
        String original_title, releaseDate, plotSynopsis, poster_path;


        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;
            String jsonMovie =null;

            try{
                String main_url="https://api.themoviedb.org/3/movie/";
                URL url=new URL(main_url+Integer.toString(id)+"?api_key"+api_key);
                Log.d(LOG_TAG,"URL: " + url.toString());

                urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream=urlConnection.getInputStream();
                StringBuffer buffer=new StringBuffer();
                if(inputStream==null){
                    return null;
                }
                reader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line=reader.readLine())!=null){
                    buffer.append(line+"\n");
                }
                if(buffer.length()==0){
                    return null;
                }
                jsonMovie=buffer.toString();
                Log.d(LOG_TAG, "JSON Parsed: " + jsonMovie);
                JSONObject movie = new JSONObject(jsonMovie);
                original_title = movie.getString("original_title");
                releaseDate = movie.getString("release_date");
                votes = movie.getDouble("vote_average");
                plotSynopsis = movie.getString("overview");
                poster_path = "http://image.tmdb.org/t/p/w185" + movie.getString("poster_path");

            }catch (Exception e){
                Log.e(LOG_TAG,"There is an error",e);
            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if(reader!=null){
                    try {
                        reader.close();
                    }catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                }
                }
            }



            return null;
        }
        @Override
        protected void onPostExecute(Void data){
            title.setText(original_title);
            vote_average.setText("Votes: " + Double.toString(votes));
            release_date.setText("Release Date: " + releaseDate);
            synopsis.setText(plotSynopsis);
            movie_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            movie_image.setPadding(8, 8, 8, 8);
            Picasso.with(MovieDetail.this).load(poster_path).into(movie_image);
        }
    }

}
