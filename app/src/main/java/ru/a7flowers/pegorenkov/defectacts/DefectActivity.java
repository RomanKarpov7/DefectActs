package ru.a7flowers.pegorenkov.defectacts;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.adapters.GoodsSearchAdapter;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReasonEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.network.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.network.Good;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.DefectViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ViewModelFactory;
import ru.a7flowers.pegorenkov.defectacts.views.AmountView;

public class DefectActivity extends ItemActivity {

    public static final String DELIVERY = "delivery_id";
    public static final String DEFECT = "defect_key";

    private static final int SELECT_REASON = 47;
    public static final String SELECTED_REASONS = "selected_reasons";

    private DefectViewModel model;

    private GoodsSearchAdapter adapter;

    private AmountView etAmount;
    private AmountView etWriteoff;

    private TextView tvReasons;
    private EditText etComment;
    private TextView tvSeries;
    private TextView tvTitle;
    private TextView tvSuplier;
    private TextView tvCountry;
    private TextView tvDelivery;
    private TextView tvDeliveryAmount;
    private AutoCompleteTextView acSearch;
    private TextView tvPhotoCount;
    private TextView tvNewPhotoCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        model = ViewModelProviders.of(this, ViewModelFactory.getInstance(getApplication(), getSupportFragmentManager())).get(DefectViewModel.class);

        findViews();

        if (savedInstanceState == null) {
            Intent i = getIntent();
            if (i.hasExtra(DELIVERY)) {
                String[] deliveryIds = i.getExtras().getStringArray(DELIVERY);
                if (i.hasExtra(DEFECT)) {
                    String defectId = i.getStringExtra(DEFECT);
                    model.start(deliveryIds, defectId);
                } else {
                    model.start(deliveryIds);
                }
            }
        }else{
            model.restoreState();
        }

        model.getGoods().observe(this, new Observer<List<Good>>() {
            @Override
            public void onChanged(@Nullable List<Good> goods) {
                adapter.clear();
                if (goods != null) adapter.setItems(goods);
            }
        });

