package com.omelchenkoaleks.importantcontacts.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.omelchenkoaleks.importantcontacts.R;

/*
    Определяет, как должны выполняться
    операции query, insert, update и delete с базой данных.
 */
public class AddressBookContentProvider extends ContentProvider {
    // используется для доступа к базе данных
    private AddressBookDatabaseHelper dbHelper;

    // UriMatcher помогает ContentProvider определить операцию для выполнения
    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    // constants используемые с UriMatcher для определения операции для выполнения
    private static final int ONE_CONTACT = 1; // манипулировать одним контактом
    private static final int CONTACTS = 2; // манипулировать таблицей контактов

    // static block для настройки UriMatcher этого ContentProvider
    static {
        // Uri for Contact with the specified id (#)
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                DatabaseDescription.Contact.TABLE_NAME + "/#", ONE_CONTACT);

        // Uri for Contacts table
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                DatabaseDescription.Contact.TABLE_NAME, CONTACTS);
    }

    // вызывается при создании AddressBookContentProvider
    @Override
    public boolean onCreate() {
        // create the AddressBookDatabaseHelper
        dbHelper = new AddressBookDatabaseHelper(getContext());
        return true; // ContentProvider successfully created
    }

    // required method: Not used in this app, so we return null
    @Override
    public String getType(Uri uri) {
        return null;
    }

    // запросить базу данных
    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {

        // create SQLiteQueryBuilder for querying contacts table
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseDescription.Contact.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ONE_CONTACT: // контакт с указанным id будет выбран
                queryBuilder.appendWhere(
                        DatabaseDescription.Contact._ID + "=" + uri.getLastPathSegment());
                break;
            case CONTACTS: // все контакты будут выбраны
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_query_uri) + uri);
        }

        // выполнить запрос, чтобы выбрать один или все контакты
        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);

        // настроить отслеживание изменение содержимого
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    // вставить новый контакт в базу данных
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri newContactUri = null;

        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                // при успехе возвращается id записи нового контакта
                long rowId = dbHelper.getWritableDatabase().insert(
                        DatabaseDescription.Contact.TABLE_NAME, null, values);

                // если контакт был вставлен, создать подходящий Uri;
                // в другом случае выдать исключение
                if (rowId > 0) { // SQLite row IDs start at 1
                    newContactUri = DatabaseDescription.Contact.buildContactUri(rowId);

                    // оповестить наблюдателей об изменениях в базе данных
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                else
                    throw new SQLException(
                            getContext().getString(R.string.insert_failed) + uri);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_insert_uri) + uri);
        }

        return newContactUri;
    }

    // обновление существующего контакта в базе данных
    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int numberOfRowsUpdated; // 1 if update successful; 0 otherwise

        switch (uriMatcher.match(uri)) {
            case ONE_CONTACT:
                // получение id контакта из Uri
                String id = uri.getLastPathSegment();

                // обновление контакта
                numberOfRowsUpdated = dbHelper.getWritableDatabase().update(
                        DatabaseDescription.Contact.TABLE_NAME, values, DatabaseDescription.Contact._ID + "=" + id,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_update_uri) + uri);
        }

        // если были внесены изменения, оповестить наблюдателей
        if (numberOfRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsUpdated;
    }

    // удаление существующего контакта из базы данных
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numberOfRowsDeleted;

        switch (uriMatcher.match(uri)) {
            case ONE_CONTACT:
                // получение из URI id контакта
                String id = uri.getLastPathSegment();

                // удаление контакта
                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(
                        DatabaseDescription.Contact.TABLE_NAME, DatabaseDescription.Contact._ID + "=" + id, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_delete_uri) + uri);
        }

        // оповестить наблюдателей об изменениях в базе данных
        if (numberOfRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsDeleted;
    }
}
