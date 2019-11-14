package com.desarrollandoapps.senamarket.calendario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.desarrollandoapps.senamarket.MainActivity;
import com.desarrollandoapps.senamarket.calendario.NuevoEventoCalendarioActivity;
import com.desarrollandoapps.senamarket.R;
import com.desarrollandoapps.senamarket.calendario.VerCalendariosActivity;

public class CalendarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void lanzarCalendario(View v) {
        Intent i = new Intent(this, VerCalendariosActivity.class);
        startActivity(i);
    }

    public void lanzarNuevoEventoCalendario(View v) {
        Intent i = new Intent(this, NuevoEventoCalendarioActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ir_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_ir_inicio:
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
