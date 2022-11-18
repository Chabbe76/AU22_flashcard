package com.example.au22_flashcard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope{

    lateinit var wordView : TextView
    var currentWord : Word? = null
    //val wordList = WordList()
    private lateinit var job: Job
    var allWordList = mutableListOf<Word>()
    var wordUsed = mutableListOf<Word>()

    override val coroutineContext: CoroutineContext
        get()= Dispatchers.Main+job

    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job=Job()

        var addWordBnt=findViewById<Button>(R.id.addingBtn)
        wordView = findViewById(R.id.wordTextView)

        db = AppDatabase.getInstance(this)

        showNewWord()

        wordView.setOnClickListener {
            revealTranslation()
        }
        addWordBnt.setOnClickListener {
            val intent= Intent(this, AddWord::class.java)
            startActivity(intent)
        }//Lägga in ord

    }
    fun loadAllWords(): Deferred<List<Word>> =
        async (Dispatchers.IO){
            db.wordDao.getAllWords()
        }
//hämtar alla ord
    fun revealTranslation() {
        wordView.text = currentWord?.english
    }
    fun showNewWord() {
        val list=loadAllWords()//här kommer in alla ord
        launch {
            val wordList=list.await()
            for (word in wordList){
                allWordList.add(word)
                Log.d("!!!","item ${word.swedish}")
            }
            currentWord=getNewWord(wordList)
            wordView.text = currentWord?.swedish
        }
        allWordList.clear()
        Log.d("!!!","currentWord: ${currentWord?.english}")
        Log.d("!!!","list contains: ${allWordList.size}")
    }
    fun getNewWord(list:List<Word>) : Word {
        if (list.size == wordUsed.size) {
            wordUsed.clear()
        }
        Log.d("!!!","used list contains: ${wordUsed.size}")
        var word : Word? = null
        do {
            val rnd = (0 until list.size).random()
            word = list[rnd]
        } while(wordUsed.contains(word))
        wordUsed.add(word!!)

        return word
    }
    fun delete(word: Word){
        launch(Dispatchers.IO){
            db.wordDao.deleteWord(word)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            showNewWord()
        }
        return true
    }


    }


// override fun onCreate(savedInstanceState: Bundle?) {
// super.onCreate(savedInstanceState)
// setContentView(R.layout.activity_main)
// job = Job()
// db = AppDatabase.getInstance(this)
//
// launch {
// val list
// val itemList = list.await()
//
// for(item in itemList) {
// Log.d("!!!", "item: $item")
// }
// }
//
//
// fun delete(word: Word) =
// launch(Dispatchers.IO) {
// db.wordDao.delete(word)
// }
//
//
//
// fun loadAllItems() : Deferred<List<Word>> =
// async(Dispatchers.IO) {
// db.wordDao.readAllData()
// }
//
// fun saveItem(word: Word) {
// launch(Dispatchers.IO) {
// db.wordDao.insert(word)
// }
// }
//
//
//
//
// }}
// //Vad ska göras:
//
// //1. skapa en ny aktivitet där ett nytt ord får skrivas in
// //2. spara det nya ordet i databasen.
//
// //3. I main activity läs in alla ord från databasen
//
// // (anväd coroutiner när ni läser och skriver till databasen se tidigare exempel)