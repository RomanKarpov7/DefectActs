package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.LogEntry;

@Dao
public interface LogEntryDao {

    @Query("SELECT * FROM logs ORDER BY period DESC")
    LiveData<List<LogEntry>> loadAllEntries();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntry(LogEntry entry);


}
