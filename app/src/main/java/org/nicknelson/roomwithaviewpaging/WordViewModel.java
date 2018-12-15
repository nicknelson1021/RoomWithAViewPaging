package org.nicknelson.roomwithaviewpaging;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

public class WordViewModel extends AndroidViewModel {

    private WordRepository mRepository;

    private LiveData<PagedList<WordEntity>> mAllWords;

    public WordViewModel (Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    LiveData<PagedList<WordEntity>> getAllWords() {
        return mAllWords;
    }

    void insert(WordEntity word) {
        mRepository.insert(word);
    }

    public void delete(WordEntity word) {
        mRepository.delete(word);
    }

    void update(WordEntity word) {
        mRepository.update(word);
    }

    void deleteAll() {
        mRepository.deleteAll();
    }

}
