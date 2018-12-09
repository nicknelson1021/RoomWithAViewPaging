package org.nicknelson.roomwithaviewpaging;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.Date;

@Entity(tableName = "WordEntity")
@TypeConverters(DateConverter.class)
public class WordEntity {

    @PrimaryKey(autoGenerate = true)
    public int mWordId;

    @NonNull
    public String mWord;

    @NonNull
    public Date mCreateDate;

    @NonNull
    public boolean mIsSelected;

    int getWordId() {
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

    @NonNull
    boolean getIsSelected() { return mIsSelected; }

    void setIsSelected(@NonNull boolean isSelected) { this.mIsSelected = isSelected; }

    public static DiffUtil.ItemCallback<WordEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<WordEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull WordEntity oldItem, @NonNull WordEntity newItem) {
            return oldItem.mWordId == newItem.mWordId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull WordEntity oldItem, @NonNull WordEntity newItem) {
            return oldItem.mWordId == newItem.mWordId
                    && oldItem.mIsSelected == newItem.mIsSelected
                    && oldItem.mWord.equals(newItem.mWord)
                    && oldItem.mCreateDate == newItem.mCreateDate;
            //return oldItem == newItem;
        }
    };
}
