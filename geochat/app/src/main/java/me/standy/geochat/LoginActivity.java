package me.standy.geochat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;


public class LoginActivity extends Activity {
    private Button logInButton;
    private final static String APP_ID = "4636866";
    private final static String TOKEN_NAME = "vk_access_token";
    private final static String[] myScope = {VKScope.AUDIO};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logInButton = (Button) findViewById(R.id.logInButton);
        logInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                VKSdk.initialize(new VKSdkListener() {
                    @Override
                    public void onCaptchaError(VKError captchaError) {
                        new VKCaptchaDialog(captchaError).show();
                    }

                    @Override
                    public void onTokenExpired(VKAccessToken expiredToken) {
                        VKSdk.authorize(myScope);
                    }


                    @Override
                    public void onReceiveNewToken(VKAccessToken newToken) {
                        newToken.saveTokenToSharedPreferences(LoginActivity.this,TOKEN_NAME);
                    }

                    @Override
                    public void onAccessDenied(VKError authorizationError) {
                        new AlertDialog.Builder(LoginActivity.this).setMessage(authorizationError.errorMessage).show();
                    }
                }, APP_ID,VKAccessToken.tokenFromSharedPreferences(LoginActivity.this,TOKEN_NAME));

            }
        });
        VKSdk.authorize(myScope, true, false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
