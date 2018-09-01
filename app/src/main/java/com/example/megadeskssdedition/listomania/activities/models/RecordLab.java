package com.example.megadeskssdedition.listomania.activities.models;

import android.content.Context;
import android.util.Log;

import com.example.megadeskssdedition.listomania.activities.saving.MediaJSONSerializer;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Megadesk SSD Edition on 7/11/2018.
 */

public class RecordLab {
    private static final String FILENAME = "records.json";

    private Context context;
    private static RecordLab recordLab;
    private ArrayList<Record> records;
    private MediaJSONSerializer serializer;

    private RecordLab(Context context) {
        this.context = context;
        serializer = new MediaJSONSerializer(context, FILENAME);

        try {
            records = serializer.loadMedia();
        } catch (Exception e) {
            records = new ArrayList<Record>();
            Log.e("Loading", "Error loading media: ", e);
        }
    }

    public void addRecord(Record r) {
        records.add(r);
    }

    public void deleteRecord(Record r) {
        records.remove(r);
    }

    public boolean saveRecords() {
        try {
            serializer.saveMedia(records);
            Log.d("Saving", "saved successfully");
            return true;
        } catch (Exception e) {
            Log.e("Saving", "error saving file: ", e);
            return false;
        }
    }

    public static RecordLab get(Context c) {
        if (recordLab == null) {
            recordLab = new RecordLab(c);
        }
        return recordLab;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public Record getRecord(UUID uuid) {
        for (Record record: records) {
            if (record.getUuid().equals(uuid)) {
                return record;
            }
        }
        return null;
    }
}
