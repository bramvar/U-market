package com.icesi.umarket.model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.icesi.umarket.R

class ProductViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    //UI controllers
    var producImageRow: ImageView = itemView.findViewById(R.id.productImage)
    var productNameRow: TextView = itemView.findViewById(R.id.productNameTextView)
    var productPriceRow: TextView = itemView.findViewById(R.id.productPriceTextView)

    //state

    init {

    }
}