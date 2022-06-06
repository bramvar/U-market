package com.icesi.umarket.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.icesi.umarket.R

class OrderSellerAdapter: RecyclerView.Adapter<OrderSellerViewHolder>()  {

    private val orders = ArrayList<Order>()

    override fun onBindViewHolder(holder: OrderSellerViewHolder, position: Int) {
        val order = orders[position]
        holder.bindProduct(order)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderSellerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.order_row,parent,false)
        val OrderViewHolder = OrderSellerViewHolder(view)
        return OrderViewHolder
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    fun clear() {
        val size = orders.size
        orders.clear()
        notifyItemRangeRemoved(0,size)
    }

    fun addProduct(order: Order){
        orders.add(order)
        notifyItemInserted(orders.size-1)
    }




}