        model.getNewPhotoCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer count) {
                if(count == null || count == 0){
                    tvNewPhotoCount.setText("");
                }else {
                    tvNewPhotoCount.setText("+" + String.valueOf(count));
                }
            }
        });

        model.getDefect().observe(this, new Observer<Defect>() {
            @Override
            public void onChanged(@Nullable Defect defect) {
                if (defect == null)
                    fillByDefect(new Defect());
                else
                    fillByDefect(defect);
            }
        });
        model.getGoodDefects().observe(this, new Observer<List<Defect>>() {
            @Override
            public void onChanged(@Nullable List<Defect> defects) {
                if(defects == null || defects.isEmpty()) return;
                showChooseDefectDialog(defects);
            }
        });
    }

    private void fillByDefect(Defect defect){
        etAmount.setValue(defect.getQuantity());
        etWriteoff.setValue(defect.getWriteoff());

        fillEditText(etComment, defect.getComment());

        tvSeries.setText(defect.getSeries());
        tvTitle.setText(defect.getTitle());
        tvSuplier.setText(defect.getSuplier());
        tvCountry.setText(defect.getCountry());
        tvDelivery.setText(defect.getDeliveryNumber());
        tvDeliveryAmount.setText(defect.getDeliveryQuantity() == 0 ? "" : String.valueOf(defect.getDeliveryQuantity()));
        tvPhotoCount.setText(defect.getPhotoQuantity() == 0 ? "" : String.valueOf(defect.getPhotoQuantity()));

        StringBuilder text = new StringBuilder();
        for (DefectReasonEntity reason : defect.getReasons()) {
            text.append(reason.getTitle()).append("; ");
        }
        tvReasons.setText(text.toString());
    }

    private void fillEditText(EditText view, String text){
        if (text == null) {
            view.setText("");
            return;
        }
        if(!text.equals(view.getText().toString())) {
            view.setText(text);
            view.setSelection(view.getText().length());
        }
    }

    private void findViews() {

        etAmount = findViewById(R.id.includeAmount);
        etAmount.setTitle(getString(R.string.amount));
        etAmount.setListener(new AmountView.ValueChangeListener() {
            @Override
            public void onValueChange(int value) {
                model.setAmount(value);
            }
        });

        etWriteoff = findViewById(R.id.includeWriteoff);
        etWriteoff.setTitle(getString(R.string.writeoff));
        etWriteoff.setListener(new AmountView.ValueChangeListener() {
            @Override
            public void onValueChange(int value) {
                model.setWriteoff(value);
            }
        });

        acSearch = findViewById(R.id.acSearch);
        tvSeries = findViewById(R.id.tvSeries);
        tvTitle = findViewById(R.id.tvTitle);
        tvSuplier = findViewById(R.id.tvSuplier);
        tvCountry = findViewById(R.id.tvCountry);
        tvDelivery = findViewById(R.id.tvDelivery);
        tvDeliveryAmount = findViewById(R.id.tvDeliveryAmount);
        tvReasons = findViewById(R.id.tvReasons);
        etComment = findViewById(R.id.etComment);
        tvPhotoCount = findViewById(R.id.tvPhotoCount);
        tvNewPhotoCount = findViewById(R.id.tvNewPhotoCount);

        ImageButton ibPhoto = findViewById(R.id.ibPhoto);
        ImageButton ibNext = findViewById(R.id.ibNext);
        ImageButton ibBarcode = findViewById(R.id.ibBarcode);
        ImageButton ibClear = findViewById(R.id.btnClear);

        acSearch.setThreshold(3);
        acSearch.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        acSearch.setKeyListener(DigitsKeyListener.getInstance("0123456789@#"));

        adapter = new GoodsSearchAdapter(this);
        adapter.setCallback(new GoodsSearchAdapter.GoodFoundCallback() {
            @Override
            public void onGoodFounded(Good good) {
                model.setGood(good, getSupportFragmentManager());
            }

            @Override
            public void onGoodNotFounded(CharSequence charSequence) {
                model.logGoodNotFound(charSequence.toString());
            }
        });
        acSearch.setAdapter(adapter);

        acSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Good good = (Good) adapterView.getAdapter().getItem(i);
                model.setGood(good, getSupportFragmentManager());
            }
        });

        tvReasons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseReason();
            }
        });

        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                model.setComment(String.valueOf(etComment.getText()));
            }
        });

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getNewPhotoCount().getValue() == 0) {
                    getDialogNoPhoto().show();
                } else {
                    saveDefect();
                }
            }
        });

        ibBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBarcode();
            }
        });

        ibPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPhoto(null);
             }
        });

        ibClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acSearch.setText("");
            }
        });
    }

    private void chooseReason(){
        Intent i = new Intent(this, ReasonsActivity.class);
        i.putExtra(SELECTED_REASONS, model.getDefectReasonsList());
        startActivityForResult(i, SELECT_REASON);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if(requestCode == SELECT_REASON && resultCode == RESULT_OK){
            if(intent.hasExtra(SELECTED_REASONS)) {
                List<Reason> list = (List<Reason>) intent.getExtras().getSerializable(SELECTED_REASONS);
                model.setDefectReasons(list);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Override
    public void onBarcodeScanned(String barcode) {
        acSearch.setText(barcode);
        List<Good> goods = model.findGoodsByBarcode(barcode);
        chooseGood(goods);
        Toast.makeText(this, barcode, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onScanFailed() {
        Toast.makeText(this, R.string.barcode_failure, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGoodScanned(Good good) {
        model.setGood(good, getSupportFragmentManager());
    }

    @Override
    public void onPhotoTaken(String photoPath, Bundle photoParams) {
        model.setPhotoPath(photoPath);
    }

    private Dialog getDialogNoPhoto(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_no_photo)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveDefect();
                    }
                })
                .setNeutralButton(R.string.photo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startPhoto(null);
                    }
                });
        return builder.create();
    }

    @Override
    public boolean showBackpressedDialog() {
        return model.showBackpressedDialog();
    }

    private void saveDefect(){
        model.saveDefect();
        acSearch.setText("");
        acSearch.requestFocus();
    }

    private void showChooseDefectDialog(final List<Defect> defects){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_position_dialog_title)
                .setAdapter(new DefectsDialogAdapter(this, defects),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                model.setDefect(defects.get(i));
                            }
                        })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        acSearch.setText("");
                        acSearch.requestFocus();
                    }
                })
                .setNeutralButton(getString(R.string.btn_create_new), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        model.createNewDefectByGood();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        acSearch.setText("");
                        acSearch.requestFocus();
                    }
                });
        builder.show();
    }

    class DefectsDialogAdapter extends ArrayAdapter<Defect>{

        public DefectsDialogAdapter(@NonNull Context context, @NonNull List<Defect> objects) {
            super(context, R.layout.item_defect, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_defect, parent, false);

            Defect defect = getItem(position);

            TextView tvSeries = v.findViewById(R.id.tvSeries);
            TextView tvGood = v.findViewById(R.id.tvGood);
            TextView tvSuplier = v.findViewById(R.id.tvSuplier);
            TextView tvCountry = v.findViewById(R.id.tvCountry);
            TextView tvQuantity = v.findViewById(R.id.tvQuantity);
            TextView tvPhotoQuantity = v.findViewById(R.id.tvPhotoQuantity);

            tvSeries.setText(defect.getSeries());
            tvGood.setText(defect.getTitle());
            tvSuplier.setText(defect.getSuplier());
            tvCountry.setText(defect.getCountry());
            tvQuantity.setText(String.valueOf(defect.getQuantity()));
            tvPhotoQuantity.setText(String.valueOf(defect.getPhotoQuantity()));

            return v;
        }
    }

}