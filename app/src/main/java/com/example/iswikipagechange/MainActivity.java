package com.example.iswikipagechange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    WikiPage[] wikiPages;
    ArrayList<String> paths;
    TextView textView;
    ListView listView;
    ArrayAdapter<String> adapter;
    Context context;
    Button reloadButton, addButton, getDataButton;
    EditText newUrlField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        context = this;

        textView  = findViewById(R.id.textView);
        listView = findViewById(R.id.list);
        reloadButton = findViewById(R.id.reloadButton);
        addButton = findViewById(R.id.addButton);
        getDataButton = findViewById(R.id.getDataButton);
        newUrlField = findViewById(R.id.newUrlField);

        wikiPages = DB.getAllRecords(this);  //возьмем все записи из базы данных

        //создадим строковый массив для заполнения списка
        paths = new ArrayList<>();
        for (int i=0; i<wikiPages.length; i++){
            paths.add(wikiPages[i].getUrl());
        }

        //адаптер для списка
        adapter = new ArrayAdapter<>(this,
                R.layout.item_layout, paths);
        listView.setAdapter(adapter);


        //кнопка получения новых данных с сервера
        getDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // для парсинга необходимо создать новый поток
                MyTask mt = new MyTask();
                mt.execute(paths.toArray(new String[paths.size()]));
            }
        });



        //лобавление записи в БД по нажатию на кнопку
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!newUrlField.getText().toString().equals("")){
                    DB.addWikiPage(new WikiPage(newUrlField.getText().toString()), v.getContext());
                    paths.add(newUrlField.getText().toString());
                //    mt.execute(paths.toArray(new String[paths.size()]));

                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"Страница успешно добавлена!", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Поле пустое!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //по нажатию на кнопку заполним таблицу в базе данных новыми значениями даты изменения и обновим список на экране
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB.updateRecords(wikiPages,context);
                Log.d("wiki7777", " update");
                wikiPages = DB.getAllRecords(context);
                paths.clear();
                for (int i=0; i<wikiPages.length; i++){
                    paths.add(wikiPages[i].getUrl() + "\n" + "Последняя дата правки в базе: "  + wikiPages[i].getDate() + "\n" + "Последняя дата правки на сайте: " + wikiPages[i].getNewDate() + "\n" + wikiPages[i].getIsChange());
                }
                adapter.notifyDataSetChanged();
            }
        });

    }
    // внутренний класс потока, в котором  и происходит парсинг сайта Википедии
    class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // params - это массив строк - url-адрессов для парсинга

            String str = "null";
            //Log.d("wiki7777", Arrays.toString(params));
            Document doc = null; // Здесь хранится HTML страничка википедии

            // по очереди парсим каждую страницу
            for (int i=0;i<params.length; i++) {
                //   Log.d("wiki7777", i + "");
                try {
                    // Считываем страницу
                    doc = Jsoup.connect(params[i] + "&action=history").get(); // добавляем к адресу страницы элемент, чтоб открыть историю изменения страницы
                    Elements elements = doc.getElementsByClass("mw-index-pager-list-header-first mw-index-pager-list-header"); // по классу находим дату последнего изменения
                    for (Element element : elements) {
                        str = element.getAllElements().text(); //берем дату
                        wikiPages[i].setNewDate(str); // записываем ее в поле для новой даты
                        String isChange = " \nзапись без изменений";  //флаг изменения
                        if (!wikiPages[i].getDate().equals(wikiPages[i].getNewDate())){
                            isChange = " \nзапись была изменена";
                        }
                        // формируем новую строку для списка на экране
                        paths.set(i, paths.get(i) + "\n" + "Последняя дата правки в базе: "  + wikiPages[i].getDate() + "\n" + "Последняя дата правки на сайте: " + wikiPages[i].getNewDate() + isChange);
                        //Log.d("wiki7777", str);

                    }
                } catch (IOException e) {
                    // Если не получилось считать
                    e.printStackTrace();
                }

            }
            return str  ;
        }




        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter.notifyDataSetChanged();
        }
    }


}