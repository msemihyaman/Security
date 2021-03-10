package com.msy.security;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class admin_list_user extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


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

    private DatabaseReference mDatabase,mDatabase2,mDatabase3;
    private ListView listView;
    private Tamamlananlaradapter adapter2;
    private final ArrayList<tamamlama> tamamlamaArrayList = new ArrayList<>();

    private String listeden_alınan_item ;
    private String path = "Kullanıcılar";

    private Button admin_list_geridönme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_user);

        //******************************************************************************************************************************

        // Tc ve saha bilgisini sharedpreferencesden alma
        tc_sahaalma();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        date = df.format(Calendar.getInstance().getTime());

        // NavigationView işlemleri
        drawer();



        mDatabase = FirebaseDatabase.getInstance().getReference(path);
        listView = (ListView) findViewById(R.id.listview1);

        //--------------------
        adapter2= new Tamamlananlaradapter(this,R.layout.adapter_view_layout,tamamlamaArrayList);
        listView.setAdapter(adapter2);
        //-------------------


        admin_list_geridönme = (Button) findViewById(R.id.admin_list_geridönme);
        admin_list_geridönme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!path.equals("Kullanıcılar/")) {
                    geridön();
                }
                else {
                    Intent adminsayfasına = new Intent(admin_list_user.this, admin_main.class);
                    startActivity(adminsayfasına);
                    finish();
                }

            }
        });


        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String data = snapshot.getKey();
                String data2 = snapshot.getValue().toString();
                tamamlama son = new tamamlama(data,"");
                tamamlamaArrayList.add(son);
                adapter2.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                adapter2.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                adapter2.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView item = (TextView) view.findViewById(R.id.list_textview1);
                listeden_alınan_item = item.getText().toString();

                listealma(listeden_alınan_item);
            }
        });
    }

    private void geridön() {

        String b = path;
        String a = b.substring(0, b.lastIndexOf("/"));
        path = a;

        mDatabase3 = FirebaseDatabase.getInstance().getReference(path);
        tamamlamaArrayList.clear();
        mDatabase3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot3, @Nullable String previousChildName) {
                String data = snapshot3.getKey();


                if (!snapshot3.hasChildren()){

                    String data2 = snapshot3.getValue().toString();
                    tamamlama son3 = new tamamlama(data,data2);
                    tamamlamaArrayList.add(son3);
                    adapter2.notifyDataSetChanged();
                    listView.setEnabled(false);
                }
                else {

                    tamamlama son4 = new tamamlama(data,"");
                    tamamlamaArrayList.add(son4);
                    adapter2.notifyDataSetChanged();
                    listView.setEnabled(true);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void listealma(final String listeden_alınan_item) {

        adapter2.notifyDataSetChanged();
        path = path + "/" + listeden_alınan_item;

        mDatabase2 = FirebaseDatabase.getInstance().getReference(path);
        tamamlamaArrayList.clear();
        mDatabase2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot2, @Nullable String previousChildName) {

                String data = snapshot2.getKey();


                if (!snapshot2.hasChildren()){

                    String data2 = snapshot2.getValue().toString();
                    tamamlama son2 = new tamamlama(data,data2);
                    tamamlamaArrayList.add(son2);
                    adapter2.notifyDataSetChanged();
                    listView.setEnabled(false);

                }
                else {

                    tamamlama son3 = new tamamlama(data,"");
                    tamamlamaArrayList.add(son3);
                    adapter2.notifyDataSetChanged();
                    listView.setEnabled(true);

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot2, @Nullable String previousChildName) {

                adapter2.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot2) {

                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot2, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    private void drawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_admin_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        drawer = findViewById(R.id.drawer_layout_admin_list);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        NavigationView navigationView = findViewById(R.id.naw_view_admin_list);
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
        TextView navdate = (TextView) headerView.findViewById(R.id.nawdate);
        navdate.setText(date);
        Menu menu = navigationView.getMenu();
        for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
            MenuItem menuItem= menu.getItem(menuItemIndex);
            if(menuItem.getItemId() == R.id.menu_admin){
                menuItem.setVisible(sharedadminbln);
            }
            if(menuItem.getItemId() == R.id.menu_barkod){
                menuItem.setVisible(sharedtombln);
            }
        }
        //******************************************************************************************************************************

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_admin:
                Intent adminsayfasına = new Intent(admin_list_user.this, admin_main.class);
                startActivity(adminsayfasına);
                finish();
                break;

            case R.id.menu_giriscikis:
                Intent giriscikissayfasina = new Intent(admin_list_user.this, giriscikis.class);
                startActivity(giriscikissayfasina);
                finish();
                break;

            case R.id.menu_barkod:
                Intent barkodsayfasına = new Intent(admin_list_user.this, MainActivity.class);
                startActivity(barkodsayfasına);
                finish();
                break;
            case R.id.menu_yardım_ic:
                Intent icyardımsayfasına = new Intent(admin_list_user.this,yardimekrani_ic.class);
                startActivity(icyardımsayfasına);
                finish();
                break;
            case R.id.menu_sorun:
                Intent sorunbildirsayfasına = new Intent(admin_list_user.this,sorunbildir.class);
                startActivity(sorunbildirsayfasına);
                finish();
                break;
            case R.id.menu_kisiselbilgiler:
                Intent kisiselbilgilersayfasına = new Intent(admin_list_user.this,kisisel_bilgiler.class);
                startActivity(kisiselbilgilersayfasına);
                finish();
                break;
            case R.id.menu_kisiselraporlar:
                Intent kisiseraporlarsayfasına = new Intent(admin_list_user.this,kisisel_rapor.class);
                startActivity(kisiseraporlarsayfasına);
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (!path.equals("Kullanıcılar")) {
            geridön();
        }
        else {
            Intent adminsayfasına = new Intent(admin_list_user.this, admin_main.class);
            startActivity(adminsayfasına);
            finish();
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        }
    }
}