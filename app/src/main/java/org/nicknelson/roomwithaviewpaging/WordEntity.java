package org.nicknelson.roomwithaviewpaging;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.util.Log;

import java.util.Date;

@Entity(tableName = "WordEntity")
@TypeConverters(DateConverter.class)
public class WordEntity {

    @PrimaryKey(autoGenerate = true)
    int mWordId;

    public String mWord;

    Date mCreateDate;

    boolean mIsSelected;

    private int getWordId() {
        return mWordId;
    }

    void setWordId(int wordId) {
        this.mWordId = wordId;
    }

    @NonNull
    String getWord() {
        return mWord;
    }

    void setWord(@NonNull String word) {
        this.mWord = word;
    }

    @NonNull
    Date getCreateDate() {
        return mCreateDate;
    }

    void setCreateDate(@NonNull Date createDate) {
        this.mCreateDate = createDate;
    }

    boolean getIsSelected() { return mIsSelected; }

    void setIsSelected(boolean isSelected) { this.mIsSelected = isSelected; }

    static DiffUtil.ItemCallback<WordEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<WordEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull WordEntity oldItem, @NonNull WordEntity newItem) {
            Log.i("CLEAN_LOG","areItemsTheSame: " +
                    Boolean.toString(oldItem.getWordId() == newItem.getWordId()));

            return oldItem.mWordId == newItem.mWordId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull WordEntity oldItem, @NonNull WordEntity newItem) {
            Log.i("CLEAN_LOG","oldItem: " +
                    Boolean.toString(oldItem.getIsSelected()));
            Log.i("CLEAN_LOG","newItem: " +
                    Boolean.toString(newItem.getIsSelected()));
            Log.i("CLEAN_LOG","areContentsTheSame: " +
                    Boolean.toString(oldItem.getIsSelected() == newItem.getIsSelected()));
            return false;
            //return oldItem.getIsSelected() == newItem.getIsSelected();
        }
    };
}
