package com.icesi.umarket.model.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.icesi.umarket.consumer.ConsumerMainOverviewFragment
import com.icesi.umarket.R
import com.icesi.umarket.model.Market
import com.icesi.umarket.model.holders.MarketViewHolder
import java.io.File

class MarketAdapter: RecyclerView.Adapter<MarketViewHolder>() {

    private val markets = ArrayList<Market>()
    lateinit var onSellerObserver: ConsumerMainOverviewFragment.SellerObserver

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.market_row,parent,false)
        val marketViewHolder = MarketViewHolder(view)
        marketViewHolder.onSellerObserver = onSellerObserver
        return marketViewHolder
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val market = markets[position]
        holder.bindMarket(market)
    }

    override fun getItemCount(): Int {
        return markets.size
    }

    fun clear() {
        val size = markets.size
        markets.clear()
        notifyItemRangeRemoved(0,size)
    }

    fun addMarket(market: Market){
        markets.add(market)
        notifyItemInserted(markets.size-1)
    }
}