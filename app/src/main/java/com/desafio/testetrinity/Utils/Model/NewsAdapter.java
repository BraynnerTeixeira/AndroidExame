package com.desafio.testetrinity.Utils.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desafio.testetrinity.R;
import com.desafio.testetrinity.Utils.Api.Article;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 * PARA CONTROLAR A MY VIEWHOLDER
 */

public class NewsAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<Article> newsList;

    public NewsAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.newsList = articleList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_news, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Article news = newsList.get(position);


        holder.setTitulo(news.getTitle());
        holder.setFonte(news.getSource().getName());
        holder.setData(news.getPublishedAt());
        holder.setImgNews(context, news.getUrlToImage());
    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

}


