package com.dementev.interactingwiththelist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {
    private SharedPreferences mySharedPref;
    private static String NOTE_TEXT = "note_text";
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView list = findViewById(R.id.list);
        List<Map<String, String>> values = prepareContent();
        BaseAdapter listContentAdapter = createAdapter(values);
        list.setAdapter(listContentAdapter);

        mySharedPref = getSharedPreferences(getString(R.string.large_text), MODE_PRIVATE);
        SharedPreferences.Editor myEditor = mySharedPref.edit();
        String text = String.valueOf(getText(R.string.large_text));
        myEditor.putString(NOTE_TEXT, text);
        myEditor.apply();

        list.setOnItemClickListener((parent, view, position, id) -> {
            values.remove(position);
            listContentAdapter.notifyDataSetChanged();
        });

        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            values.clear();
            values.addAll(prepareContent());
            mSwipeRefreshLayout.setRefreshing(false);
            listContentAdapter.notifyDataSetChanged();
        });
    }

    @NonNull
    private BaseAdapter createAdapter(List<Map<String, String>> values) {
        return new SimpleAdapter(this, values, R.layout.simple_adapter,
                new String[]{"text", "number"},
                new int[]{R.id.textView, R.id.textView2});
    }

    @NonNull
    private List<Map<String, String>> prepareContent() {
        List<Map<String, String>> arrayList = new ArrayList<>();

        String[] arrayContent = getString(R.string.large_text).split("\n\n");

        for (String s : arrayContent) {
            Map<String, String> map = new HashMap<>();
            map.put("text", s);
            map.put("number", String.valueOf(s.length()));
            arrayList.add(map);

        }
        return arrayList;
    }
}