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
    // The sharing text
    private val shareText = expTitle +
            "\n" +
            "\uD83D\uDCC3 + "shortDescription" +
            "\uD83C\uDFC6 + "rewardShortDescription" +
            "\n" +
            "\uD83C\uDFAB I'm handing over one of my exclusive" + brandName + "invites to you. Download Rippl and participate now: " + expLink +
            "\n" +
            "It's all happening on Rippl!"

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById<ImageView>(R.id.capturedImage)


        findViewById<ImageButton>(R.id.captureButton).setOnClickListener { _ ->

            // Convert image from API into Bitmap
            val drawableAsBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.ic_expimage)

            // Create a file in the cache dir, with a fixed image name
            /**
             * @param parent The cache directory for the app
             * @param child The filename for storing the bitmap
             */
            val tempFile = File(cacheDir, "image.png")

            // Write the bitmap to the newly created file
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

        // Call the share function to execute
        findViewById<ImageButton>(R.id.shareButton).setOnClickListener { _ ->
            shareImage()
        }
    }

    private fun shareImage()
    {
        // Read the image back from the cache, with the same dir and filename as when written
        val imageFile = File(cacheDir, "image.png")

        /**
         * @param context The app's context
         * @param authority Same as the authority defined in the manifest
         * @param file The image file just written
         */
        val contentUri: Uri = FileProvider.getUriForFile(this, "com.get-rippl.fileprovider", imageFile)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND // Intent action
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Important for Android 10+
        shareIntent.setDataAndType(contentUri, this.contentResolver.getType(contentUri))
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(Intent.createChooser(shareIntent, "Share image to:"))

    }

}