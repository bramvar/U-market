package com.icesi.umarket.model

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity

class ShoppingCar(consumerName: String, marketNumber: String) {

    private var consumerName: String
    private var marketNumber: String
    var orders = ArrayList<Order>()

    init {
        this.consumerName = consumerName
        this.marketNumber = marketNumber
    }

    fun generateText(): String{
        var msg = ""
        for(order in orders){
            msg += "- "+order.amount+" de "+order.name+ "\n"
        }
        return msg
    }

    fun sendMessage(): Intent {
        val msj = generateText()
        val completeNumber = "57$marketNumber"
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = "whatsapp://send?phone=$completeNumber&text=$msj"
        intent.data = Uri.parse(uri)
        return intent
    }
}