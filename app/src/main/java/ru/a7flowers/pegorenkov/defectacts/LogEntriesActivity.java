package ru.a7flowers.pegorenkov.defectacts;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import ru.a7flowers.pegorenkov.defectacts.adapters.LogEntriesAdapter;
import ru.a7flowers.pegorenkov.defectacts.data.entities.LogEntry;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.LogEntriesViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ViewModelFactory;

public class LogEntriesActivity extends AppCompatActivity implements LogEntriesAdapter.OnLogEntriesClickListener {

    private LogEntriesViewModel model;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_entries);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        model = ViewModelProviders.of(this, ViewModelFactory.getInstance(getApplication(), getSupportFragmentManager())).get(LogEntriesViewModel.class);

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> model.refreshData(getSupportFragmentManager()));
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        final RecyclerView rvLogEntries = findViewById(R.id.rvLogEntries);
        rvLogEntries.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvLogEntries.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvLogEntries.addItemDecoration(itemDecor);

        final LogEntriesAdapter adapter = new LogEntriesAdapter();
        adapter.setOnLogEntryClickListener(this);
        rvLogEntries.setAdapter(adapter);

        model.getLogEntries().observe(this, logEntries -> {if(logEntries != null) adapter.setItems(logEntries);});

        model.getIsReloading().observe(this, isLoading -> {if(isLoading != null)swipeContainer.setRefreshing(isLoading);});

        model.isActualVersion().observe(this,
                isActualVersion -> {
                    if(isActualVersion != null && !isActualVersion){
                        showVersionDialog();
                    }
                });


    }

    private void showVersionDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LogEntriesActivity.this)
                .setTitle("Version error")
                .setMessage("Version of app don't matches with server version. Please upgrade the app")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> LogEntriesActivity.this.finish());

        dialogBuilder.show();
    }

    @Override
    public void onLogEntryClick(LogEntry LogEntry) {

    }
}
