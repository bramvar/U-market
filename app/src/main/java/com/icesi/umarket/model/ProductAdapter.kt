package com.icesi.umarket.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.icesi.umarket.R
import java.io.File

class ProductAdapter: RecyclerView.Adapter<ProductViewHolder>() {

    private val products = ArrayList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.product_row,parent,false)
        val productViewHolder = ProductViewHolder(view)

        return productViewHolder
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val productn = products[position]
        holder.productNameRow.text = productn.name
        holder.productPriceRow.text = productn.price.toString()
        holder.producImageRow.setImageBitmap(setImageBitmap(productn.image))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun addProduct(product: Product){
        products.add(product)
    }

    private fun setImageBitmap(image: String?): Bitmap? {
        val file = image?.let { File(it) }
        val bitmap = BitmapFactory.decodeFile(file?.path)
        val thumpnail = Bitmap.createScaledBitmap(bitmap, bitmap.width/4, bitmap.height/4, true)

        return thumpnail
    }

}