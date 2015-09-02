package on.od.ua.news.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import on.od.ua.news.DetailActivity;
import on.od.ua.news.MainActivity;
import on.od.ua.news.R;
import on.od.ua.news.utils.DOMParser;
import on.od.ua.news.utils.InternetUtil;
import on.od.ua.news.utils.Item;
import on.od.ua.news.utils.MyDataBase;
import on.od.ua.news.utils.NewsCustomAdapter;


/**
 * Created by Lubomyr Shershun on 23.07.2015.
 * l.sherhsun@gmail.com
 */
public class MainFragment extends Fragment {
    static final String ARG_SECTION_NUMBER = "section_number";
    private NewsCustomAdapter adapter;
    private List<Item> listItem;
    private DbTask atask = null;
    private int preLast;
    private SwipeRefreshLayout swipe;
    private ListView listView = null;
    private ProgressBar progressBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) rootView.findViewById(R.id.listview_news);
        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);

        progressBar=(ProgressBar)rootView.findViewById(R.id.list_progress);


        listItem = new ArrayList<>();


        adapter = new NewsCustomAdapter(getActivity(), listItem, true);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("title", adapter.getItemConverted(position).getTitle());
                getActivity().startActivity(intent);
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateNews();

            }
        });
        return rootView;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("MainFragment", "onCreateOptionsMenu");
        inflater.inflate(R.menu.mainfragment, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        updateNews();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (atask != null && (atask.getStatus() == AsyncTask.Status.RUNNING)) {
            atask.cancel(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.i("MAINFRAGMENT", "onOptionsItemSelected");
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh: {
                Log.i("MAINFRAGMENT", "refresh");
                listView.setSelection(0);
                //  preLast=0;
                //  to=20;

                updateNews();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));

    }

    private void updateNews() {
        Log.i("UpdateNews", "UpdateNews");
        atask = new DbTask();
        atask.execute();

    }

    private class DbTask extends AsyncTask<Void, Void, Void> {
        private List<Item> list;

        @Override
        protected void onPreExecute() {
            swipe.setRefreshing(true);
            progressBar.setVisibility(View.GONE);

        }

        @Override
        protected Void doInBackground(Void... params) {
            Context context = getActivity();
            list = new ArrayList<>();
            DOMParser parser = new DOMParser(context);
           InternetUtil internet = new InternetUtil(context);
            if (internet.isConnected()) parser.parseXML();
            if (context != null) {
                MyDataBase db = new MyDataBase(context);
                list.clear();
                list.addAll(db.getItems());
                Collections.sort(list);
                listItem.addAll(list);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            swipe.setRefreshing(false);
            adapter.notifyDataSetChanged();

        }
    }
}



