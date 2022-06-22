package com.icesi.umarket.model

class Order(
    var amount: Int = 0,
    var name: String = "",
    var unitPrice: Int = 0,
    var totalPrice: Int = 0,
    val imageID: String="",
    val idMarket: String="",
    val idProduct: String ="",
    var idOrder: String ="",
    var orderFlag: String =""
)