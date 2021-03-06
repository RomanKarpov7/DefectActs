package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.DataSource;
import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.User;

public class UsersViewModel extends AndroidViewModel {

    private Repository mRepository;

    private LiveData<List<User>> mUsers;

    private MutableLiveData<Boolean> isReloading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isActualVersion = new MutableLiveData<>();

    public UsersViewModel(@NonNull Application application, Repository repository) {
        super(application);
        mRepository = repository;

        isReloading.setValue(false);
        isActualVersion.setValue(true);

        mRepository.getServerVersion(new DataSource.GetVersionCallback() {
            @Override
            public void onVersionLoaded(String serverVersion) {
                try {
                    PackageInfo packageInfo = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
                    String localVersion = packageInfo.versionName;
                    isActualVersion.postValue(localVersion.equals(serverVersion));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVersionLoadFailed() {

            }
        });

        mUsers = mRepository.getUsers();
    }

    public LiveData<List<User>> getUsers() {
        return mUsers;
    }

    public void setCurrentUser(User user) {
        mRepository.setCurrentUser(user);
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

    public MutableLiveData<Boolean> getIsReloading() {
        return isReloading;
    }

    public MutableLiveData<Boolean> isActualVersion() {
        return isActualVersion;
    }
}
