package com.example.user.json_parsing;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button= (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatetextView();
            }
        });
    }

    private void updatetextView() {
        //TextView textView= (TextView) findViewById(R.id.textView);
        NetworkTask networkTask=new NetworkTask();
        networkTask.execute("https://api.github.com/search/users?q=harshit");

    }
    void makeNetworkCall(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
               // ArrayList<GithubUser> users=parseJson(result);
                Gson gson=new Gson();
                ApiResult apiResult=gson.fromJson(result,ApiResult.class);

                final GithubUserAdapter githubUserAdapter = new GithubUserAdapter(apiResult.getItems());

                 MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvUsers);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        recyclerView.setAdapter(githubUserAdapter);

                    }
                });


            }
        });

    }

    private ArrayList<GithubUser> parseJson(String s) {
        ArrayList<GithubUser>githubUsers=new ArrayList<>();
        //JSON parsing

        try {
            JSONObject root = new JSONObject(s);
            JSONArray items = root.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject object = items.getJSONObject(i);
                String login = object.getString("login");
                Integer id = object.getInt("id");
                String avatar = object.getString("avatar_url");
                Double score = object.getDouble("score");
                String html = object.getString("html_url");
                GithubUser githubUser = new GithubUser(login, id, html, score, avatar);
                githubUsers.add(githubUser);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return githubUsers;

    }

    class NetworkTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String stringUrl=params[0];
            try {
                URL url=new URL(stringUrl);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                Scanner scanner=new Scanner(inputStream);
                scanner.useDelimiter("\\A");
                if(scanner.hasNext())
                {
                    String s=scanner.next();
                    return s;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "failed to load";

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<GithubUser> users=parseJson(s);
            GithubUserAdapter githubUserAdapter = new GithubUserAdapter(users);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvUsers);
            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            recyclerView.setAdapter(githubUserAdapter);


            //TextView textView= (TextView) findViewById(R.id.textView);
            //textView.setText(s);

        }
       /* ArrayList<GithubUser>parseJson(String s)
        {
            ArrayList<GithubUser>githubUsers=new ArrayList<>();
            //JSON parsing

            try {
                JSONObject root = new JSONObject(s);
                JSONArray items = root.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject object = items.getJSONObject(i);
                    String login = object.getString("login");
                    Integer id = object.getInt("id");
                    String avatar = object.getString("avatar_url");
                    Double score = object.getDouble("score");
                    String html = object.getString("html_url");
                    GithubUser githubUser = new GithubUser(login, id, html, score, avatar);
                    githubUsers.add(githubUser);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return githubUsers;
        }
        */
    }
}

