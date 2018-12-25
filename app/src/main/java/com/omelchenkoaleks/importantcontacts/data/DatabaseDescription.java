package com.omelchenkoaleks.importantcontacts.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/*
    Содержит описание таблицы базы данных.
 */
public class DatabaseDescription {
    // ContentProvider's name
    public static final String AUTHORITY =
            "com.omelchenkoaleks.importantcontacts.data";

    // base URI используется для взаимодействия с ContentProvider
    private static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    // класс определяет содержимое таблицы contacts
    public static final class Contact implements BaseColumns {
        public static final String TABLE_NAME = "contacts"; // table's name

        // Uri для таблицы контактов
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        // имена столбцов для контактов
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_STREET = "street";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_INDEX = "code";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_NOTES = "notes";

        // создает Uri для конкретного контакта
        public static Uri buildContactUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
