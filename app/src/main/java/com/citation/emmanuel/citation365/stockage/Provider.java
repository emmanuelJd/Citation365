package com.citation.emmanuel.citation365.stockage;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

public class Provider extends ContentProvider{

	private OpenHelper mopenHelper;
	
	private static final HashMap<String, String> mNotesProjectionMap = new HashMap<String, String>();
    private static final HashMap<String, String> mNotesProjectionMapAuteur = new HashMap<String, String>();


    private static final int CITATIONS = 1;
	private static final int CITATION_ID = 2;

    private static final int AUTEURS = 3;
    private static final int AUTEUR_ID = 4;
	
	private static final String AUTHORITY = "com.citation.emmanuel.citation365.provider";
	
	private static final String BASE_PATH = "citation";
    private static final String BASE_PATH_AUTEUR = "auteur";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
	      + "/" + BASE_PATH);

    public static final Uri CONTENT_URI_AUTEUR = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_AUTEUR);
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	  static {
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH, CITATIONS);
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CITATION_ID);

        sURIMatcher.addURI(AUTHORITY, BASE_PATH_AUTEUR, AUTEURS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH_AUTEUR + "/#", AUTEUR_ID);
	    
	    mNotesProjectionMap.put(OpenHelper.COLUMN_ID, OpenHelper.COLUMN_ID);
        mNotesProjectionMap.put(OpenHelper.COLUMN_ID_CITATION_LINK, OpenHelper.COLUMN_ID_CITATION_LINK);
        mNotesProjectionMap.put(OpenHelper.COLUMN_ID_AUTEUR_LINK, OpenHelper.COLUMN_ID_AUTEUR_LINK);
        mNotesProjectionMap.put(OpenHelper.COLUMN_CITATION, OpenHelper.COLUMN_CITATION);
        mNotesProjectionMap.put(OpenHelper.COLUMN_AUTEUR, OpenHelper.COLUMN_AUTEUR);

        mNotesProjectionMapAuteur.put(OpenHelper.COLUMN_ID, OpenHelper.COLUMN_ID);
        mNotesProjectionMapAuteur.put(OpenHelper.COLUMN_ID_AUTEUR_LINK, OpenHelper.COLUMN_ID_AUTEUR_LINK);
        mNotesProjectionMapAuteur.put(OpenHelper.COLUMN_IMAGE_LINK, OpenHelper.COLUMN_IMAGE_LINK);
        mNotesProjectionMapAuteur.put(OpenHelper.COLUMN_AUTEUR_FONCTION, OpenHelper.COLUMN_AUTEUR_FONCTION);
	  }

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		
		 SQLiteDatabase db = mopenHelper.getWritableDatabase();
         String finalWhere;

         int count;

         switch (sURIMatcher.match(uri)) {
             case CITATIONS:
                 count = db.delete(OpenHelper.TABLE_CITATION_FAVORIS, where, whereArgs);
                 getContext().getContentResolver().notifyChange(uri, null);
                 getContext().getContentResolver().notifyChange(Provider.CONTENT_URI, null);
                 break;

             case CITATION_ID:
                 finalWhere = DatabaseUtils.concatenateWhere(
                         OpenHelper.COLUMN_ID + " = " + ContentUris.parseId(uri), where);
                 count = db.delete(OpenHelper.TABLE_CITATION_FAVORIS, finalWhere, whereArgs);
                 getContext().getContentResolver().notifyChange(uri, null);
                 getContext().getContentResolver().notifyChange(Provider.CONTENT_URI, null);
                 break;

             case AUTEURS:
                 count = db.delete(OpenHelper.TABLE_AUTEUR, where, whereArgs);
                 getContext().getContentResolver().notifyChange(uri, null);
                 getContext().getContentResolver().notifyChange(Provider.CONTENT_URI_AUTEUR, null);
                 break;

             case AUTEUR_ID:
                 finalWhere = DatabaseUtils.concatenateWhere(
                         OpenHelper.COLUMN_ID + " = " + ContentUris.parseId(uri), where);
                 count = db.delete(OpenHelper.TABLE_AUTEUR, finalWhere, whereArgs);
                 getContext().getContentResolver().notifyChange(uri, null);
                 getContext().getContentResolver().notifyChange(Provider.CONTENT_URI_AUTEUR, null);
                 break;

             default:
                 throw new IllegalArgumentException("Unknown URI " + uri);
         }



         return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues contentValues) {

        ContentValues values;

        if (contentValues != null) {
            values = new ContentValues(contentValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = mopenHelper.getWritableDatabase();
        long rowId = -1;
        switch (sURIMatcher.match(uri)) {
            case CITATIONS:
                            if (values.containsKey(OpenHelper.COLUMN_ID_CITATION_LINK) == false) {
                                values.put(OpenHelper.COLUMN_ID_CITATION_LINK, "");
                            }
                            if (values.containsKey(OpenHelper.COLUMN_ID_AUTEUR_LINK) == false) {
                                values.put(OpenHelper.COLUMN_ID_AUTEUR_LINK, "");
                            }
                            if (values.containsKey(OpenHelper.COLUMN_CITATION) == false) {
                                values.put(OpenHelper.COLUMN_CITATION, "");
                            }
                            if (values.containsKey(OpenHelper.COLUMN_AUTEUR) == false) {
                                values.put(OpenHelper.COLUMN_AUTEUR, "");
                            }

                            rowId = db.insert(OpenHelper.TABLE_CITATION_FAVORIS, null, values);

                            if (rowId > 0) {
                                Uri noteUri = ContentUris.withAppendedId(Uri.parse("content://" + AUTHORITY + "/"+OpenHelper.TABLE_CITATION_FAVORIS+"/"), rowId);
                                getContext().getContentResolver().notifyChange(noteUri, null);
                                return noteUri;
                            }
                            throw new SQLException("Failed to insert row into " + uri);



            case AUTEURS:
                            if (values.containsKey(OpenHelper.COLUMN_ID_AUTEUR_LINK) == false) {
                                values.put(OpenHelper.COLUMN_ID_AUTEUR_LINK, "");
                            }
                            if (values.containsKey(OpenHelper.COLUMN_IMAGE_LINK) == false) {
                                values.put(OpenHelper.COLUMN_IMAGE_LINK, "");
                            }
                            if (values.containsKey(OpenHelper.COLUMN_AUTEUR_FONCTION) == false) {
                                values.put(OpenHelper.COLUMN_AUTEUR_FONCTION, "");
                            }

                            rowId = db.insert(OpenHelper.TABLE_AUTEUR, null, values);

                            if (rowId > 0) {
                                Uri noteUri = ContentUris.withAppendedId(Uri.parse("content://" + AUTHORITY + "/"+OpenHelper.TABLE_AUTEUR+"/"), rowId);
                                getContext().getContentResolver().notifyChange(noteUri, null);
                                return noteUri;
                            }
                            throw new SQLException("Failed to insert row into " + uri);

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
	}

	@Override
	public boolean onCreate() {

		mopenHelper = new OpenHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		int match = sURIMatcher.match(uri);
		
		switch(match){
		
			case CITATIONS:	qb.setTables(OpenHelper.TABLE_CITATION_FAVORIS);
                            qb.setProjectionMap(mNotesProjectionMap);
							break;
				
			case CITATION_ID:   qb.setTables(OpenHelper.TABLE_CITATION_FAVORIS);
                                qb.setProjectionMap(mNotesProjectionMap);
	        				    qb.appendWhere(OpenHelper.COLUMN_ID + "=?");
	        				    selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs, new String[] { uri.getLastPathSegment() });
	        				    break;

            case AUTEURS:	qb.setTables(OpenHelper.TABLE_AUTEUR);
                            qb.setProjectionMap(mNotesProjectionMapAuteur);
                            break;

            case AUTEUR_ID: qb.setTables(OpenHelper.TABLE_AUTEUR);
                            qb.setProjectionMap(mNotesProjectionMapAuteur);
                            qb.appendWhere(OpenHelper.COLUMN_ID + "=?");
                            selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs, new String[] { uri.getLastPathSegment() });
                            break;

	        default:
	            throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		SQLiteDatabase db = mopenHelper.getReadableDatabase();

        Cursor c = qb.query(db, projection, selection, selectionArgs,
                null, null, null);

        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
	}

	@Override
	public int update(Uri uri, ContentValues contentValues, String where, String[] whereArgs) {
        SQLiteDatabase db = mopenHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (sURIMatcher.match(uri)) {
            case CITATIONS:
                count = db.update(OpenHelper.TABLE_CITATION_FAVORIS, contentValues, where, whereArgs);
                break;

            case CITATION_ID:
                
                finalWhere = DatabaseUtils.concatenateWhere(
                        OpenHelper.COLUMN_ID + " = " + ContentUris.parseId(uri), where);
                count = db.update(OpenHelper.TABLE_CITATION_FAVORIS, contentValues, finalWhere, whereArgs);
                break;

            case AUTEURS:
                count = db.update(OpenHelper.TABLE_AUTEUR, contentValues, where, whereArgs);
                break;

            case AUTEUR_ID:

                finalWhere = DatabaseUtils.concatenateWhere(
                        OpenHelper.COLUMN_ID + " = " + ContentUris.parseId(uri), where);
                count = db.update(OpenHelper.TABLE_AUTEUR, contentValues, finalWhere, whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
	}

}
