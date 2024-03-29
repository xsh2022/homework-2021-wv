package xyz.imaginarycrisis.wanandroidapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.widget.Toast.LENGTH_SHORT;

public class LoginActivity extends AppCompatActivity {
    private Button touristButton;
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvNoAccount;
    private Button loginButton;
    private Context thisLoginActivityContext;
    String responseData;
    private final MHandler mHandler = new MHandler();
    private final LoginActivity thisLoginActivity = LoginActivity.this;
    private List<String> setCookies = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        thisLoginActivityContext = this;

        initViews();
        setListeners();
    }

    static void activityStart(Context context){
        context.startActivity(new Intent(context,LoginActivity.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews(){
        Window window = thisLoginActivity.getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int option = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(option);
        window.setStatusBarColor(Color.TRANSPARENT);
        touristButton = findViewById(R.id.tourist_login_button);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        tvNoAccount = findViewById(R.id.no_account);
        loginButton = findViewById(R.id.button_login);

    }

    private void setListeners(){
        loginButton.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();


            if(username.isEmpty()||password.isEmpty())
                Toast.makeText(thisLoginActivityContext,"用户名、密码不能为空", LENGTH_SHORT).show();

            else {
                HashMap<String,String> map = new HashMap<>();
                map.put("username",username);
                map.put("password",password);
                requestLogin(map);
            }


        });
        final SpannableStringBuilder style = new SpannableStringBuilder();
        style.append("没有账号？立刻注册！");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                RegisterActivity.activityStart(thisLoginActivityContext);
            }
        };
        style.setSpan(clickableSpan, 5, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvNoAccount.setText(style);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
        style.setSpan(foregroundColorSpan, 5, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvNoAccount.setMovementMethod(LinkMovementMethod.getInstance());

        touristButton.setOnClickListener(v -> {
            InnerActivity.actStart(thisLoginActivityContext,DecodedLoginData.spawnDecodedJsonData(responseData),setCookies);
            thisLoginActivity.finish();
        });
    }

    private void requestLogin(HashMap<String,String> params){

        new Thread(
                ()->{
                    try{
                        String sUrl = getLoginUrl();
                        URL url = new URL(sUrl);
                        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        StringBuilder dataToWrite = new StringBuilder();
                        for(String key:params.keySet()){
                            dataToWrite.append(key).append("=").append(params.get(key)).append("&");
                        }
                        connection.connect();
                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(dataToWrite.substring(0,dataToWrite.length()-1).getBytes());
                        Map<String, List<String>> cookies = connection.getHeaderFields();
                        List<String> setCookies = cookies.get("Set-Cookie");
                        InputStream in = connection.getInputStream();
                        String responseData = Tools.streamToString(in);
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("responseData",responseData);
                        map.put("setCookies",setCookies);
                        Message msg = new Message();
                        msg.obj = map;
                        mHandler.sendMessage(msg);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
        ).start();
    }
    private String getLoginUrl(){
        return "https://www.wanandroid.com/user/login";
    }

    private void continueLoginProcess(){
        boolean err = loginErr();
        if(err){
            Toast.makeText(thisLoginActivityContext,"登陆失败！\n原因："+errMsg(),LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(thisLoginActivityContext,"恭喜您登陆成功！",LENGTH_SHORT).show();
            InnerActivity.actStart(thisLoginActivityContext,DecodedLoginData.spawnDecodedJsonData(responseData),setCookies);
            thisLoginActivity.finish();
        }
    }

    private String errMsg(){
        String msg;
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            msg = jsonObject.getString("errorMsg");
        } catch (JSONException e) {
            e.printStackTrace();
            msg="未知";
        }
        return msg;
    }

    private boolean loginErr(){
        int judgement;
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            judgement = jsonObject.getInt("errorCode");
        } catch (Exception e) {
            judgement = 99;
            e.printStackTrace();
        }
        return judgement != 0;
    }



    @SuppressLint("HandlerLeak")
    private class MHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            HashMap<String,Object> map = (HashMap<String, Object>) msg.obj;
            responseData = (String) map.get("responseData");
            setCookies = (List<String>)map.get("setCookies");
            continueLoginProcess();
        }
    }
}