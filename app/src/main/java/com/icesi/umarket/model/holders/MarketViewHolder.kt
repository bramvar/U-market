package com.icesi.umarket.model.holders

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.icesi.umarket.consumer.ConsumerMainOverviewFragment
import com.icesi.umarket.R
import com.icesi.umarket.model.Market
import com.icesi.umarket.util.Util

class MarketViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    //STATE
    var market: Market? = null
    lateinit var onSellerObserver: ConsumerMainOverviewFragment.SellerObserver

    //UI controllers
    var marketName: TextView = itemView.findViewById(R.id.marketNameRowTextView)
    var marketImageRow: ImageView = itemView.findViewById(R.id.marketRowImage)
    var descriptMarket: TextView = itemView.findViewById(R.id.descriptMarket)

    /**
     * Envia en patron observer el nombre y la descripcion
     */
    init {

        marketImageRow.setOnClickListener {
            var marketE = market!!
            onSellerObserver.sendMarket(marketE)
        }
    }

    fun bindMarket(marketBind: Market) {

        market = marketBind
        marketName.text = market!!.marketName
        descriptMarket.text = market!!.marketShortDescription
        Util.loadImage(market!!.imageID.toString(), marketImageRow, "market-image-profile")

        Log.e("Market Img en Bind: ",marketBind.imageID )
        Log.e("Market Descrip Bind: ",marketBind.marketDescription )
        Log.e("Market Short Bind: ",marketBind.marketShortDescription )

    }
}
