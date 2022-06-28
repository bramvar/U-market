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
import com.icesi.umarket.databinding.FragmentConfirmPurchaseDiaglogBinding
import com.icesi.umarket.databinding.FragmentEditProductDialogBinding
import com.icesi.umarket.model.Product
import com.icesi.umarket.seller.SellerMainOverviewFragment

class EditProductDialogFragment : DialogFragment() {

    private lateinit var _binding: FragmentEditProductDialogBinding
    private val binding get() = _binding!!
    private lateinit var product : Product
    private lateinit var newProduct : Product
    lateinit var onProductSellerObserver: SellerMainOverviewFragment.OnProductsOnSellerObserver

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProductDialogBinding.inflate(inflater, container, false)
        loadData()

        _binding.acceptChangesButton.setOnClickListener {
            if(checkData()){
                onProductSellerObserver.editProduct(newProduct)
            }else{
                Toast.makeText(requireContext(), "Faltan campos por completar", Toast.LENGTH_SHORT).show()
            }
        }

        _binding.cancelChangesButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    private fun checkData() : Boolean {
        var name= binding.nameProductEditSeller.text.toString()
        var description= binding.descriptProductEditSeller.text.toString()
        var price= binding.priceProductEditSeller.text.toString()
        var amount = binding.amountProductEdit.text.toString()

        if(name != "" && description != "" && price !="" && amount !=""){
           var priceInt = Integer.parseInt(price)
            newProduct = Product(product.id, name, priceInt,description, product.imageID , Integer.parseInt(amount))
            return true
        }else{
            return false
        }
    }

    private fun loadData(){
        binding.nameProductEditSeller.setText(product.name)
        binding.descriptProductEditSeller.setText(product.description)
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