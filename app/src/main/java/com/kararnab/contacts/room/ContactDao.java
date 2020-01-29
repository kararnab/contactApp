package com.kararnab.contacts.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDao {

    /**
     * Tip: When inserting data, you can provide a conflict strategy.
     *
     * In this codelab, you do not need a conflict strategy, because the word is your primary key, and the default SQL behavior is ABORT, so that you cannot insert two items with the same primary key into the database.
     * If the table has more than one column, you can use @Insert(onConflict = OnConflictStrategy.REPLACE)
     * to replace a row.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contact word);

    //There is no convenience annotation for deleting multiple entities, so annotate the method with the generic @Query
    @Query("delete from contact_table")
    void deleteAll();

    @Query("SELECT * from contact_table ORDER BY name ASC")
    LiveData<List<Contact>> getAlphabetizedWords();

    @Query("SELECT * from contact_table where name like :s1+'%' ORDER BY name ASC")
    LiveData<List<Contact>> filterWords(String s1);
}
