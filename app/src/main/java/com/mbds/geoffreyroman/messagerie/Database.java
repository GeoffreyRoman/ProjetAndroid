package com.mbds.geoffreyroman.messagerie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.BaseColumns;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Database implements Serializable {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void newContact(String s, Callback callback) {
        ApiService api = new ApiService();
        UserInfo currentUserInfo = Database.getINSTANCE().userInfo;
        String username = currentUserInfo.get("username");
        String token = currentUserInfo.get("access_token");
        String message;

        try {
            Crypto c = new Crypto(username);
            c.getPublicKey(username);
            message = "publickey=" + Arrays.toString(c.getPublicKey(username).getEncoded());

            api.sendMessage(token, s, message,callback);


        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

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


    public ArrayList<String> getContacts(){
        TreeSet<String> setAuteurs = new TreeSet<String>();
        try {
            Database.getINSTANCE().setJsonMessageArray(JsonMessageArray);
            for(int x = 0;x < JsonMessageArray.length(); x++){
                JSONObject message = (JSONObject) JsonMessageArray.get(x);
                setAuteurs.add(message.get("author").toString());
            }
            ArrayList<String> contacts = new ArrayList<>();
            contacts.addAll(setAuteurs);



            return contacts;


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static void getMessage(Callback callback){

        String token = Database.getINSTANCE().userInfo.get("access_token");
        ApiService api = new ApiService();
        try{
            api.getMessages(token,callback);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void sendMessage(String message, String receiver, Callback callback){

        String token = Database.getINSTANCE().userInfo.get("access_token");

        ApiService api = new ApiService();


        try {
            api.sendMessage(token,message,receiver,callback);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

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

    /*public final class User {
        private User() {
        }

        public String access_token;

        public class FeedUser implements BaseColumns {
            public static final String TABLE_NAME = "User";
            public static final String COLUMN_NAME_LOGIN = "login";
            public static final String COLUMN_NAME_PASSWORD = "Password";
        }
    }*/


    public final class ContactContract {
        private ContactContract() {
        }

        public class FeedContact implements BaseColumns {
            public static final String TABLE_NAME = "Contact";
            public static final String COLUMN_NAME_LASTNAME = "Nom";
            public static final String COLUMN_NAME_CLEPUBLIC = "Clepublic";

        }
    }

    public void addPerson(String name, String clepublic) {
        // Gets the data repository in write mode
        ContactHelper mDbHelper = new ContactHelper(ctx);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ContactContract.FeedContact.COLUMN_NAME_LASTNAME, name);
        values.put(ContactContract.FeedContact.COLUMN_NAME_CLEPUBLIC, clepublic);


// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ContactContract.FeedContact.TABLE_NAME, null, values);
    }


    public List<Person> readPerson() {
        ContactHelper mDbHelper = new ContactHelper(ctx);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContactContract.FeedContact.COLUMN_NAME_LASTNAME,
                ContactContract.FeedContact.COLUMN_NAME_CLEPUBLIC

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
            String clepublic = cursor.getString(cursor.getColumnIndex(ContactContract.FeedContact.COLUMN_NAME_CLEPUBLIC));

            persons.add(new Person(nom, clepublic));
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
