package com.eldenbuild.util

import android.content.Context
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.eldenbuild.R
import com.eldenbuild.databinding.DialogInputTextBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


object Dialog {

    fun buildCustomDialog(
        context: Context,
        inflater: LayoutInflater,
        getNameAndType: (String, String,String?) -> Unit
    ): AlertDialog {
        val inputM = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val builder = MaterialAlertDialogBuilder(context)
        val bindingDialog: DialogInputTextBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_input_text,
                null,
                false
            )

        bindingDialog.textName.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus){
                inputM.hideSoftInputFromWindow(v.windowToken,0)
            }
        }
        builder.setView(bindingDialog.root)

        builder.setPositiveButton("Accept") { dialog, _ ->
            getNameAndType(
                bindingDialog.textName.text.toString(),
                bindingDialog.autoCompleteList.text.toString(),
                null
            )
            dialog.dismiss()
        }

        builder.setNegativeButton(R.string.cancel_text) { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
        bindingDialog.textName.doAfterTextChanged {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = !it.isNullOrBlank()
        }
        return dialog
    }

    fun buildDialog(
        context: Context,
        message: Int,
        title: Int,
        positiveAction: () -> Unit

    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(context)
        builder.apply {
            setTitle(title)
            setMessage(message)
            show()
            setPositiveButton(R.string.accept_text) { dialog, _ ->
                positiveAction()
                dialog.dismiss()
            }
        }
        return builder.create()
    }
}

