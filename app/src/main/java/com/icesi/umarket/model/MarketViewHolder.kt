package com.icesi.umarket.model

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.icesi.umarket.R

class MarketViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    //STATE
    var market: Market? = null

    //UI controllers
    var marketName:  TextView = itemView.findViewById(R.id.marketNameRowTextView)
    var marketImageRow: ImageView = itemView.findViewById(R.id.marketRowImage)
    var descriptMarket:  TextView = itemView.findViewById(R.id.descriptMarket)



    init {
        marketImageRow.setOnClickListener {
            //clickRowListener.onClickRowListener(pokemon)
        }


        //state
    fun bindMarket(market: Market){
        this.market = market
        marketName.text = market.marketName
        descriptMarket.text = market.marketDescription
        Log.e("Market: ", market.toString())
        if(market.imageID != ""){
            Firebase.storage.reference.child("market-image-profile").child(market.imageID!!).downloadUrl
                .addOnSuccessListener {
                    Glide.with(marketImageRow).load(it).into(marketImageRow)
                }
        }



    }
}