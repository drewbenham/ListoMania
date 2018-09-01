package com.example.megadeskssdedition.listomania.activities.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.megadeskssdedition.listmania.R;
import com.example.megadeskssdedition.listomania.activities.MediaPagerActivity;
import com.example.megadeskssdedition.listomania.activities.models.Record;
import com.example.megadeskssdedition.listomania.activities.models.RecordLab;

import java.util.ArrayList;

/**
 * Created by Megadesk SSD Edition on 7/12/2018.
 */

public class RecordListFragment extends ListFragment {
    private static final String RECORD_FRAGMENT = "RecordListFragment";

    private ArrayList<Record> records;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getActivity().setTitle("Records");
        records = RecordLab.get(getActivity()).getRecords();

        RecordAdapter recordArrayAdapter = new RecordAdapter(records);
        setListAdapter(recordArrayAdapter);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_format, container, false);

        ListView listView = (ListView) view.findViewById(android.R.id.list);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            //use floating context for older versions
            registerForContextMenu(listView);
        } else {
            //use contextual action bar
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                    //not used for now
                }

                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    MenuInflater menuInflater = actionMode.getMenuInflater();
                    menuInflater.inflate(R.menu.media_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    //not used in this implementation
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.menu_item_delete:
                            RecordAdapter adapter = (RecordAdapter)getListAdapter();
                            RecordLab lab = RecordLab.get(getActivity());

                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    lab.deleteRecord(adapter.getItem(i));
                                }
                            }

                            actionMode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {
                    //not used in this implementation.
                }
            });
        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        RecordLab.get(getActivity()).saveRecords();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((RecordAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("Fragment", "Menu");
        inflater.inflate(R.menu.fragment_music_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_music:
                Record record = new Record();
                RecordLab.get(getActivity()).addRecord(record);
                Intent addRecordIntent = new Intent(getActivity(), MediaPagerActivity.class);
                addRecordIntent.putExtra(RecordFragment.EXTRA_RECORD_ID, record.getUuid());
                startActivityForResult(addRecordIntent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.media_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int pos = info.position;
        RecordAdapter contextAdapter = (RecordAdapter)getListAdapter();
        Record r = contextAdapter.getItem(pos);

        switch (item.getItemId()) {
            case R.id.menu_item_delete:
                RecordLab.get(getActivity()).deleteRecord(r);
                contextAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Record record = ((RecordAdapter)getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), MediaPagerActivity.class);
        i.putExtra(RecordFragment.EXTRA_RECORD_ID, record.getUuid());
        startActivity(i);
    }

    private class RecordAdapter extends ArrayAdapter<Record> {
        public RecordAdapter(ArrayList<Record> records) {
            super(getActivity(), 0, records);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_record, null);
            }

            Record record = getItem(position);

            TextView albumTextView = (TextView)convertView.findViewById(R.id.list_item_album_name);
            albumTextView.setText(record.getAlbum());
            TextView artistTextView = (TextView)convertView.findViewById(R.id.list_item_artist_name);
            artistTextView.setText(record.getArtist());
            TextView releaseTextView = (TextView)convertView.findViewById(R.id.list_item_release_date);
            releaseTextView.setText(record.getReleaseDate().toString());

            return convertView;
        }
    }
}
