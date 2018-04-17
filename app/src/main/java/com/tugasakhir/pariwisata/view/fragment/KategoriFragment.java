package com.tugasakhir.pariwisata.view.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tugasakhir.pariwisata.R;
import com.tugasakhir.pariwisata.adapter.ListKategoriAdapter;
import com.tugasakhir.pariwisata.adapter.ListSemuaTempatAdapter;
import com.tugasakhir.pariwisata.adapter.RecyclerItemClickListener;
import com.tugasakhir.pariwisata.model.Hasil;
import com.tugasakhir.pariwisata.model.database.LocalData;
import com.tugasakhir.pariwisata.model.kategori.Getkategori;
import com.tugasakhir.pariwisata.model.kategori.Kategori;
import com.tugasakhir.pariwisata.model.semua.Semua;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class KategoriFragment extends Fragment {

    private View view;
    private ListKategoriAdapter adapter;
    protected RecyclerView list;
    private SwipeRefreshLayout mySwipeRefreshLayout;

    String TAG ="hasil2";
    List<Kategori> data = new ArrayList<>();
    public KategoriFragment() {
        // Required empty public constructor
    }

    private void init() {
        list = view.findViewById(R.id.list_menu);
        adapter = new ListKategoriAdapter(getActivity(), data);
        mySwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void getKategoriTempat() {
        if (LocalData.getKategoriList().isEmpty()) {
            new Getkategori().get(getContext(), new Hasil() {
                @Override
                public void sukses() {
                    Log.e(TAG, "sukses: ");
                    data.clear();
                    data.addAll(LocalData.getKategoriList());
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void gagal() {
                    Log.d(TAG, "gagal: ");
                }
            });
        } else {
            Log.d(TAG, "getSemuaTempat: ");
            data.addAll(LocalData.getKategoriList());
            adapter.notifyDataSetChanged();
        }
    }

    private void setKategoriTempat() {
        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);
    }

    private void onClick() {
        list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), list, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(getActivity().getApplicationContext(), DetailProdukActivity.class);
//                intent.putExtra("nama", String.valueOf(data.get(position).getNama()));
//                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        data.clear();
                        getKategoriTempat();
                        setKategoriTempat();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        v.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent_color));
        searchView.setQueryHint(getString(R.string.hint_search));
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(true);

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                query = query.toLowerCase();
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            public void callSearch(String query) {
//                mAllPlacesAdapter.getFilter().filter(query);
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.context_menu:
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_kategori, container, false);
        setHasOptionsMenu(true);

        init();
        getKategoriTempat();
        setKategoriTempat();
        onClick();
        return view;
    }

}
