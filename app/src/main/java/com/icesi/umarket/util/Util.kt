package com.icesi.umarket.util

import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.icesi.umarket.model.User
import com.icesi.umarket.model.holders.MarketViewHolder

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

    fun getExtras(intent: Intent, extraString: String, classs: Class<*>): Any{
        return Gson().fromJson(
            intent.extras?.getString(extraString,""),
            classs
        )
    }

    fun setExtras(intent: Intent, extraString: String, classs: Class<*>): Any{
        return Gson().fromJson(
            intent.extras?.getString(extraString,""),
            classs
        )
    }

}