package com.icesi.umarket.model

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity

class ShoppingCar() {
    lateinit var consumerName: String
    lateinit var  marketNumber: String
    var orders = ArrayList<Order>()


    fun getAmountOfOrders(): Int{
        return orders.size
    }

    fun loadOrder(order: Order){
        orders.add(order)
        Log.e("Size ",""+orders.size)
    }
    fun generateConfirmText(): String{
        var msg =""
        for(order in orders){
            msg += "-"+order.amount+" de "+order.name+ "\n"
        }
        return msg
    }

    fun generateText(): String{
        var msg = "Hola! Soy $consumerName y me gustaria comprar lo siguiente: \n"
        var total = 0
        for(order in orders){
            msg += "-"+order.amount+" de "+order.name+ "\n"
            total += order.totalPrice
        }
        msg+="\nPara un total de $"+total
        msg+="\nMuchas gracias! Espero tu respuesta."
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