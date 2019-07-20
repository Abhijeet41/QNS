package com.example.interviewpreperation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    public static final int RequestPermissionCode = 7;
    String strId,strEmployee_name,strEmployee_salary;
    private static final String JSON_URL = "http://192.168.0.102/RCTP/v1/showEvent.php";
    private static final String url2 = "http://dummy.restapiexample.com/api/v1/employees";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (CheckingPermissionIsEnabledOrNot()) {
                Toast.makeText(this, "all permission granted", Toast.LENGTH_SHORT).show();
            } else {
                RequestMultiplePermission();
            }
        }

        volleyUsingPost();
        volleyUsingGet();
        VolleyGet();

    }

    private void VolleyGet() {


        StringRequest request = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responseGet",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("EventList");

                    for (int i=0; i<jsonArray.length();i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String TittleOfEvent = object.getString("TittleOfEvent");
                        Log.d("TittleOfEvent",TittleOfEvent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private void volleyUsingGet() {

       JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url2, null, new Response.Listener<JSONArray>() {
           @Override
           public void onResponse(JSONArray response) {

               for (int i=0; i<response.length();i++)
               {
                   try {
                       JSONObject jsonObject = response.getJSONObject(i);
                       String id = jsonObject.getString("id");
                       String employee_name = jsonObject.getString("employee_name");

                       Log.d("id",id);
                       Log.d("emp",employee_name);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       });


        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(jsonArrayRequest);

    }

    private void volleyUsingPost() {

        String url = "http://192.168.0.102/RCTP/v1/profiledetails.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String,String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("MOBILENO","8355942271");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void RequestMultiplePermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {READ_PHONE_STATE, READ_EXTERNAL_STORAGE, CAMERA, WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean ReadphonePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean CameraPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadWriteable = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (ReadphonePermission && ReadExternalStorage && CameraPermission && ReadWriteable) {
                        Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int ThirdPermissoonResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissoonResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

}
