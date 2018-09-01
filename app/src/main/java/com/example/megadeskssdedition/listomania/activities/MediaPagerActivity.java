package com.example.megadeskssdedition.listomania.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.megadeskssdedition.listmania.R;
import com.example.megadeskssdedition.listomania.activities.fragments.RecordFragment;
import com.example.megadeskssdedition.listomania.activities.models.Record;
import com.example.megadeskssdedition.listomania.activities.models.RecordLab;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Megadesk SSD Edition on 7/24/2018.
 */

public class MediaPagerActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ArrayList<Record> records;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);
        setContentView(viewPager);

        records = RecordLab.get(this).getRecords();

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Record record = records.get(position);
                return RecordFragment.newInstance(record.getUuid());
            }

            @Override
            public int getCount() {
                return records.size();
            }
        });

        UUID recordId = (UUID) getIntent().getSerializableExtra(RecordFragment.EXTRA_RECORD_ID);

        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getUuid().equals(recordId)) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
