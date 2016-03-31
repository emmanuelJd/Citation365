package com.citation.emmanuel.citation365.stockage;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {

	private TypedArray id_citation_link = null;
    private TypedArray id_auteur_link = null;
    private TypedArray citation = null;
    private TypedArray auteur = null;
	
    public static final String TABLE_CITATION_FAVORIS = "citation_favoris";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ID_CITATION_LINK = "id_citation_link";
    public static final String COLUMN_ID_AUTEUR_LINK = "id_auteur_link";
    public static final String COLUMN_CITATION = "citation";
    public static final String COLUMN_AUTEUR = "auteur";

    public static final String TABLE_AUTEUR = "auteur_image";
    //public static final String COLUMN_ID_AUTEUR = "_id";
    public static final String COLUMN_AUTEUR_FONCTION = "auteur_fonction";
    public static final String COLUMN_IMAGE_LINK = "auteur_image_link";

    private static final String DATABASE_NAME = "citation.db";
    private static final int DATABASE_VERSION = 1;

    
    private static final String CREATE_TABLE_CITATION_FAVORIS = "create table "
        + TABLE_CITATION_FAVORIS + "(" + COLUMN_ID
        + " INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_ID_CITATION_LINK+" TEXT,"+ COLUMN_ID_AUTEUR_LINK+" TEXT,"+COLUMN_CITATION+" TEXT, "+COLUMN_AUTEUR+" TEXT);";

    private static final String CREATE_TABLE_AUTEUR = "create table "
            + TABLE_AUTEUR + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ COLUMN_ID_AUTEUR_LINK+" TEXT,"+COLUMN_IMAGE_LINK+" TEXT,"+COLUMN_AUTEUR_FONCTION+" TEXT);";

    public OpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_CITATION_FAVORIS);
        db.execSQL(CREATE_TABLE_AUTEUR);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_CITATION_FAVORIS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_AUTEUR);
		this.onCreate(db);
	}

}
