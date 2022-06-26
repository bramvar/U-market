package com.icesi.umarket

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.icesi.umarket.databinding.ActivityAdditionalConsumerInfoBinding.inflate
import com.icesi.umarket.databinding.FragmentEditProductDialogBinding
import com.icesi.umarket.model.Product
import com.icesi.umarket.seller.SellerMainOverviewFragment

class EditProductDialogFragment : DialogFragment() {

    private lateinit var _binding: FragmentEditProductDialogBinding
    private val binding get() = _binding!!
    private lateinit var product : Product
    private lateinit var newProduct : Product
    lateinit var onProductSellerObserver: SellerMainOverviewFragment.OnProductsOnSellerObserver

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentEditProductDialogBinding.inflate(LayoutInflater.from(context))

        var dialog = AlertDialog.Builder(requireContext())
            .setView(_binding.root)
            .create()

        loadData()

        _binding.acceptChangesButton.setOnClickListener {
            if(checkData()){
                onProductSellerObserver.editProduct(newProduct)
            }else{
                Toast.makeText(requireContext(), "Faltan campos por completar", Toast.LENGTH_SHORT).show()
            }
        }

        _binding.cancelChangesButton.setOnClickListener {
            this.dismiss()
        }

        return dialog
    }

    private fun checkData() : Boolean {
        var name= binding.nameProductEditSeller.text.toString()
        var description= binding.descriptProductEditSeller.text.toString()
        var price= binding.priceProductEditSeller.text.toString()

        if(name != "" && description != "" && price !=""){
           var priceInt = Integer.parseInt(price)
            newProduct = Product(product.id, name, priceInt,description, product.imageID)
            return true
        }else{
            return false
        }
    }

    private fun loadData(){
        var price = Integer.parseInt(product.price.toString())
        Log.e("Product in Dialog",product.name +" - "+product.description+" - "+product.price )
        //binding.priceProductEditSeller.setText(price)
    }

    fun setProduct(product: Product) {
        this.product = product
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditProductDialogFragment()
        const val TAG = "EditProductDialogFragment"
    }
}