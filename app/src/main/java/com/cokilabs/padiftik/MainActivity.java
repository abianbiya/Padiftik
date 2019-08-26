package com.cokilabs.padiftik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    SessionManager sessionManager;
    Button loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);

        if(sessionManager.isLoggedIn()){
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }

        username = findViewById(R.id.username);
        password= findViewById(R.id.password);

        loginbtn = findViewById(R.id.email_sign_in_button);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi(username.getText().toString(), password.getText().toString());
            }
        });


    }

    public Boolean attempt(String username, String password){
        if(username == null || password == null){
            return false;
        }else{
            callApi(username, password);
            return true;
        }
    }

    public void callApi(String username, final String password){
        APIInterface service = APIClient.getClient(sessionManager.getAuthToken());
        final Gson gson = new Gson();

        Log.e("respon", "clicked");

        Call<ResponseBody> call = service.login(username, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("respon", "onRespon");
                if(response.isSuccessful()){

                    try {
                        String res = response.body().string();

                        Log.e("respon", res);

                        JSONObject json = new JSONObject(res);

                        String api_status = json.getString("status");
                        String api_message = json.getString("message");

                        if(api_status.equals("success")){

                            JSONObject data = json.getJSONObject("data");
                            String token = data.getString("token");
                            User user = gson.fromJson(data.getString("user"), User.class);

                            sessionManager.saveLogedInProfile(user);
                            sessionManager.setLogin(true);
                            sessionManager.setAuthToken(token);

                            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                            startActivity(intent);
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
                Log.e("respon", t.getMessage());
            }
        });
    }
}
