package com.cokilabs.padiftik;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cokilabs.padiftik.network.APIClient;
import com.cokilabs.padiftik.network.APIInterface;
import com.cokilabs.padiftik.utils.SessionManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPengumumanActivity extends AppCompatActivity {

    EditText judul, isi;
    Button simpan;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pengumuman);

        initToolbar("Buat Pengumuman");

        sessionManager = new SessionManager(this);

        judul = findViewById(R.id.judul);
        isi = findViewById(R.id.isi);

        simpan = findViewById(R.id.save);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi(judul.getText().toString(), isi.getText().toString());
            }
        });


    }

    public void callApi(String judul, String isi){
        APIInterface service = APIClient.getClient(sessionManager.getAuthToken());
        final Gson gson = new Gson();

        Call<ResponseBody> call = service.postPengumuman(isi);
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

                            Toast.makeText(getApplicationContext(), "Berhasil simpan pengumuman", Toast.LENGTH_SHORT).show();
                            finish();

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
