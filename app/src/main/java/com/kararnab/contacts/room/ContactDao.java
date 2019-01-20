package com.kararnab.contacts.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

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
