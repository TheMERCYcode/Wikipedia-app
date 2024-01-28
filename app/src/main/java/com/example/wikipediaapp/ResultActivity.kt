package com.example.wikipediaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import org.jsoup.Jsoup
import com.squareup.picasso.Picasso
import org.jsoup.nodes.Document


class ResultActivity : AppCompatActivity() {
    //private lateinit var textViewResult: TextView
    private var textViewResult: TextView? = null
    private var imageViewResult: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result2)
        textViewResult = findViewById(R.id.textViewResult)
        imageViewResult = findViewById(R.id.imageViewResult)

        val result = intent.getStringExtra("result")
        displayResult(result)
    }

    private fun displayResult(result: String?) {
        if (result.isNullOrEmpty()) {
            textViewResult?.text = "No result available."
            return
        }

        val doc = Jsoup.parse(result)
        val imageUrl = extractImageUrl(doc)

        if (!imageUrl.isNullOrEmpty()) {
            // Load and display the image using Picasso or any other image loading library
            Picasso.get().load(imageUrl).into(imageViewResult)
        }

        // Display the text content in the textViewResult
        textViewResult?.text = doc.text()
    }

    private fun extractImageUrl(doc: Document): String? {
        // You need to inspect the HTML structure of the Wikipedia page
        // and find the appropriate CSS selector or XPath expression to locate the image element.
        // This example assumes the image is within a div with class 'thumbinner'.
        val imageElement = doc.select("div.thumbinner img").first()
        return imageElement?.absUrl("src")
    }
}