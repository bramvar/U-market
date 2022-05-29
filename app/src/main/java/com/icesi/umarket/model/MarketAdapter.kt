package com.icesi.umarket.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.icesi.umarket.R
import java.io.File

class MarketAdapter: RecyclerView.Adapter<MarketViewHolder>() {

    private val markets = ArrayList<Market>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.market_row,parent,false)
        val marketViewHolder = MarketViewHolder(view)

        return marketViewHolder
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val market = markets[position]
        Log.e("Market en onBind: ", market.toString())
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


    private fun setImageBitmap(image: String?): Bitmap? {
        val file = image?.let { File(it) }
        val bitmap = BitmapFactory.decodeFile(file?.path)
        val thumpnail = Bitmap.createScaledBitmap(bitmap, bitmap.width/4, bitmap.height/4, true)

        return thumpnail
    }

}