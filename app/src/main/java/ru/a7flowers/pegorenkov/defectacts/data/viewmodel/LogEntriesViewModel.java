package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.DataSource;
import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.LogEntry;
import ru.a7flowers.pegorenkov.defectacts.data.entities.User;


public class LogEntriesViewModel extends AndroidViewModel {

    private Repository mRepository;
    private LiveData<List<LogEntry>> mLogEntries;
    private MutableLiveData<Boolean> isReloading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isActualVersion = new MutableLiveData<>();

    public LogEntriesViewModel(@NonNull Application application, Repository repository) {
        super(application);

        this.mRepository = repository;
        this.mLogEntries = mRepository.getLogEntries();

        isReloading.setValue(false);
        isActualVersion.setValue(true);
    }

    public void refreshData(FragmentManager fragmentManager) {
        isReloading.postValue(true);
        mRepository.reloadData(fragmentManager, new DataSource.ReloadDataCallback() {
            @Override
            public void onDataReloaded() {
                isReloading.setValue(false);
            }

            @Override
            public void onDataReloadingFailed() {
                isReloading.setValue(false);
            }
        });
    }

    public LiveData<List<LogEntry>> getLogEntries() {
        return mLogEntries;
    }

    public MutableLiveData<Boolean> getIsReloading() {
        return isReloading;
    }

    public MutableLiveData<Boolean> isActualVersion() {
        return isActualVersion;
    }

}
