package com.ocheresh.ft_hangouts;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ocheresh.ft_hangouts.Model.Abonent;
import com.ocheresh.ft_hangouts.Model.DBAbonents;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.OnItemListener {

    public static RecyclerView recyclerView;
    public static int theme = R.style.AppThemeRed;
    List <Abonent> abonents;
    ArrayList <String> temp = new ArrayList<String>();
    public static DBAbonents dbAbonents;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(theme);
        setContentView(R.layout.activity_main);
        dbAbonents = new DBAbonents(this);
        abonents = dbAbonents.readData();
        try {
            Collections.sort(abonents);
        }catch (Exception | NoClassDefFoundError e) {
            Log.e("Error", e.getMessage().toString());
        }

        recyclerView = findViewById(R.id.recycleid);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter(MainActivity.this, abonents, this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddAbonent.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        abonents = dbAbonents.readData();
        try {
            Collections.sort(abonents);
        }catch (Exception | NoClassDefFoundError e) {
            Log.e("Error", e.getMessage().toString());
        }
        recyclerView.setAdapter(new Adapter(MainActivity.this, abonents, this));
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_colors, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.color_settings :
                theme = R.style.AppTheme;
                Intent intent = new Intent(this, MainActivity.class);
                change_color(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void change_color(final Intent intent){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View dialog_layout = inflater.inflate(R.layout.dialog_colors, null);
        alert.setTitle("Choose color");
        alert.setView(dialog_layout);
        final RadioButton rb_green = dialog_layout.findViewById(R.id.button_green);
        final RadioButton rb_red = dialog_layout.findViewById(R.id.button_red);
        final RadioButton rb_yellow = dialog_layout.findViewById(R.id.button_yellow);

        rb_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme = R.style.AppTheme;
                startActivity(intent);
                finish();
            }
        });

        rb_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme = R.style.AppThemeRed;
                startActivity(intent);
                finish();
            }
        });

        rb_yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme = R.style.AppThemeYellow;
                startActivity(intent);
                finish();
            }
        });
        alert.show();
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(this, InformAbonent.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
