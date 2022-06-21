package com.icesi.umarket.model.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.icesi.umarket.R
import com.icesi.umarket.model.Order

class OrderViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    //STATE
    var order: Order? = null

    //UI controllers
    var productImgOrderRow: ImageView = itemView.findViewById(R.id.productImgOrderRow)
    var productNameOrder: TextView = itemView.findViewById(R.id.productNameOrder)
    var amountProductOrder: TextView = itemView.findViewById(R.id.amountProductOrder)
    var priceProductOrder: TextView = itemView.findViewById(R.id.priceOrderRow)

    //state

    fun bindOrder(order: Order) {
        this.order = order
        productNameOrder.text = order.name
        amountProductOrder.text = "  " + order.amount.toString()
        priceProductOrder.text = "  $" + order.totalPrice
        var imageID = order.imageID
        if (imageID != "") {
            Firebase.storage.reference.child("product-images").child(imageID).downloadUrl
                .addOnSuccessListener {
                    Glide.with(productImgOrderRow).load(it).into(productImgOrderRow)
                }
        }
    }
}