package org.nicknelson.roomwithaviewpaging;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class MainActivity extends AppCompatActivity
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    EditText editText;
    Button button;
    private WordViewModel mWordViewModel;
    int wordCount;
    DividerItemDecoration itemDecor;
    WordListAdapter adapter;
    LinearLayout mainLayout;
    String LOG_TAG = "CLEAN_LOG";

    @Override
    protected void onResume() {
        super.onResume();
        runLayoutAnimation(recyclerView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize variables
        wordCount = 0;

        // initialize widgets
        recyclerView = findViewById(R.id.recycler_view);
        editText = findViewById(R.id.edit_text);
        button = findViewById(R.id.add_button);
        mainLayout = findViewById(R.id.main_layout);

        // instantiate RecyclerView divider
        itemDecor = new DividerItemDecoration(this, HORIZONTAL);

        adapter = new WordListAdapter();
        Log.i(LOG_TAG,"Has Stable Ids: " + Boolean.toString(adapter.hasStableIds()));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        // add list divider
        //recyclerView.addItemDecoration(itemDecor);

        Log.i(LOG_TAG,"Has Fixed Size: " + Boolean.toString(recyclerView.hasFixedSize()));

        // adding swipe-to-delete functionality
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                        this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        mWordViewModel.getAllWords().observe(this, new Observer<PagedList<WordEntity>>() {
            @Override
            public void onChanged(@Nullable final PagedList<WordEntity> words) {
                // Update the cached copy of the words in the adapter.
                adapter.submitList(words);
                if (words != null) {
                    wordCount = words.size();
                } else {
                    wordCount = 0;
                }
                Log.i(LOG_TAG,"Word Count: " + Integer.toString(wordCount));
            }
        });

        // add word when enter is pressed on keyboard
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()!=KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        buttonClick(null);
                        return true;
                    } }
                return false;
            }
        });
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    public void buttonClick(View view) {

        if (TextUtils.isEmpty(editText.getText())) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.toast,
                    Toast.LENGTH_LONG).show();
        } else {

            WordEntity word = new WordEntity();
            String wordStr = editText.getText().toString();
            editText.setText("");

            word.setWord(wordStr);
            word.setIsSelected(false);
            word.setCreateDate(new Date());

            mWordViewModel.insert(word);

        }
    }

    public void addWords(int amount) {
        WordEntity word;

        int i = 1;
        String wordStr;

        do {
            wordStr = String.format("%05d", i);
            word = new WordEntity();
            word.setWord(wordStr);
            word.setIsSelected(false);
            word.setCreateDate(new Date());
            mWordViewModel.insert(word);
            i++;
        } while (i <= amount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // override options menu and inflate layout
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_overflow, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (wordCount > 0) {
            menu.findItem(R.id.home_clear).setVisible(true);
        } else {
            menu.findItem(R.id.home_clear).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home_clear:
                mWordViewModel.deleteAll();
                break;
            case R.id.home_add:
                addWords(500);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        final WordEntity word = adapter.getCurrentList().get(position);
        mWordViewModel.delete(word);

        Snackbar snackbar = Snackbar
                .make(mainLayout, "Word deleted!", Snackbar.LENGTH_LONG);

        // insert deleted item back into the database when user clicks undo
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWordViewModel.insert(word);
            }
        });

        // add padding to bottom of layout when snackbar is displayed
        // prevents snackbar from covering the edit text
        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onShown(Snackbar transientBottomBar) {
                int height = transientBottomBar.getView().getHeight();
                mainLayout.setPadding(0, 0, 0, height);

                super.onShown(transientBottomBar);
            }

            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                mainLayout.setPadding(0, 0, 0, 0);

                super.onDismissed(transientBottomBar, event);
            }
        });

        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();

    }

    public class WordListAdapter extends PagedListAdapter<WordEntity, WordListAdapter.WordViewHolder> {

        WordListAdapter() {
            super(WordEntity.DIFF_CALLBACK);
            setHasStableIds(true);
        }

        @NonNull
        @Override
        public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_item, parent, false);
            return new WordViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {

            WordEntity current = getItem(position);

            if (current != null) {
                holder.bindTo(current);
            }
        }

        @Override
        public long getItemId(int position) {
            WordEntity current = getItem(position);
            Log.i(LOG_TAG,"Item ID: " + Integer.toString(current.mWordId));
            return current.mWordId;
        }

        class WordViewHolder extends RecyclerView.ViewHolder {

            TextView wordItemView;
            CheckBox checkBox;
            LinearLayout viewForeground;

            public void bindTo(WordEntity word) {
                wordItemView.setText(word.mWord + "_" + Integer.toString(word.mWordId));
                checkBox.setChecked(word.mIsSelected);
            }

            private WordViewHolder(View itemView) {
                super(itemView);
                viewForeground = itemView.findViewById(R.id.viewForeground);
                wordItemView = itemView.findViewById(R.id.textView);
                checkBox = itemView.findViewById(R.id.checkBox);

                checkBox.setClickable(false);

                viewForeground.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        final WordEntity thisWord = getItem(getAdapterPosition());
                        if (thisWord != null) {
                            Toast.makeText(MainActivity.this,
                                    "You long-clicked: " + thisWord.getWord(),
                                    Toast.LENGTH_LONG).show();
                        }
                        // returning false here will alow onClickListener to trigger as well
                        return true;
                    }
                });

                viewForeground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final WordEntity thisWord = getItem(getAdapterPosition());

                        if (thisWord != null) {
                            if (thisWord.getIsSelected()) {
                                thisWord.setIsSelected(false);
                            } else {
                                thisWord.setIsSelected(true);
                            }
                            mWordViewModel.update(thisWord);
                        }
                    }
                });
            }
        }
    }
}
