package com.sportzinteractive.demo.customviews

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.sportzinteractive.demo.R

class CustomDialog(myActivity: Activity) {
    private var activity: Activity? = null
    private var dialog: AlertDialog? = null

    init {
        activity = myActivity
    }

    @SuppressLint("InflateParams")
    fun show() {
        val builder = AlertDialog.Builder(
            activity!!
        )
        val inflater = activity!!.layoutInflater
        builder.setView(inflater.inflate(R.layout.alert_loading_dialog, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog!!.show()
    }

    fun dismiss() {
        dialog!!.dismiss()
    }
}