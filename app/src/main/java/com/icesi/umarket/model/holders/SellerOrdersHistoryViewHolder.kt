package com.icesi.umarket.model.holders

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.icesi.umarket.R
import com.icesi.umarket.SellerOrderOverviewFragment
import com.icesi.umarket.model.Order
import com.icesi.umarket.util.Util

class SellerOrdersHistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    //STATE
    lateinit var order: Order

    var productImg: ImageView = itemView.findViewById(R.id.product_img_order)
    var productName: TextView = itemView.findViewById(R.id.productname_order_text)
    var username: TextView = itemView.findViewById(R.id.username_order_text)
    var amount: TextView = itemView.findViewById(R.id.amount_order_text)
    var price: TextView = itemView.findViewById(R.id.price_order_text)
    var status: TextView = itemView.findViewById(R.id.status_order_text)

    lateinit var orderId: String

    fun bindProduct(order: Order) {
        this.order = order
        productName.setText(order.name)
        username.setText(order.name)
        amount.setText(order.amount.toString())
        price.setText("$" + order.totalPrice)
        orderId = order.idOrder
        status.setText(order.orderFlag)
        Util.loadImage(order.imageID,productImg,"product-images")
    }
}