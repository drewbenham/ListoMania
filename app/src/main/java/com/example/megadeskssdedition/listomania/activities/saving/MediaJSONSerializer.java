package com.example.megadeskssdedition.listomania.activities.saving;

import android.content.Context;
import android.util.Log;

import com.example.megadeskssdedition.listomania.activities.models.Record;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;

public class MediaJSONSerializer {
    private Context context;
    private String fileName;

    public MediaJSONSerializer(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public ArrayList<Record> loadMedia() throws IOException, JSONException {
        ArrayList<Record> records = new ArrayList<>();
        BufferedReader reader = null;
        try {
            //open and read the file
            InputStream in = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //line breaks are omitted and irrelevant.
                jsonString.append(line);
            }
            //Parse the JSON using JSONTokener
            JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //build array
            for (int i = 0; i < jsonArray.length(); i++) {
                records.add(new Record(jsonArray.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            //ignore, happens on first load.
        } catch (ParseException e) {
            Log.e("Loading", "Error parsing media: ", e);
        } finally {
            if (reader != null)
                reader.close();
        }
        return records;
    }

    public void saveMedia(ArrayList<Record> records) throws JSONException, IOException {
        //Build an array in JSON.
        JSONArray jsonArray = new JSONArray();
        for (Record r : records)
            jsonArray.put(r.toJSON());

        //write file
        Writer writer = null;
        try {
            OutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonArray.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
