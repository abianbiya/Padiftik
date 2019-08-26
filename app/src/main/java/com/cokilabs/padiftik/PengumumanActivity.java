package com.cokilabs.padiftik;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cokilabs.padiftik.adapter.PengumumanAdapter;
import com.cokilabs.padiftik.model.Pengumuman;
import com.cokilabs.padiftik.model.User;
import com.cokilabs.padiftik.network.APIClient;
import com.cokilabs.padiftik.network.APIInterface;
import com.cokilabs.padiftik.utils.SessionManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PengumumanActivity extends AppCompatActivity {

    TextView judul, isi;
    RecyclerView list;
    ArrayList<Pengumuman> pengumumanArrayList;
    PengumumanAdapter adapter;
    SessionManager sessionManager;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengumuman);
        sessionManager = new SessionManager(this);

        initToolbar("");

        pengumumanArrayList = new ArrayList<>();

        judul = findViewById(R.id.tv_id_kartu);
        isi = findViewById(R.id.tv_no_kartu);

        list = findViewById(R.id.recycler);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PengumumanActivity.this, AddPengumumanActivity.class);
                startActivity(intent);
            }
        });


//        callApi();
    }

    public void callApi(){
        APIInterface service = APIClient.getClient(sessionManager.getAuthToken());
        final Gson gson = new Gson();

        Call<ResponseBody> call = service.getPengumumans();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String res = response.body().string();

                        Log.e("respon", res);

                        JSONObject json = new JSONObject(res);

                        String api_status = json.getString("status");
                        String api_message = json.getString("message");

                        if(api_status.equals("success")){
                            int i;
                            JSONArray item = json.getJSONArray("data");
                            for (i=0; i<item.length(); i++) {
                                JSONObject obj = item.getJSONObject(i);
                                Pengumuman data = gson.fromJson(obj.toString(), Pengumuman.class);
                                pengumumanArrayList.add(data);
                            }

                            if(pengumumanArrayList.size() > 0 ){
                                isi.setText(pengumumanArrayList.get(0).getPengumuman());
                                judul.setText(pengumumanArrayList.get(0).getCreatedAt());

                                pengumumanArrayList.remove(0);
                            }

                            adapter = new PengumumanAdapter(PengumumanActivity.this, pengumumanArrayList);
                            list.setAdapter(adapter);


                        }else {
                            Toast.makeText(getApplicationContext(), api_message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Log.e("login", e.toString());
                    }
                }else {
//                    password.setError(getString(R.string.error_incorrect_password));
//                    mPasswordView.requestFocus();
//                    load.dismiss();
                    Toast.makeText(getApplicationContext(),response.message()+" "+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pengumumanArrayList.clear();
        callApi();
    }

    public void initToolbar(String vTitle){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View viewActionBar = getLayoutInflater().inflate(R.layout.toolbar_title, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView tvTitle = viewActionBar.findViewById(R.id.text_title);
        tvTitle.setText(vTitle);
        getSupportActionBar().setCustomView(viewActionBar, params);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
    }
}
