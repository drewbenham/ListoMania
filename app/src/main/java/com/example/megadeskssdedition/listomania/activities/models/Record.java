package com.example.megadeskssdedition.listomania.activities.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Megadesk SSD Edition on 6/27/2018.
 */

public class Record {
    private static final String JSON_ID = "id";
    private static final String JSON_ARTIST = "artist";
    private static final String JSON_ALBUM = "album";
    private static final String JSON_DATE = "date";

    private UUID uuid;
    private String artist;
    private String album;
    private Date releaseDate;

    public Record() {
        uuid = UUID.randomUUID();
        artist = "Not Set";
        album = "Not Set";
        releaseDate = new Date();
    }

    public Record(JSONObject jsonObject) throws JSONException, ParseException {
        uuid = UUID.fromString(jsonObject.getString(JSON_ID));
        if (jsonObject.has(JSON_ARTIST))
            artist = jsonObject.getString(JSON_ARTIST);
        album = jsonObject.getString(JSON_ALBUM);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        releaseDate = dateFormat.parse(jsonObject.getString(JSON_DATE));
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_ID, uuid.toString());
        jsonObject.put(JSON_ARTIST, artist.toString());
        jsonObject.put(JSON_ALBUM, album.toString());
        jsonObject.put(JSON_DATE, releaseDate.toString());
        return jsonObject;
    }

    public Record(String artist, String album) {
        uuid = UUID.randomUUID();
        this.artist = artist;
        this.album = album;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String toString() {
        String s = "";
        s += artist + " ";
        s += album + " ";
        s += releaseDate;
        return s;
    }
}
