package com.example.protopendu

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //this.getSharedPreferences("GAME_PROGRESS", 0).edit().clear().commit();
    }

    override fun onStart(){
        super.onStart()
        val btnEasy = findViewById<Button>(R.id.menu_btn_easy)
        val btnNormal = findViewById<Button>(R.id.menu_btn_normal)
        val btnHard = findViewById<Button>(R.id.menu_btn_hard)

        btnEasy.setOnClickListener(this);
        btnNormal.setOnClickListener(this);
        btnHard.setOnClickListener(this);

        val imageView = findViewById<ImageView>(R.id.menu_icon)
        imageView.setImageResource(R.drawable.game_10);

        val sharedPreference = getSharedPreferences("GAME_PROGRESS", Context.MODE_PRIVATE)
        val easyBeaten = sharedPreference.getBoolean("easy_beaten",false)
        val normalBeaten = sharedPreference.getBoolean("normal_beaten",false)

        btnNormal.isEnabled = easyBeaten;
        btnNormal.isClickable = easyBeaten;

        btnHard.isEnabled = normalBeaten;
        btnHard.isClickable = normalBeaten;
    }

    override fun onClick(v: View?) {

        var difficulty = -1;

        when(v!!.id){
            R.id.menu_btn_easy -> difficulty = 0;
            R.id.menu_btn_normal -> difficulty = 1;
            R.id.menu_btn_hard -> difficulty = 2;
        }

        val intent = Intent(this, GameActivity::class.java)
        //transmettre la difficulté choisir :
        intent.putExtra("difficulty", difficulty)
        startActivity(intent);
    }
}