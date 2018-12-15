package org.nicknelson.roomwithaviewpaging;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.AsyncTask;

public class WordRepository {

    private WordDao mWordDao;
    private LiveData<PagedList<WordEntity>> mAllWords;

    WordRepository(Application application) {

        RoomDatabase db = RoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder()).setEnablePlaceholders(true)
                        .setPrefetchDistance(20)
                        .setPageSize(40).build();

        mAllWords = new LivePagedListBuilder<>(mWordDao.getAllWords(), pagedListConfig).build();
        // mAllWords = mWordDao.getAllWords();
    }

    LiveData<PagedList<WordEntity>> getAllWords() {
        return mAllWords;
    }

    public void delete (WordEntity word) {
        new deleteAsyncTask(mWordDao).execute(word);
    }
    void update (WordEntity word) {
        new updateAsyncTask(mWordDao).execute(word);
    }
    void insert (WordEntity word) {
        new insertAsyncTask(mWordDao).execute(word);
    }
    void deleteAll () { new WordRepository.deleteAllAsyncTask(mWordDao).execute(); }

    private static class insertAsyncTask extends AsyncTask<WordEntity, Void, Void> {
        private WordDao mAsyncTaskDao;
        insertAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WordEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<WordEntity, Void, Void> {
        private WordDao mAsyncTaskDao;
        updateAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WordEntity... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<WordEntity, Void, Void> {
        private WordDao mAsyncTaskDao;
        deleteAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WordEntity... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private WordDao mAsyncTaskDao;
        deleteAllAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

}
