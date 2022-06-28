package com.icesi.umarket

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.icesi.umarket.databinding.FragmentConfirmPurchaseDiaglogBinding
import com.icesi.umarket.databinding.FragmentEditOrderSellerDialogBinding
import com.icesi.umarket.databinding.FragmentEditProductDialogBinding
import com.icesi.umarket.model.Order
import com.icesi.umarket.model.Product
import com.icesi.umarket.seller.SellerMainOverviewFragment

class EditOrderSellerDialogFragment : DialogFragment(){
    private lateinit var _binding: FragmentEditOrderSellerDialogBinding
    private val binding get() = _binding!!
    private lateinit var currentOrder : Order
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
        _binding = FragmentEditOrderSellerDialogBinding.inflate(inflater, container, false)

        _binding.acceptOrderEditBtn.setOnClickListener {
            if(binding.editAmountOrderText.text.toString() !="" ){
                calculateTotalPrice()
                onProductSellerObserver.editOrderSuccessfull(currentOrder)
                dismiss()
            }else{
                Toast.makeText(requireContext(), "Escribe una cantidad valida", Toast.LENGTH_SHORT).show()
            }
        }
        _binding.cancelOrderEditBtn.setOnClickListener {
            dismiss()
        }

        return _binding.root
    }


    fun calculateTotalPrice(){
        currentOrder.amount = Integer.parseInt(binding.editAmountOrderText.text.toString())
        currentOrder.totalPrice = currentOrder.amount * currentOrder.unitPrice
    }

    fun setOrder(order: Order) {
        currentOrder = order
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditOrderSellerDialogFragment()
        const val TAG = "EditOrderSellerDialogFragment"
    }
}