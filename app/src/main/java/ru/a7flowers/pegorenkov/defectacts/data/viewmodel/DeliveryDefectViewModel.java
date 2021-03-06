package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.network.Defect;

public class DeliveryDefectViewModel extends AndroidViewModel {

    private Repository mRepository;

    private String[] mDeliveries;
    private LiveData<List<Defect>> mDefects;

    public DeliveryDefectViewModel(@NonNull Application application, Repository repository) {
        super(application);
        mRepository = repository;
    }

    public void start(String[] deliveries, FragmentManager fragmentManager){
        mDeliveries = deliveries;
        mDefects = mRepository.getDefectGoods(mDeliveries, fragmentManager);
    }

    public LiveData<List<Defect>> getDefects() {
        return mDefects;
    }

    public String[] getDeliveryIds(){
        return mDeliveries;
    }

}
