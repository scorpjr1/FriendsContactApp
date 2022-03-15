package com.example.shiyan1.view.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.shiyan1.R;
import com.example.shiyan1.viewmodel.HaoyouViewModel;

public class AddHaoyouActivity extends AppCompatActivity {

    public static final int NEW_HAOYOU_REQUEST_CODE = 1;
    private HaoyouViewModel haoyouViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_haoyou);

//        RecyclerView recyclerView = findViewById(R.id.recyclerview);
//        final WordListAdapter adapter = new WordListAdapter(new WordListAdapter.WordDiff());
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        haoyouViewModel = new ViewModelProvider(this).get(HaoyouViewModel.class);
        haoyouViewModel.getAllHaoyous().observe(this, haoyous -> {
            // Update the cached copy of the words in the adapter.
//            adapter.submitList(haoyous);
            Toast.makeText(AddHaoyouActivity.this, haoyous.toString(), Toast.LENGTH_LONG).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_HAOYOU_REQUEST_CODE && resultCode == RESULT_OK) {
            // caranya si tutorial dari addactivity form submit, putextra data. trus dsini insert. di jiemian ini dia juga sekalian select data
//            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
//            mWordViewModel.insert(word);
        }
    }
}