package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.a7flowers.pegorenkov.defectacts.data.DataSource;
import ru.a7flowers.pegorenkov.defectacts.data.Mode;
import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.User;

public class DeliveriesViewModel extends AndroidViewModel {

    private Repository mRepository;

    private MutableLiveData<Boolean> isReloading = new MutableLiveData<>();
    private MutableLiveData<Mode> mMode = new MutableLiveData<>();

    private LiveData<List<Delivery>> mDeliveries;
    private Set<String> mSelectedDeliveriesIds = new HashSet<>();

    public DeliveriesViewModel(@NonNull Application application, Repository repository) {
        super(application);

        mRepository = repository;

        isReloading.postValue(false);
        mMode.setValue(mRepository.getMode());

        mDeliveries = mRepository.getDeliveries();
    }

    public LiveData<List<Delivery>> getDeliveries() {
        return mDeliveries;
    }

    public String[] getSelectedDeliveryIds() {
        return mSelectedDeliveriesIds.toArray(new String[mSelectedDeliveriesIds.size()]);
    }

    public boolean isDeliverySelected(Delivery delivery) {
        return mSelectedDeliveriesIds.contains(delivery.getId());
    }

    public void addSelectedDelivery(Delivery delivery) {
        mSelectedDeliveriesIds.add(delivery.getId());
    }

    public void removeSelectedDelivery(Delivery delivery) {
        mSelectedDeliveriesIds.remove(delivery.getId());
    }

    public MutableLiveData<Mode> getMode(){
        return mMode;
    }

    public void setMode(@NonNull Mode mode){
        mMode.setValue(mode);
        mRepository.setMode(mode);
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

    public void saveDeliveryPhoto(String deliveryId, String photoPath) {
        mRepository.saveDeliveryPhoto(deliveryId, photoPath);
    }

    public boolean isModeSelectionAvailable() {
        User user = mRepository.getCurrentUser();
        return user.isDefectAccess() && user.isDiffAccess();
    }
}
