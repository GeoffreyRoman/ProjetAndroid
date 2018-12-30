package com.mbds.geoffreyroman.messagerie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class Database {
    Context ctx;
    private static Database INSTANCE;

    private Database(Context ctx) {
        this.ctx = ctx;
    }

    public static Database getInstance(Context ctx){
        if(INSTANCE == null){
            INSTANCE = new Database(ctx);
        }
        return INSTANCE;
    }

    public final class ContactContract{
        private ContactContract() {}
        public class FeedContact implements BaseColumns
        {
            public static final String TABLE_NAME = "Contact";
            public static final String COLUMN_NAME_LASTNAME = "Nom";
            public static final String COLUMN_NAME_FIRSTNAME = "Prenom";
        }
    }

    public void addPerson(String name,String lname)
    {
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



    public List<Person> readPerson()
    {
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
        while(cursor.moveToNext())
        {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactContract.FeedContact._ID));
            String nom = cursor.getString(cursor.getColumnIndex(ContactContract.FeedContact.COLUMN_NAME_LASTNAME));
            String prenom = cursor.getString(cursor.getColumnIndex(ContactContract.FeedContact.COLUMN_NAME_FIRSTNAME));
            persons.add(new Person(nom,prenom));
        }
        cursor.close();

        return persons;
    }


}
