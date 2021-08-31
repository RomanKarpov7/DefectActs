package ru.a7flowers.pegorenkov.defectacts.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ru.a7flowers.pegorenkov.defectacts.R;
import ru.a7flowers.pegorenkov.defectacts.data.entities.LogEntry;

public class LogEntriesAdapter extends RecyclerView.Adapter<LogEntriesAdapter.LogEntriesHolder>{

    private List<LogEntry> items;
    private OnLogEntriesClickListener listener;

    public interface OnLogEntriesClickListener {
        void onLogEntryClick(LogEntry LogEntry);
    }

    @NonNull
    @Override
    public LogEntriesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_log_entry, viewGroup, false);

        return new LogEntriesHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LogEntriesHolder viewHolder, int position) {
        LogEntry logEntry = items.get(position);

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        viewHolder.tvPeriod.setText(format.format(logEntry.getPeriod()));
        viewHolder.tvEventName.setText(logEntry.getEventName());
        viewHolder.tvDescription.setText(logEntry.getDescription());

    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setItems(List<LogEntry> logEntries){
        items = logEntries;
        notifyDataSetChanged();
    }

    public void setOnLogEntryClickListener(OnLogEntriesClickListener listener) {
        this.listener = listener;
    }

    class LogEntriesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvPeriod;
        TextView tvEventName;
        TextView tvDescription;

        public LogEntriesHolder(@NonNull View itemView) {
            super(itemView);
            tvPeriod = itemView.findViewById(R.id.tvPeriod);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            LogEntry logEntry = items.get(getAdapterPosition());
            if(listener != null) listener.onLogEntryClick(logEntry);
        }
    }

}
