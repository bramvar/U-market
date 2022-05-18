package com.icesi.umarket

import java.io.Serializable

data class Seller(
    val id: String="",
    val marketName: String="",
    val sellerName: String="",
    val email: String="",
    val password: String="",
    val phone: String="",
    val marketDescription: String="",
    val role:String=""
) : Serializable