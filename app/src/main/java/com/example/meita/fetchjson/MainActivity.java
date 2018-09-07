package com.example.meita.fetchjson;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static android.provider.Contacts.SettingsColumns.KEY;

public class MainActivity extends AppCompatActivity implements Serializable {

    private String url = "http://jsonplaceholder.typicode.com/posts";

    private RecyclerView pList;
    EditText searchBar;

    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeLayout;
    private DividerItemDecoration dividerItemDecoration;
    private List<PostModel> postList;
    private ArrayList<PostModel> postArray;

    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBar = findViewById(R.id.search);
        pList = findViewById(R.id.recycleView);
        swipeLayout = findViewById(R.id.swipe);

        postList = new ArrayList<>();
        postArray = new ArrayList<>();
        adapter = new PostAdapter(getApplicationContext(), postList);


        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(pList.getContext(), linearLayoutManager.getOrientation());

        pList.setHasFixedSize(true);
        pList.setLayoutManager(linearLayoutManager);
        pList.addItemDecoration(dividerItemDecoration);
        pList.setAdapter(adapter);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postList.clear();
                postArray.clear();
                getData();
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String searchValue = searchBar.getText().toString().toLowerCase(Locale.getDefault());
                myFilter(searchValue);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getData();

    }

    private void getData() {
        if (InternetConnection.getInstance().isOnline(MainActivity.this)) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            PostModel postModel = new PostModel();
                            postModel.setTitle(jsonObject.getString("title"));
                            postModel.setId(jsonObject.getInt("id"));
                            postModel.setBody(jsonObject.getString("body"));
                            postList.add(postModel);
                            postArray.add(postModel);

                            try {
                                InternalStorage.writeObject(MainActivity.this, "postCache", postList);
                                //Log.e("Eksekusi", "eksekusi write objek");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Collections.sort(postList, new Comparator<PostModel>() {
                                public int compare(PostModel p1, PostModel p2) {
                                    return p1.getTitle().compareTo(p2.getTitle());
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    swipeLayout.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley", error.toString());
                    progressDialog.dismiss();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonArrayRequest);
        } else {
            try {
                List<PostModel> cachedPost = (List<PostModel>) InternalStorage.readObject(MainActivity.this, "postCache");
                if (cachedPost!=null) {
                    for (PostModel post : cachedPost) {
                        postList.add(post);
                    }
                    adapter.notifyDataSetChanged();
                    swipeLayout.setRefreshing(false);
                } else {
                    ShowAlertDialog.showAlert("Please Pull to Refresh", MainActivity.this);
                }
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void myFilter(String title){
        title = title.toLowerCase(Locale.getDefault());
        postList.clear();
        if (title.length() == 0){
            postList.addAll(postArray);
        } else {
            for (PostModel PL : postArray){
                if (PL.getTitle().toLowerCase(Locale.getDefault()).contains(title)){
                    postList.add(PL);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
