package com.icesi.umarket.model.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.icesi.umarket.R
import com.icesi.umarket.model.Product
import com.icesi.umarket.seller.SellerMainOverviewFragment

class ProductSellerViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    //STATE
    var product: Product? = null
    lateinit var onProductSellerObserver : SellerMainOverviewFragment.onProductsOnSellerObserver

    //UI controllers
    var producImageRow: ImageView = itemView.findViewById(R.id.marketRowImage)
    var productNameRow: TextView = itemView.findViewById(R.id.marketNameRowTextView)
    var productPriceRow: TextView = itemView.findViewById(R.id.descriptMarket)
    //state

    init {
        producImageRow.setOnClickListener {
            onProductSellerObserver.sendProduct(product!!)
        }
    }

    fun bindProduct(product: Product){
        this.product = product
        productNameRow.text = product.name
        productPriceRow.text = product.price.toString()

        if(product.imageID != ""){
            Firebase.storage.reference.child("product-images").child(product.imageID!!).downloadUrl
                .addOnSuccessListener {
                    Glide.with(producImageRow).load(it).into(producImageRow)
                }
        }
    }
}