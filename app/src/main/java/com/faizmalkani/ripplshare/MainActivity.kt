package com.faizmalkani.ripplshare

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.*


class MainActivity : AppCompatActivity()
{
    private val shareText = "What do you think of our Paneer Tikka Sandwich?\n" +
            "\n" +
            "\uD83D\uDCC3 Tell us what you think of our iconic sandwich\n" +
            "\uD83C\uDFC6 Win a lead badge\n" +
            "\n" +
            "\uD83C\uDFAB I'm handing over one of my exclusive Indigo invites to you. Download Rippl and participate now: https://link.get-rippl.com/uUSxTuSLpH2saUwu5\n" +
            "\n" +
            "It's all happening on Rippl!"

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById<ImageView>(R.id.capturedImage)


        findViewById<ImageButton>(R.id.captureButton).setOnClickListener { _ ->

            val drawableAsBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.ic_expimage)
            val tempFile = File(cacheDir, "image.png")

            try
            {
                val fileOutStream = FileOutputStream(tempFile)
                drawableAsBitmap.compress(Bitmap.CompressFormat.JPEG,100, fileOutStream)
                fileOutStream.flush()
                fileOutStream.close()
            }
            catch (e: FileNotFoundException)
            {
                e.printStackTrace()
            }
            catch (e: IOException)
            {
                e.printStackTrace()
            }
        }

        findViewById<ImageButton>(R.id.shareButton).setOnClickListener { _ ->
            shareImage()
        }
    }

    private fun shareImage()
    {
        val imageFile = File(cacheDir, "image.png")
        val contentUri: Uri = FileProvider.getUriForFile(this, "com.get-rippl.fileprovider", imageFile)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.setDataAndType(contentUri, this.contentResolver.getType(contentUri))
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(Intent.createChooser(shareIntent, "Share image to:"))

    }

}