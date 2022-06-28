package com.icesi.umarket.model

import com.google.firebase.Timestamp

class Order(
    var amount: Int = 0,
    var name: String = "",
    var unitPrice: Int = 0,
    var totalPrice: Int = 0,
    val imageID: String="",
    val idMarket: String="",
    val idProduct: String ="",
    var idOrder: String ="",
    var orderFlag: String ="",
    var idUser: String ="",
    var date: Timestamp? = null
)

class AuxOrder(
    var idOrder: String ="",
    var date: Timestamp? = null
)