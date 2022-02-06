package com.subrotokumar.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.subrotokumar.memeshare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var memeurl:String?=null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        loadMeme()
        loadMeme()
    }

    private fun loadMeme()
    {
        binding.progressBar.visibility=View.VISIBLE

        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET, "https://meme-api.herokuapp.com/gimme",null,
            Response.Listener{ response ->
                memeurl=response.getString("url")
                Glide.with(this).load(memeurl).listener(object : RequestListener<Drawable>
                {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean
                    {
                        binding.progressBar.visibility=View.GONE
                        return false
                    }
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean
                    {
                        binding.progressBar.visibility=View.GONE
                        return false
                    }
                }).into(binding.ivMeme)
            },
            Response.ErrorListener { Toast.makeText(this,"Something went wrong!", Toast.LENGTH_SHORT).show() })
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun shareMeme(view: View){
        val intent= Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"$memeurl")
        val chooser=Intent.createChooser(intent,"Share this meme using ...")
        startActivity(chooser)
    }
    fun nextMeme(view: View){
        loadMeme()
    }

}