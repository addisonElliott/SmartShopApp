package com.smartshop.smartshopapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.READ_CONTACTS;
import static android.view.View.GONE;

public class SplashActivity extends AppCompatActivity
{
    @BindView(R.id.connect_progress)
    ProgressBar progressBar;
    @BindView(R.id.server)
    EditText serverEditText;
    @BindView(R.id.port)
    EditText portEditText;
    @BindView(R.id.connect_button)
    Button connectButton;

    @BindView(R.id.connect_form)
    ScrollView connectForm;

    @BindString(R.string.prompt_port_default)
    String defaultPort;
    @BindString(R.string.error_field_required)
    String errorRequiredField;

    SharedPreferences preferences = null;

    TcpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize all variables annotated with @BindView and other variants
        ButterKnife.bind(this);

        portEditText.setText(defaultPort);

        // Get preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String serverName = preferences.getString("server", "");
        int portNumber = preferences.getInt("port", -1);

        if (serverName.length() == 0 || portNumber == -1)
        {
            connect(serverName, portNumber);
        }
    }

    @OnClick(R.id.connect_button)
    public void connectButton_onClick(View v)
    {
        // Reset errors.
        serverEditText.setError(null);
        portEditText.setError(null);

        // Store values at the time of the login attempt.
        String server = serverEditText.getText().toString();
        String port = portEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(server))
        {
            serverEditText.setError(errorRequiredField);
            focusView = serverEditText;
            cancel = true;
        }
        else if (TextUtils.isEmpty(port))
        {
            portEditText.setError(errorRequiredField);
            focusView = portEditText;
            cancel = true;
        }

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first form field with an error.
            focusView.requestFocus();
        }
        else
        {
            connect(server, Integer.valueOf(port));
        }
    }

    public void connect(String server, int port)
    {
        client = new TcpClient(server, port);
        if (client.connect())
        {
            Toast.makeText(getApplicationContext(), "Connected successfully!", Toast.LENGTH_SHORT).show();
            client.start();
            client.sendMessage("testing");

            // Go to main activity
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show)
    {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow for very easy
        // animations. If available, use these APIs to fade-in the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            connectForm.setVisibility(show ? View.GONE : View.VISIBLE);
            connectForm.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(
                    new AnimatorListenerAdapter()
                    {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            connectForm.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(
                    new AnimatorListenerAdapter()
                    {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });
        }
        else
        {
            // The ViewPropertyAnimator APIs are not available, so simply show and hide the
            // relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            connectForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

