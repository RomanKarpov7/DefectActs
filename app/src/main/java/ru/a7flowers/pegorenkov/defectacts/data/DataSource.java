package ru.a7flowers.pegorenkov.defectacts.data;

import android.support.annotation.NonNull;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.User;
import ru.a7flowers.pegorenkov.defectacts.data.network.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.network.Diff;
import ru.a7flowers.pegorenkov.defectacts.data.network.Good;

public interface DataSource {

    interface GetVersionCallback{
        void onVersionLoaded(String version);
        void onVersionLoadFailed();
    }

    interface LoadUsersCallback{
        void onUsersLoaded(List<User> users);
        void onUsersLoadFailed(Throwable t);
    }

    interface LoadDeliveriesCallback{
        void onDeliveriesLoaded(List<Delivery> deliveries);
        void onDeliveriesLoadFailed(Throwable t);
    }

    interface LoadDeliveryCallback{
        void onDeliveryLoaded(Delivery delivery);
        void onDeliveryLoadFailed();
    }

    interface LoadGoodsCallback{
        void onGoodsLoaded(List<Good> goods);
        void onGoodsLoadFailed(Throwable t);
    }

    interface LoadGoodCallback{
        void onGoodLoaded(Good good);
        void onGoodLoadFailed();
    }

    interface LoadDefectsCallback{
        void onDefectsLoaded(List<Defect> defects);
        void onDefectsLoadFailed(Throwable t);
    }

    interface LoadDefectCallback{
        void onDefectLoaded(Defect defect);
        void onDefectLoadFailed();
    }

    interface LoadDiffCallback{
        void onDiffLoaded(Diff diff);
        void onDiffLoadFailed();
    }

    interface LoadDiffsCallback{
        void onDiffsLoaded(List<Diff> diffs);
        void onDiffsLoadFailed();
    }

    interface LoadReasonsCallback{
        void onReasonsLoaded(List<Reason> reasons);
        void onReasonsLoadFailed(Throwable t);
    }

    interface SaveReasonsCallback{
        void onReasonsSaved();
        void onReasonsSavingFailed();
    }

    interface UploadDefectCallback{
        void onDefectUploaded(Defect defect);
        void onDefectUploadingFailed();
    }

    interface SavePhotoCallback{
        void onPhotoSaved();
    }

    interface UploadPhotosCallback{
        void onPhotosUploaded(int photoAmount);
        void onPhotosUploadingFailed();
    }

    interface ClearDatabaseCallback{
        void onDatabaseCleared();
        void onDatabaseClearingFailed();
    }

    interface ReloadDataCallback{
        void onDataReloaded();
        void onDataReloadingFailed();
    }

    interface UploadDiffCallback {
        void onDiffUploaded(Diff diff);
        void onDiffUploadingFailed();
    }
}
