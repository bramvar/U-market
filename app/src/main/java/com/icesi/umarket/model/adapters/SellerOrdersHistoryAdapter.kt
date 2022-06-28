package com.icesi.umarket.model.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.icesi.umarket.R
import com.icesi.umarket.model.Order
import com.icesi.umarket.model.holders.SellerOrdersHistoryViewHolder

class SellerOrdersHistoryAdapter: RecyclerView.Adapter<SellerOrdersHistoryViewHolder>(){

    private val orders = ArrayList<Order>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerOrdersHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.seller_order_row,parent,false)
        val productViewHolder = SellerOrdersHistoryViewHolder(view)
        return productViewHolder
    }

    override fun onBindViewHolder(holder: SellerOrdersHistoryViewHolder, position: Int) {
        val order = orders[position]
        holder.bindProduct(order)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    fun clear() {
        val size = orders.size
        orders.clear()
        notifyItemRangeRemoved(0,size)
    }

    fun addOrder(order: Order){
            orders.add(order)
            notifyItemInserted(orders.size-1)
    }

}