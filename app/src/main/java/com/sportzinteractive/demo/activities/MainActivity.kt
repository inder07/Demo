package com.sportzinteractive.demo.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.sportzinteractive.demo.R
import com.sportzinteractive.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.listener = this
    }

    override fun onClick(view: View?) {
        view?.let {
            when (it.id) {
                R.id.cv1 -> {
                    startNextActivity("https://demo.sportz.io/nzin01312019187360.json")
                }
                R.id.cv2 -> {
                    startNextActivity("https://demo.sportz.io/sapk01222019186652.json")
                }
            }
        }
    }

    fun startNextActivity(url: String) {
        val intent = Intent(this@MainActivity, MatchDetailsActivity::class.java)
        var bundle = Bundle();
        bundle.putString("URL", url);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}