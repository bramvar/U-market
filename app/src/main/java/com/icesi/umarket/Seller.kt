package com.icesi.umarket

import java.io.Serializable

data class Seller(
    val id: String="",
    val sellerName: String="",
    val email: String="",
    val password: String="",
    val phone: String="",
<<<<<<< HEAD
    val role:String="",
    val marketID:String=""
)
=======
    val marketDescription: String="",
    val role:String=""
) : Serializable
>>>>>>> 6fd07b087b20971091fa6bc90914f79c0fe758b8
