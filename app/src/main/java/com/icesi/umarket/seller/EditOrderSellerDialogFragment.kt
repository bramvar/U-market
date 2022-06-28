package com.icesi.umarket.seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.icesi.umarket.databinding.FragmentEditOrderSellerDialogBinding
import com.icesi.umarket.model.Order

class EditOrderSellerDialogFragment : DialogFragment(){

    /// View
    private lateinit var _binding: FragmentEditOrderSellerDialogBinding
    private val binding get() = _binding!!

    /// Object
    private lateinit var currentOrder : Order

    /// Listener
    lateinit var onProductSellerObserver: SellerOrderOverviewFragment.OnConfirmOrderListener

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
                onProductSellerObserver.editOrderSuccessful(currentOrder)
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


    private fun calculateTotalPrice(){
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