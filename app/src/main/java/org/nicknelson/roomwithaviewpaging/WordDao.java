package org.nicknelson.roomwithaviewpaging;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WordEntity word);

    @Update
    void update(WordEntity word);

    @Delete
    void delete(WordEntity word);

    @Query("DELETE FROM WordEntity")
    void deleteAll();

    @Query("SELECT * from WordEntity ORDER BY mWord ASC")
    DataSource.Factory<Integer, WordEntity> getAllWords();
    // LiveData<List<WordEntity>> getAllWords();

}
