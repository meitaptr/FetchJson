package com.example.meita.fetchjson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;

import static android.content.ContentValues.TAG;

public class DetailPost extends AppCompatActivity {
    private String url = "http://jsonplaceholder.typicode.com/posts";

    TextView textViewBody, textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        setTitle("Detail Post");

        Bundle bundle = getIntent().getExtras();
        int idPost = bundle.getInt("idPost");
        String titlePost = bundle.getString("title");
        String bodyPost = bundle.getString("body");
        Log.d(TAG, "idPostDetail: "+idPost);

        textViewTitle = findViewById(R.id.title);
        textViewBody = findViewById(R.id.body);
        textViewTitle.setText(titlePost);
        textViewBody.setText(bodyPost);


    }
}
