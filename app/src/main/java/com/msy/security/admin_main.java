package com.msy.security;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class admin_main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //**************************************

    private DrawerLayout drawer;
    public static String alınantc;
    public static String alınanisim;
    public static String saha;
    //**************************************

    private static boolean sharedadminbln;
    private boolean sharedtombln;

    // Tarih
    public String date;

    private Button admin_list_tamamlananlar;
    private Button admin_list_user;
    private Button admin_kullanıcı_arabtn;
    private Button admin_list_sahalar;
    private Button admin_sahaekle;
    private Button admin_list_sorunlar;
    private Button admin_list_aylik_tamamlama;
    private Button admin_list_giriscikis;
    private Button admin_yillikizinekle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        tc_sahaalma();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        date = df.format(Calendar.getInstance().getTime());

        draver();



        admin_list_tamamlananlar = (Button) findViewById(R.id.admin_list_tamamlananlar);
        admin_list_user = (Button) findViewById(R.id.admin_list_user);
        admin_kullanıcı_arabtn = (Button) findViewById(R.id.admin_list_user_search);
        admin_list_sahalar = (Button) findViewById(R.id.admin_list_sahalar);
        admin_sahaekle = (Button) findViewById(R.id.admin_sahaekle);
        admin_list_sorunlar = (Button) findViewById(R.id.admin_sorunlar);
        admin_list_aylik_tamamlama = (Button) findViewById(R.id.admin_list_aylıktamamlamalar);
        admin_list_giriscikis = (Button) findViewById(R.id.admin_giriscikis);
        admin_yillikizinekle = (Button) findViewById(R.id.admin_yillikizinekle);


        admin_list_aylik_tamamlama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin_list_aylik_tamamlama();
            }
        });

        admin_list_tamamlananlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin_list_tamamlananlar();
            }
        });

        admin_list_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin_list_user();
            }
        });

        admin_kullanıcı_arabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin_list_user_ara();
            }
        });

        admin_list_sahalar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin_list_sahalar();
            }
        });

        admin_sahaekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin_sahaekle();
            }
        });

        admin_list_sorunlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin_list_sorunlar();
            }
        });

        admin_list_giriscikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin_list_giriscikis();
            }
        });

        admin_yillikizinekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin_yillikizinekle();
            }
        });


    }

    private void admin_yillikizinekle() {
        Intent admin_yillikizinekle = new Intent(admin_main.this, admin_yillikizin.class);
        startActivity(admin_yillikizinekle);
        finish();
    }


    private void admin_list_aylik_tamamlama() {
        Intent admin_list_aylik_tamamlama = new Intent(admin_main.this, admin_list_aylik_tamamlama.class);
        startActivity(admin_list_aylik_tamamlama);
        finish();
    }

    private void admin_list_sorunlar() {
        Intent admin_list_sorunlar = new Intent(admin_main.this, admin_list_sorunlar.class);
        startActivity(admin_list_sorunlar);
        finish();
    }

    private void admin_list_sahalar() {
        Intent admin_list_sahalar = new Intent(admin_main.this, admin_list_sahalar.class);
        startActivity(admin_list_sahalar);
        finish();
    }

    private void admin_list_user() {
        Intent admin_list_user = new Intent(admin_main.this, admin_list_user.class);
        startActivity(admin_list_user);
        finish();
    }

    private void admin_list_user_ara() {
        Intent admin_list_user_ara = new Intent(admin_main.this, admin_kullanici_ara.class);
        startActivity(admin_list_user_ara);
        finish();
    }

    private void admin_sahaekle() {
        Intent admin_sahaekle = new Intent(admin_main.this, admin_sahaekleme.class);
        startActivity(admin_sahaekle);
        finish();
    }

    private void admin_list_tamamlananlar() {
        Intent admin_list_tamamlananlar = new Intent(admin_main.this, admin_list_tamamlananlar.class);
        startActivity(admin_list_tamamlananlar);
        finish();
    }

    private void admin_list_giriscikis() {
        Intent admin_list_giriscikis = new Intent(admin_main.this, admin_list_giriscikis.class);
        startActivity(admin_list_giriscikis);
        finish();
    }




    private void tc_sahaalma() {

        SharedPreferences sharedtom = this.getSharedPreferences("sharedtom",Context.MODE_PRIVATE);
        sharedtombln = sharedtom.getBoolean("sharedtom",false);

        //Shared Preferences tc alma
        SharedPreferences sharedtc = this.getSharedPreferences("sharedtc", Context.MODE_PRIVATE);
        alınantc = sharedtc.getString("tc",null);
        //Shared Preferences isim alma

        SharedPreferences sharedisim = this.getSharedPreferences("sharedisim", Context.MODE_PRIVATE);
        alınanisim = sharedisim.getString("isim",null);

        // Shared Preferences ile sahakodu alma
        SharedPreferences sharedsahakodu = this.getSharedPreferences("sharedsahakodu",Context.MODE_PRIVATE);
        saha = sharedsahakodu.getString("sahakodu",null);
        // Shared Preferences ile admin alma
        SharedPreferences sharedadmin = this.getSharedPreferences("sharedadmin",Context.MODE_PRIVATE);
        sharedadminbln = sharedadmin.getBoolean("sharedadmin",false);
        //******************************************************************************************************************************
    }

    // NavigationView işlemleri
    //*********************************************************************************************************************
    private void draver() {
        //******************************************************************************************************************************

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_admin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        drawer = findViewById(R.id.drawer_layout_admin);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        NavigationView navigationView = findViewById(R.id.naw_view_admin);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navisim = headerView.findViewById(R.id.menu_isim);
        navisim.setText(alınanisim);
        TextView navtc = headerView.findViewById(R.id.menu_tc);
        navtc.setText(alınantc);
        TextView navsaha = (TextView) headerView.findViewById(R.id.menu_saha);
        navsaha.setText(saha);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.menu_barkod);
        Menu menu = navigationView.getMenu();
        for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
            MenuItem menuItem= menu.getItem(menuItemIndex);
            if(menuItem.getItemId() == R.id.menu_barkod){
                menuItem.setVisible(sharedtombln);
            }
            if(menuItem.getItemId() == R.id.menu_admin){
                menuItem.setVisible(sharedadminbln);
            }
        }
        //******************************************************************************************************************************


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_admin:
                Intent adminsayfasına = new Intent(admin_main.this, admin_main.class);
                startActivity(adminsayfasına);
                finish();
                break;

            case R.id.menu_giriscikis:
                Intent giriscikissayfasina = new Intent(admin_main.this, giriscikis.class);
                startActivity(giriscikissayfasina);
                finish();
                break;

            case R.id.menu_barkod:
                Intent barkodsayfasına = new Intent(admin_main.this,MainActivity.class);
                startActivity(barkodsayfasına);
                finish();
                break;
            case R.id.menu_yardım_ic:
                Intent icyardımsayfasına = new Intent(admin_main.this,yardimekrani_ic.class);
                startActivity(icyardımsayfasına);
                finish();
                break;
            case R.id.menu_sorun:
                Intent sorunbildirsayfasına = new Intent(admin_main.this,sorunbildir.class);
                startActivity(sorunbildirsayfasına);
                finish();
                break;
            case R.id.menu_kisiselbilgiler:
                Intent kisiselbilgilersayfasına = new Intent(admin_main.this,kisisel_bilgiler.class);
                startActivity(kisiselbilgilersayfasına);
                finish();
                break;
            case R.id.menu_kisiselraporlar:
                Intent kisiseraporlarsayfasına = new Intent(admin_main.this,kisisel_rapor.class);
                startActivity(kisiseraporlarsayfasına);
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
    //*********************************************************************************************************************
}