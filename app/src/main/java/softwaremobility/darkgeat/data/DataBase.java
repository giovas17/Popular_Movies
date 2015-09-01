package softwaremobility.darkgeat.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import softwaremobility.darkgeat.objects.Movie;
import softwaremobility.darkgeat.popularmovies1.R;

/**
 * Created by darkgeat on 17/07/15.
 */
public class DataBase {

    /** --------------------------------- Name of Tables ------------------------------------**/
    private static final String nTMovies = "Movies";
    /** --------------------------------- DataBase name -------------------------------------**/
    private static final String DataBaseName = "MoviesDataBase";
    /** --------------------------------- Table members -------------------------------------**/
    private static final String Key_Id = "id";
    private static final String Key_Title = "title";
    private static final String Key_Popularity = "popularity";
    private static final String Key_Rating = "rating";
    private static final String Key_Description = "summary";
    private static final String Key_Reviews = "reviews";
    private static final String Key_Trailers = "trailers";
    private static final String Key_Thumbnail = "thumbnail";
    private static final String Key_Poster = "poster";
    private static final String Key_Preview = "preview";
    /** --------------------------------- Data Base Version ---------------------------------**/
    private static final int version = 1;
    /** --------------------------------- Table Statements ----------------------------------**/
    private static final String TMovies = "CREATE TABLE " + nTMovies + " (" +
                                            Key_Id + " INTEGER PRIMARY KEY, " +
                                            Key_Title + " TEXT NOT NULL, " +
                                            Key_Description + " TEXT NOT NULL, " +
                                            Key_Popularity + " FLOAT, " +
                                            Key_Rating + " FLOAT, " +
                                            Key_Reviews + " TEXT NOT NULL, " +
                                            Key_Trailers + " TEXT NOT NULL, " +
                                            Key_Thumbnail + " TEXT NOT NULL, " +
                                            Key_Poster + " TEXT NOT NULL, " +
                                            Key_Preview + " TEXT);";
    /** ---------------------------------- SQLite Helper ------------------------------------**/
    private MyHelper myDB;
    private SQLiteDatabase myDataBase;
    private final Context mContext;

    public DataBase(Context c){ mContext = c; }

    private static class MyHelper extends SQLiteOpenHelper{

        public MyHelper(Context context) {
            super(context, DataBaseName, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TMovies);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + nTMovies);
            onCreate(db);
            db.setVersion(newVersion);
        }
    }
    /**  -----------------------------------------------------------------------------------**/

    /**
     * Open the DataBase
     * @return The database opened with write permissions
     */
    public DataBase open(){
        myDB = new MyHelper(mContext);
        myDataBase = myDB.getWritableDatabase();
        return this;
    }

    /**
     * Open the DataBase
     * @return The database opened with read permissions
     */
    public DataBase open_read(){
        myDB = new MyHelper(mContext);
        myDataBase = myDB.getReadableDatabase();
        return this;
    }

    /**
     * Close the DataBase
     */
    public void close(){
        myDB.close();
        myDataBase.close();
    }

    /**
     * This method is used to make a new entry for movies table
     * @param movie Movie object that represents a virtual instance of the movie.
     * @param reviews String that represents the JSON string of reviews.
     * @param trailers String that represents the JSON string of trailers.
     */
    public void newEntryMovies(Movie movie, String reviews, String trailers){
        open();
        ContentValues registro = new ContentValues();
        registro.put(Key_Id, movie.getId());
        registro.put(Key_Title, movie.getTitle());
        registro.put(Key_Description, movie.getDescription());
        registro.put(Key_Popularity, movie.getPopularity());
        registro.put(Key_Rating, movie.getRating());
        registro.put(Key_Reviews, reviews);
        registro.put(Key_Trailers, trailers);
        registro.put(Key_Thumbnail, movie.getPoster_thumbnail());
        registro.put(Key_Poster, movie.getPoster_image_path());
        registro.put(Key_Preview, movie.getPreview_image_path());
        myDataBase.insert(nTMovies, null, registro);
        close();
    }

    /**
     * This method is used to retrieve data from movies table and you can sorted the data by sortBy
     * parameter.
     * @param sortBy String that represents the field that will sorted the retrieved data from DB
     * @param sortByValue String that represents the value of the sorted by parameter
     * @return Cursor that retrieves the data from the db of the query
     */
    public Cursor getMoviesOrderedBy(@Nullable String sortBy, @Nullable String sortByValue){
        open();
        String[] columns = new String[]{ Key_Id,Key_Poster,Key_Rating,Key_Popularity,Key_Title,Key_Description,Key_Trailers,Key_Reviews,Key_Thumbnail};
        Cursor cursor = myDataBase.query(nTMovies,columns,sortBy,new String[]{sortByValue},null,null,sortBy);
        close();
        return cursor;
    }

    public void removeMovie(long idMovie){
        open();
        myDataBase.delete(nTMovies,Key_Id + "= ?", new String[]{String.valueOf(idMovie)});
        close();
    }

    public boolean isFavorite(long idMovie){
        open();
        String[] columns = new String[]{Key_Id};
        Cursor cursor = myDataBase.query(nTMovies,columns,Key_Id + " = ?", new String[]{String.valueOf(idMovie)},null,null,null);
        if (cursor.getCount() > 0){
            close();
            return true;
        }else{
            close();
            return false;
        }
    }

    /**
     * This method says if the table selected is empty
     * @param tableName String that represents the table that will be check.
     * @param primaryKey String that represents the primary field of the table selected.
     * @return a boolean values that represents if the table is empty.
     */
    public boolean isEmpty(String tableName, String primaryKey){
        try {
            open();
            Cursor c = myDataBase.query(tableName,new String[]{primaryKey},null,null,null,null,null);
            if(c.moveToFirst()){
                return false;
            }else {
                return true;
            }
        }catch (Exception e){
            return true;
        }finally {
            close();
        }
    }

    /**
     * This method is used to delete all data from the table selected
     * @param table String that represents the name of the table that will be erased
     */
    public void DeleteDataFromTable(String table){
        open();
        myDataBase.execSQL("DROP TABLE IF EXISTS " + table);
        if(table == nTMovies){
            myDataBase.execSQL(TMovies);
        }
        close();
    }
}
