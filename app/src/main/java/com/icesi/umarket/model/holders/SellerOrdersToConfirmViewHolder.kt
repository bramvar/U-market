package com.icesi.umarket.model.holders

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.icesi.umarket.R
import com.icesi.umarket.SellerOrderOverviewFragment
import com.icesi.umarket.model.Order
import com.icesi.umarket.util.Util

class SellerOrdersToConfirmViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    //STATE
    lateinit var order: Order
    lateinit var onOrderConfirmObserver:  SellerOrderOverviewFragment.OnConfirmOrderListener
    lateinit var onChangesInOrderListener:  SellerOrdersToConfirmViewHolder.OnChangesInOrderListener

    var productImg: ImageView = itemView.findViewById(R.id.product_img_order)
    var productName: TextView = itemView.findViewById(R.id.productname_text)
    var username: TextView = itemView.findViewById(R.id.username_text)
    var amount: TextView = itemView.findViewById(R.id.amount_text)
    var price: TextView = itemView.findViewById(R.id.price_text)

    var accepted_btn: Button = itemView.findViewById(R.id.accepted_btn)
    var declined_btn: Button = itemView.findViewById(R.id.declined_btn)
    var partially_btn: Button = itemView.findViewById(R.id.partially_btn)

    lateinit var orderId: String

    init {
        accepted_btn.setOnClickListener{
            onOrderConfirmObserver.confirmOrder(order.idOrder)
            onChangesInOrderListener.deleteOrder(order)
        }

        declined_btn.setOnClickListener{
            onOrderConfirmObserver.cancelOrder(order.idOrder)
            onChangesInOrderListener.deleteOrder(order)
        }

        partially_btn.setOnClickListener{
            onOrderConfirmObserver.editOrder(order.idOrder)
        }
    }

    fun bindProduct(order: Order) {
        this.order = order
        productName.setText(order.name)
        username.setText(order.name)
        amount.setText(order.amount.toString())
        price.setText("$" + order.totalPrice)
        orderId = order.idOrder
        Util.loadImage(order.idOrder,productImg, "product-images")

    }

    interface OnChangesInOrderListener{
        fun deleteOrder(order:Order)
    }
}