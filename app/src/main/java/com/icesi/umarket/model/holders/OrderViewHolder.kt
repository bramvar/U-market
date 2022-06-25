package com.icesi.umarket.model.holders

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.icesi.umarket.R
import com.icesi.umarket.model.Order
import com.icesi.umarket.util.Util

class OrderViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    //STATE
    var order: Order? = null

    //UI controllers
    var productImgOrderRow: ImageView = itemView.findViewById(R.id.productImgOrderRow)
    var productNameOrder: TextView = itemView.findViewById(R.id.productNameOrder)
    var amountProductOrder: TextView = itemView.findViewById(R.id.amountProductOrder)
    var priceProductOrder: TextView = itemView.findViewById(R.id.priceOrderRow)
    var statusProductOrder: TextView = itemView.findViewById(R.id.statusOrderConsumerText)


    fun bindOrder(order: Order) {
        this.order = order
        productNameOrder.text = order.name
        amountProductOrder.text = "  " + order.amount.toString()
        priceProductOrder.text = "  $" + order.totalPrice
        orderFlagColor(order.orderFlag)
        Util.loadImage(order.imageID,productImgOrderRow, "product-images")
    }

    private fun orderFlagColor(orderFlag: String) {
        when(orderFlag){
            "exitosa" -> statusProductOrder.setTextColor(Color.rgb(139,195,74))
            "cancelada" ->statusProductOrder.setTextColor(Color.rgb(255,51,51))
        }
        statusProductOrder.text = orderFlag
    }
}