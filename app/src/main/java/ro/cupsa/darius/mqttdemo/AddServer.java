package ro.cupsa.darius.mqttdemo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ro.cupsa.darius.mqttdemo.constants.Constants;


public class AddServer extends AppCompatActivity {
    /**
     * Log class activity
     */
    private static final String LOGGER = AddServer.class.getSimpleName();

    private Button button;
    private EditText hostInput;
    private EditText portInput;
    private EditText userInput;
    private EditText passwordInput;

    private String host;
    private int port;
    private String user;
    private String password;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(Constants.STORAGE, Context.MODE_PRIVATE);

        host = preferences.getString("host", "");
        port = preferences.getInt("port", 0);
        user = preferences.getString("user", "");
        password = preferences.getString("password", "");

        if (validCredentials(host, port, user, password)) {
            Log.d(LOGGER, "Settings already in storage");
            moveAlong();
        }

        setContentView(R.layout.activity_add_server);

        button = (Button) findViewById(R.id.add_server_button);
        hostInput = (EditText)findViewById(R.id.host);
        portInput = (EditText)findViewById(R.id.port);
        userInput = (EditText)findViewById(R.id.user);
        passwordInput = (EditText)findViewById(R.id.password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                host = hostInput.getText().toString();
                port = portInput.getText().toString().length() > 0 ? Integer.parseInt(portInput.getText().toString()) : 0;
                user = userInput.getText().toString();
                password = passwordInput.getText().toString();

                Boolean hasError = false;
                if (host.length() <= 0) {
                    hasError = true;
                    hostInput.setError("Host field can't be empty.");
                }
                if (port <= 0) {
                    hasError = true;
                    portInput.setError("Port field can't be empty.");
                }
                if (user.length() <= 0) {
                    hasError = true;
                    userInput.setError("User field can't be empty.");

                }if (password.length() <= 0) {
                    hasError = true;
                    passwordInput.setError("Password field can't be empty.");
                }
                if (!hasError) {
                    saveNewSettings(host, port, user, password);
                    moveAlong();
                }
            }
        });

    }

    protected void saveNewSettings(String host, int port, String user, String password) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("host", host);
        editor.putInt("port", port);
        editor.putString("user", user);
        editor.putString("password", password);

        editor.apply();
    }

    protected boolean validCredentials(String host, int port, String user, String password) {
        return  host.length() > 0 && port > 0 && user.length() > 0 && password.length() > 0;
    }

    protected void moveAlong() {
        Intent move = new Intent(AddServer.this, Controller.class);

        move.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(move);

        finish();
    }
}

