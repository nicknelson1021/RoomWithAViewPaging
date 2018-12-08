package org.nicknelson.roomwithaviewpaging;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private WordRepository mRepository;

    private LiveData<List<WordEntity>> mAllWords;

    public WordViewModel (Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    LiveData<List<WordEntity>> getAllWords() {
        return mAllWords;
    }

    public void insert(WordEntity word) {
        mRepository.insert(word);
    }

    public void delete(WordEntity word) {
        mRepository.delete(word);
    }

    public void update(WordEntity word) {
        mRepository.update(word);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

}
