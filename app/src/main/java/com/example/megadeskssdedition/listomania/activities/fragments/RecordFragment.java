package com.example.megadeskssdedition.listomania.activities.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.megadeskssdedition.listmania.R;
import com.example.megadeskssdedition.listomania.activities.models.Record;
import com.example.megadeskssdedition.listomania.activities.models.RecordLab;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Megadesk SSD Edition on 6/29/2018.
 */

public class RecordFragment extends Fragment {
    private static final int REQUEST_CODE = 1;
    private static final String DIALOG_DATE = "date";
    public static final String EXTRA_RECORD_ID = "Record_ID";


    private Record record;
    private ViewHolder viewHolder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        UUID recordId = (UUID) getArguments().getSerializable(EXTRA_RECORD_ID);

        record = RecordLab.get(getActivity()).getRecord(recordId);
    }

    @Override
    public void onPause() {
        super.onPause();
        RecordLab.get(getActivity()).saveRecords();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public static RecordFragment newInstance(UUID recordId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_RECORD_ID, recordId);

        RecordFragment fragment = new RecordFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void updateDate() {
        viewHolder.releaseDateButton.setText(record.getReleaseDate().toString());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        viewHolder = new ViewHolder(view);
        viewHolder.artistName.setText(record.getArtist());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        viewHolder.artistName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // TODO: 6/29/2018 blank for now
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                record.setArtist(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // TODO: 6/29/2018 blank for now
            }
        });

        viewHolder.albumName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // TODO: 6/29/2018 blank for now
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                record.setAlbum(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // TODO: 6/29/2018 blank for now
            }
        });

        viewHolder.releaseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ReleaseDatePickerFragment dialog = ReleaseDatePickerFragment.newInstance(record.getReleaseDate());
                dialog.setTargetFragment(RecordFragment.this, REQUEST_CODE);
                dialog.show(fragmentManager, DIALOG_DATE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == REQUEST_CODE) {
            Date date = (Date) data.getSerializableExtra(ReleaseDatePickerFragment.EXTRA_DATE);
            record.setReleaseDate(date);
            updateDate();
        }
    }

    private class ViewHolder {
        private EditText artistName;
        private EditText albumName;
        private Button releaseDateButton;

        private ViewHolder(View view) {
            this.artistName = (EditText) view.findViewById(R.id.record_artist);
            this.albumName = (EditText) view.findViewById(R.id.record_album);
            this.releaseDateButton = (Button) view.findViewById(R.id.release_date_button);
        }
    }
}
