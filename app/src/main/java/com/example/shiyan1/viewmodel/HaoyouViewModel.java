package com.example.shiyan1.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.shiyan1.model.database.YoujiRepository;
import com.example.shiyan1.model.entities.Haoyou;

import java.util.List;

// activities and fragments are responsible for drawing data to the screen, while your ViewModel can take care of holding and processing all the data needed for the UI.
public class HaoyouViewModel extends AndroidViewModel {

    private YoujiRepository youjiRepository;
    private final LiveData<List<Haoyou>> allHaoyous;

    public HaoyouViewModel(@NonNull Application application) {
        super(application);
        youjiRepository = new YoujiRepository(application);
        allHaoyous = youjiRepository.getAllHaoyous();
    }

    public LiveData<List<Haoyou>> getAllHaoyous() {
        return allHaoyous;
    }

    public void insert(Haoyou haoyou) {
        youjiRepository.insert(haoyou);
    }
}
