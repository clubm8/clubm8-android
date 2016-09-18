package ch.clubm8.clubm8;


import android.app.DownloadManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebugFragment extends Fragment {
    SQLiteDatabase mydatabase;
    TextView txtView;

    public DebugFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_debug, container, false);

        mydatabase = getActivity().openOrCreateDatabase("schedule", getActivity().MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS TutorialsPoint(Username VARCHAR,Password VARCHAR);");
        mydatabase.execSQL("INSERT INTO TutorialsPoint VALUES('admin','admin');");

        txtView = (TextView) v.findViewById(R.id.textView);
        Button btn1 = (Button) v.findViewById(R.id.button1);
        Button btn2 = (Button) v.findViewById(R.id.button2);
        Button btn3 = (Button) v.findViewById(R.id.button3);


        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Cursor resultSet = mydatabase.rawQuery("Select * from TutorialsPoint", null);
                resultSet.moveToFirst();
                String username = resultSet.getString(0);
                String password = resultSet.getString(1);
                txtView.setText("data from sqlite database: " + username + " " + password);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtView.setText("cleared.");
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String url = "http://sb.lilo.ch/api/v1/news/";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("q", "android");
                params.put("rsz", "8");
                client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        txtView.setText(response.toString());
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject e) {
                        // Handle the failure and alert the user to retry
                        Log.e("ERROR", e.toString());
                    }
                });
            }

        });
        return v;
    }

}
