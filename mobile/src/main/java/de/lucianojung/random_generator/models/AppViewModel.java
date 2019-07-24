package de.lucianojung.random_generator.models;

import android.arch.lifecycle.ViewModel;

import de.lucianojung.random_generator.database.AppDatabase;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AppViewModel extends ViewModel {
    private AppDatabase database = null;

    public AppViewModel(AppDatabase database) {
        this.database = database;
    }
}
