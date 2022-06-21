package com.icesi.umarket.model.holders

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
            onSellerObserver.sendMarket(Market(market!!.id,"", marketName.text.toString(),descriptMarket.text.toString(), market!!.imageID, market!!.phoneNumber))
        }

    }
        //state
        fun bindMarket(market: Market) {
            this.market = market
            marketName.text = market.marketName
            descriptMarket.text = market.marketDescription
            if (market.imageID != "") {
                Firebase.storage.reference.child("market-image-profile")
                    .child(market.imageID!!).downloadUrl
                    .addOnSuccessListener {
                        Glide.with(marketImageRow).load(it).into(marketImageRow)
                    }
            }
        }
    }
