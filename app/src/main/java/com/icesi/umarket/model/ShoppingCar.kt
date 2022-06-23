package com.icesi.umarket.model

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class ShoppingCar() {
    lateinit var consumerName: String
    lateinit var currentMarket: Market
    var orders = ArrayList<Order>()


    fun getAmountOfOrders(): Int{
        return orders.size
    }

    fun loadOrder(order: Order){
        orders.add(order)
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
            msg += "\n-"+order.amount+" de "+order.name
            total += order.totalPrice
        }
        msg+="\n\nPara un total de $"+total
        msg+="\nMuchas gracias! Espero tu respuesta."
        return msg
    }

    fun sendMessage(): Intent {
        val msj = generateText()
        val completeNumber = "57${currentMarket.phoneNumber}"
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = "whatsapp://send?phone=$completeNumber&text=$msj"
        intent.data = Uri.parse(uri)
        loadOrders()
        return intent
    }

    fun loadOrders(){
        for(order in orders){
            order.idOrder = UUID.randomUUID().toString()
            var auxOrder = AuxOrder(order.idOrder)

            Firebase.firestore.collection("orders")
                .document(order.idOrder)
                .set(order)

            Firebase.firestore.collection("users")
                .document(order.idUser)
                .collection("orders")
                .document(order.idOrder)
                .set(auxOrder)

            Firebase.firestore.collection("markets")
                .document(currentMarket.id)
                .collection("pendentOrders")
                .document(order.idOrder)
                .set(auxOrder)
        }
    }
}