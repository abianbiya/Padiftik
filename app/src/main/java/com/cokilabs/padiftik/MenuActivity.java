package com.cokilabs.padiftik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cokilabs.padiftik.model.User;
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

public class MenuActivity extends AppCompatActivity {

    Switch onoff;
    Button pengumuman, logout;
    Boolean status;
    SessionManager sessionManager;
    TextView nama, nip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sessionManager = new SessionManager(this);

        onoff = findViewById(R.id.switchnya);
        pengumuman = findViewById(R.id.button_pengumumuman);

        nama = findViewById(R.id.welcome_name);
        nip = findViewById(R.id.welcome_status);
        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setAuthToken("");
                sessionManager.setLogin(false);
                sessionManager.setLoginBy("");

                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        nama.setText(sessionManager.getLogedInProfile().getName());
        nip.setText(sessionManager.getLogedInProfile().getNip());

        callApi();

        onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                callApiNew(b?1:0);

            }
        });


        pengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, PengumumanActivity.class);
                startActivity(intent);
            }
        });


    }

    public void callApi(){
        Log.e("respon", sessionManager.getAuthToken());
        APIInterface service = APIClient.getClient(sessionManager.getAuthToken());
        final Gson gson = new Gson();

        Call<ResponseBody> call = service.getStatus();
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

                            JSONObject object= json.getJSONObject("data");

                            status = object.getInt("status") == 1;


                            onoff.setChecked(status);


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

    public void callApiNew(Integer status){
        APIInterface service = APIClient.getClient(sessionManager.getAuthToken());
        final Gson gson = new Gson();

        Call<ResponseBody> call = service.setStatus(status);
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

                            Toast.makeText(getApplicationContext(), "Berhasil perbarui status", Toast.LENGTH_SHORT).show();

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
}
