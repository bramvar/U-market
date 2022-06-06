package com.icesi.umarket.model

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.icesi.umarket.R
import kotlin.random.Random

import kotlin.random.Random.Default.nextInt

class OrderSellerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    var order: Order? = null


    //UI controllers
    var orderName: TextView = itemView.findViewById(R.id.name)
    var amount: TextView = itemView.findViewById(R.id.descriptOrder)
    var totalOrder:TextView =itemView.findViewById(R.id.TotalOrder)

    //state

    init {

    }

    fun bindProduct(order: Order){
        this.order = order

        orderName.text = order.name.toString()
        amount.text = order.amount.toString()
        totalOrder.text= ""+(Random.nextInt(0, 100))



    }


}