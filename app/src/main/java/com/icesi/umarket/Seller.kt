package com.icesi.umarket

import java.io.Serializable

data class Seller(
    val id: String="",
    val sellerName: String="",
    val email: String="",
    val password: String="",
    val phone: String="",
    val role:String="",
    val marketID:String="",
    val marketDescription: String="",
) : Serializable
