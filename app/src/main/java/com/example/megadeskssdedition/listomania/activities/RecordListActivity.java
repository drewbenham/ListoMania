package com.example.megadeskssdedition.listomania.activities;

import android.support.v4.app.Fragment;

import com.example.megadeskssdedition.listomania.activities.fragments.RecordListFragment;


/**
 * Created by Megadesk SSD Edition on 7/12/2018.
 */

public class RecordListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RecordListFragment();
    }
}
