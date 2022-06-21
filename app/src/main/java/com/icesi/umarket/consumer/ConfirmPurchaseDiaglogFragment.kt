package com.icesi.umarket.consumer

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.icesi.umarket.databinding.FragmentConfirmPurchaseDiaglogBinding

class ConfirmPurchaseDiaglogFragment : DialogFragment() {
    private lateinit var _binding: FragmentConfirmPurchaseDiaglogBinding
    lateinit var orderText: String
    lateinit var onConfirmPurchaseObserver: ConfirmPurchaseObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfirmPurchaseDiaglogBinding.inflate(inflater,container,false)
        _binding.orderText.text = orderText

        // Inflate the layout for this fragment
        return _binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar compra")
            .setMessage(orderText)
            .setPositiveButton("Confirmar")
            { task, error ->
                task.cancel()
                onConfirmPurchaseObserver.confirm()
            }
            .setNegativeButton("Descartar") { task, error ->
                task.cancel()
                onConfirmPurchaseObserver.discard()
            }
            .setNeutralButton("Seguir comprando") { task, error ->

            }
            .create()

        companion object {
            @JvmStatic
            fun newInstance() = ConfirmPurchaseDiaglogFragment()
            const val TAG = "PurchaseConfirmationDialog"
        }

    interface ConfirmPurchaseObserver{
        fun confirm()
        fun discard()
    }

    }


