package com.icesi.umarket.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.icesi.umarket.model.User
import com.icesi.umarket.model.holders.MarketViewHolder
import com.icesi.umarket.seller.SellerHomeActivity

object Util {

    fun loadImage(id: String, imgView: ImageView, path: String) {
        if (id != "") {
            Firebase.storage.reference.child(path)
                .child(id).downloadUrl
                .addOnSuccessListener {
                    Glide.with(imgView).load(it).into(imgView)
                }
        }
    }

    fun initRecycler(recycler: RecyclerView, activity: Activity, orientation: Int) : RecyclerView{
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(activity, orientation, false)
        return recycler
    }

    fun sendImg(id: String, path: String , uri: Uri): Boolean{
        var sended: Boolean = true;
        Firebase.storage.reference.child(path).child(id)
            .putFile(uri)
            .addOnSuccessListener {
                sended = true
            }
        return sended
    }
}