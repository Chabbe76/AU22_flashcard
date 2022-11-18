package com.example.au22_flashcard

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddWord: AppCompatActivity(), CoroutineScope{

    private lateinit var db : AppDatabase
    lateinit var sweInputText: EditText
    lateinit var engInputText: EditText
    private lateinit var job: Job
    //val wordList = WordList()


    override val coroutineContext: CoroutineContext
        get()= Dispatchers.Main+job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)

        sweInputText=findViewById(R.id.sweInputText)
        engInputText=findViewById(R.id.engInputTxt)
        var inputBtn=findViewById<Button>(R.id.inputBtn)
        var exitBtn=findViewById<Button>(R.id.exitBtn)

        job=Job()
        db= AppDatabase.getInstance(this)

        inputBtn.setOnClickListener {
            saveWord()
        }
        exitBtn.setOnClickListener {
            finish()
        }

    }

    private fun saveWord() {
        val swedish= sweInputText.text.toString()
        val english= engInputText.text.toString()
        if(swedish!= "" && english!="") {
            val word = Word(0, swedish=swedish, english=english)

            insert(word)
            finish()
        }
    }
    fun insert(word:Word){
        launch(Dispatchers.IO){
            db.wordDao.insert(word)
        }
    }




}