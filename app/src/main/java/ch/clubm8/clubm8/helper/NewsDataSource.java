package ch.clubm8.clubm8.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ch.clubm8.clubm8.bl.News;

public class NewsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MyDbHelper dbHelper;
    private String[] allColumns = { "id", "date", "time", "title", "text" };

    public NewsDataSource(Context context) {
        dbHelper = new MyDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public News createNews(long id, String date, String time, String title, String text) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("date", date);
        values.put("time", time);
        values.put("title", title);
        values.put("text", text);
        long insertId = database.insert("news", null, values);
        Cursor cursor = database.query("news",
                allColumns, "id" + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        News newNews = cursorToNews(cursor);
        cursor.close();
        return newNews;
    }

    public void deleteNews(News news) {
        long id = news.getId();
        System.out.println("News deleted with id: " + id);
        database.delete("news", "id" + " = " + id, null);
    }

    public List<News> getAllNews() {
        List<News> newsList = new ArrayList<News>();

        Cursor cursor = database.query("news",
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            News news = cursorToNews(cursor);
            newsList.add(news);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return newsList;
    }

    private News cursorToNews(Cursor cursor) {
        News news = new News();
        news.setId(cursor.getLong(0));
        news.setTitle(cursor.getString(1));
        return news;
    }

}
