package com.example.ca2_retrofit.News;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ca2_retrofit.API.ApiClient;
import com.example.ca2_retrofit.API.ApiInterface;
import com.example.ca2_retrofit.Core.MainActivity;
import com.example.ca2_retrofit.Models.Article;
import com.example.ca2_retrofit.Models.News;
import com.example.ca2_retrofit.R;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    //    public static final String API_KEY = "4989e46f65f9481da014e0e06c495687";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private String TAG = MainActivity.class.getSimpleName();
    //  private TextView topHeadline;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container, false);
        

        Toolbar toolbar = v.findViewById(R.id.toolbar);


        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

//    topHeadline = findViewById(R.id.topheadlines);
        recyclerView = v.findViewById(R.id.recycleView);
//        layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        onLoadingSwipeRefresh("");

        return v;
    }


    public void LoadJson(final String keyword){
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


        String country = Utilis.getCountry();
        String language = Utilis.getLanguage();

        Call<News> call;

        if (keyword.length() > 0){
            call = apiInterface.getNewsSearch(keyword, language, "publishedAt");

        }else {
            call = apiInterface.getNews(keyword);
        }


        call.enqueue(new Callback<News>() {


            //code below attempts to retrieve News from the models folder and if it cannot then it returns a no results toast
            @Override
            public void onResponse(Call<News> call, Response<News> response ) {

                if (response.isSuccessful() && response.body().getArticle() != null){


                    if (!articles.isEmpty()){
                        articles.clear();
                    }

                    articles = response.body().getArticle();
                    adapter = new Adapter(articles, getContext());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                    initListener();

                } else {
                    Toast.makeText(getContext(), "No Results!", Toast.LENGTH_SHORT).show();
                }
            }

            private  void initListener(){

                adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ImageView imageView = view.findViewById(R.id.img);
                        Intent intent = new Intent(getContext(), NewsDetailActivity.class);

                        //This passes the data for the news article to the next intent
                        Article article = articles.get(position);
                        intent.putExtra("url", article.getUrl());
                        intent.putExtra("title", article.getTitle());
                        intent.putExtra("img", article.getUrlToImage());
                        intent.putExtra("date", article.getPublishedAt());
                        intent.putExtra("source", article.getSource().getName());
                        intent.putExtra("author", article.getAuthor());
                        intent.putExtra("detailedDesc", article.getDetailedDesc());

                        Pair<View, String> pair = Pair.create((View)imageView, ViewCompat.getTransitionName(imageView));
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                getActivity(),
                                pair
                        );

                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

            //THE BELOW CODE NEEDS TO BE UNCOMMENTED AND FIXED//

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setQueryHint("Search Latest News...");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if (query.length() > 2){
//                    onLoadingSwipeRefresh(query);
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//        searchMenuItem.getIcon().setVisible(false, false);
//
//        return true;
//    }

    @Override
    public void onRefresh() {
        LoadJson("");
    }

    private  void onLoadingSwipeRefresh(final String keyword){
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJson(keyword);
                    }
                }
        );
    }



}
