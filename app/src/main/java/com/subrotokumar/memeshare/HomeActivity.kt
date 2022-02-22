package com.subrotokumar.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.subrotokumar.memeshare.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    var memeurl: String? = null

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        loadMeme()
        loadMeme()

        binding.btnNext.setOnClickListener { nextMeme(); }
        binding.btnShare.setOnClickListener { shareMeme() }
    }

    private fun loadMeme() {
        binding.progressBar.visibility = View.VISIBLE

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, "https://meme-api.herokuapp.com/gimme", null,
            { response ->
                memeurl = response.getString("url")
                Glide.with(this).load(memeurl).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }
                }).into(binding.ivMeme)
            },
            {
                Toast.makeText(
                    this,
                    "Something went wrong!",
                    Toast.LENGTH_SHORT
                ).show()
            })
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun shareMeme() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, check out this meme $memeurl .")
        val chooser = Intent.createChooser(intent, "Share this meme using ...")
        startActivity(chooser)
    }

    private fun nextMeme() {
        loadMeme()
    }

}