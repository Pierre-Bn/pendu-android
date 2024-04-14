package com.example.protopendu

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.lang.StringBuilder

class GameActivity : AppCompatActivity(), View.OnClickListener {

    var buttonArray: Array<Button?> = arrayOfNulls(26);
    var difficulty = -1;
    var guesses = 10;

    val words: Array<String> = arrayOf("Test", "Football", "Livre", "Chene", "Guitare");
    var secretWord = "";
    var displayWord = "";
    var displayWordSb: StringBuilder? = null;
    var displayWordView: TextView? = null;
    var displayGuessesView: TextView? = null;
    var imageView: ImageView? = null;
    var foundLetters = Array<Boolean>(0) {false}


    override fun onCreate(savedInstanceState: Bundle?) {
        //initial setup
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        setupBtnArray();
        displayWordView = findViewById(R.id.game_display_word);
        displayGuessesView = findViewById(R.id.game_guess_count);
        imageView = findViewById(R.id.game_image);

        //word setup
        val intent: Intent = intent;
        difficulty = intent.getIntExtra("difficulty", -1);
        secretWord = words[(0..4).random()]
        for(letter in secretWord){
            displayWord += "-";
            foundLetters += false;
        }
        displayWordSb = StringBuilder(displayWord)
        foundLetters = Array<Boolean>(secretWord.length) {false}
        when(difficulty){
            -1 -> Log.e("game", "Erreur lors de la lecture de la difficulté")
            0 -> {
                    displayWordSb!!.setCharAt(0,secretWord[0]);
                    displayWordSb!!.setCharAt(secretWord.length-1, secretWord[secretWord.length-1]);
                    foundLetters[0] = true;
                    foundLetters[secretWord.length-1] = true;
                }
            1 -> {
                    displayWordSb!!.setCharAt(0,secretWord[0])
                    foundLetters[0] = true;
                };
        }
        updateDisplay();
    }

    override fun onClick(v: View?){
        val b : Button = v as Button;
        val guess : String = b.text.toString();
        b.isEnabled = false;
        b.isClickable = false;

        var correctGuess = false;
        var isWon = true;
        for(i in 0..secretWord.length-1) {
            if (secretWord[i].toUpperCase().toString() == guess && !foundLetters[i]) {
                foundLetters[i] = true;
                correctGuess = true;
                displayWordSb!!.setCharAt(i, secretWord[i]);
            }
            if(!foundLetters[i]){
                isWon = false;
            }
        }

        if(isWon){
            Toast.makeText(this, getString(R.string.text_victory), Toast.LENGTH_SHORT).show()
            finish();
            if(difficulty < 2) {
                val sharedPreference = getSharedPreferences("GAME_PROGRESS", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                when(difficulty){
                    0 -> editor.putBoolean("easy_beaten", true)
                    1 -> editor.putBoolean("normal_beaten", true)
                }
                editor.commit()
            }
        }

        if(!correctGuess){
            guesses--;
            if(guesses == 0){
                Toast.makeText(this, getString(R.string.text_defeat), Toast.LENGTH_SHORT).show()
                finish();
            }
        }

        updateDisplay();
    }

    private fun setupBtnArray(){
        var resourceId = 0;
        var letter = 0;
        for(i in 0..25){
            letter = i + 65;
            resourceId = resources.getIdentifier(
                "game_btn_"+ letter.toChar(),
                "id",
                this.applicationContext.packageName
            );
            buttonArray[i] = findViewById(resourceId);
            buttonArray[i]!!.setOnClickListener(this);
        }
    }

    private fun updateDisplay(){
        displayWord = displayWordSb.toString()
        displayWordView!!.text = displayWord;
        displayGuessesView!!.text = guesses.toString() + " " + getString(R.string.guess_label);
        imageView!!.setImageResource(resources.getIdentifier(
            "game_"+ (10-guesses).toString(),
            "drawable",
            this.applicationContext.packageName
        ))
    }
}