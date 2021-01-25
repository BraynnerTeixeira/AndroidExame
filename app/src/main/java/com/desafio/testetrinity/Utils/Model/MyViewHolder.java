package com.desafio.testetrinity.Utils.Model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.desafio.testetrinity.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 *
 * FIZ PARA AJUDAR A INTERAGIR COM O LAYOUT DAS NEWS
 */
class MyViewHolder extends RecyclerView.ViewHolder{

    private TextView txtTitulo;
    private TextView txtFonte;
    private TextView txtData;
    private ImageView imgNews;


    MyViewHolder(@NonNull View itemView) {
        super(itemView);

        txtTitulo = itemView.findViewById(R.id.txt_titulo);
        txtFonte = itemView.findViewById(R.id.txt_fonteNews);
        txtData = itemView.findViewById(R.id.txt_data_noticia);
        imgNews = itemView.findViewById(R.id.img_news);
    }

    void setTitulo(String title) {
        this.txtTitulo.setText(title);
    }

    void setFonte(String sourceName) {
        this.txtFonte.setText(sourceName);
    }

    void setData(String date){
        this.txtData.setText(date);
    }

    void setImgNews(Context context ,String url){
        Picasso.get()
                .load(url)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(this.imgNews);
    }
}
