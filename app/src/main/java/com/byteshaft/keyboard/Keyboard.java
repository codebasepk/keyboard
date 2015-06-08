package com.byteshaft.keyboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class Keyboard extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
    }
}
