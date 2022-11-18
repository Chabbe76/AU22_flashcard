package com.example.au22_flashcard

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordDao {

    @Insert
    fun insert(word: Word)

    @Delete
    fun deleteWord(word: Word)

    @Query("SELECT * FROM word_table")
    fun getAllWords() : List<Word>


    // delete

    // getAllwords

}