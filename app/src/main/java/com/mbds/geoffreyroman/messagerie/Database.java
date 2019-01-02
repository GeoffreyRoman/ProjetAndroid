package com.mbds.geoffreyroman.messagerie;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Database implements Serializable {

    public static class UserInfo {
        JSONObject userInfoJson;

        UserInfo(JSONObject userInfoJson) {
            this.userInfoJson = userInfoJson;
        }

        public String get(String attribut) {
            try {
                String Jarray = userInfoJson.getString(attribut);
                return Jarray;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }



    JSONArray JsonMessageArray;

    UserInfo userInfo;
    Context ctx;


    public JSONArray getJsonMessageArray() {
        return this.JsonMessageArray;
    }

    public void setJsonMessageArray(JSONArray JsonMessageArray) {
        this.JsonMessageArray = JsonMessageArray;
    }

    public void setUserInfo(JSONObject userInfoJson) {
        this.userInfo = new UserInfo(userInfoJson);
    }

    public static Database getINSTANCE() {
        return INSTANCE;
    }

    private static Database INSTANCE;
    String id = "-1";

    private Database(Context ctx) {
        this.ctx = ctx;
    }

    public static Database getInstance(Context ctx) {
        if (INSTANCE == null) {
            INSTANCE = new Database(ctx);
        }
        return INSTANCE;
    }

    public final class User {
        private User() {
        }

        public String access_token;

        public class FeedUser implements BaseColumns {
            public static final String TABLE_NAME = "User";
            public static final String COLUMN_NAME_LOGIN = "login";
            public static final String COLUMN_NAME_PASSWORD = "Password";
        }
    }


    public final class ContactContract {
        private ContactContract() {
        }

        public class FeedContact implements BaseColumns {
            public static final String TABLE_NAME = "Contact";
            public static final String COLUMN_NAME_LASTNAME = "Nom";
            public static final String COLUMN_NAME_FIRSTNAME = "Prenom";
        }
    }

    public void addPerson(String name, String lname) {
        // Gets the data repository in write mode
        ContactHelper mDbHelper = new ContactHelper(ctx);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ContactContract.FeedContact.COLUMN_NAME_LASTNAME, name);
        values.put(ContactContract.FeedContact.COLUMN_NAME_FIRSTNAME, lname);

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ContactContract.FeedContact.TABLE_NAME, null, values);
    }


    public List<Person> readPerson() {
        ContactHelper mDbHelper = new ContactHelper(ctx);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContactContract.FeedContact.COLUMN_NAME_LASTNAME,
                ContactContract.FeedContact.COLUMN_NAME_FIRSTNAME
        };


        String selection = "";
        String[] selectionArgs = null;

        String sortOrder =
                ContactContract.FeedContact.COLUMN_NAME_LASTNAME + " DESC";

        Cursor cursor = db.query(
                ContactContract.FeedContact.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List persons = new ArrayList<Person>();
        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactContract.FeedContact._ID));
            String nom = cursor.getString(cursor.getColumnIndex(ContactContract.FeedContact.COLUMN_NAME_LASTNAME));
            String prenom = cursor.getString(cursor.getColumnIndex(ContactContract.FeedContact.COLUMN_NAME_FIRSTNAME));
            persons.add(new Person(nom, prenom));
        }
        cursor.close();

        return persons;
    }


    public String checkUser(String login, String password, Callback callback) {

        ApiService api = new ApiService();
        JSONObject jo = null;

        try {
            jo = new JSONObject();
            jo.put("username", login);
            jo.put("password", password);
        } catch (JSONException JsonE) {
            JsonE.printStackTrace();
        }
        String params = jo.toString();
        try {
            api.login(params, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }

    public void createUser(String username, String password, Callback callback) {
       /* ContactHelper mDbHelper = new ContactHelper(ctx);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.FeedUser.COLUMN_NAME_LOGIN, login);
        values.put(User.FeedUser.COLUMN_NAME_PASSWORD, password);

        db.insert(User.FeedUser.TABLE_NAME, null, values);
        */

        JSONObject jo = null;

        try {
            jo = new JSONObject();
            jo.put("username", username);
            jo.put("password", password);
        } catch (JSONException JsonE) {
            JsonE.printStackTrace();
        }

        String params = jo.toString();
        ApiService api = new ApiService();
        try {
            api.createUser(params, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
