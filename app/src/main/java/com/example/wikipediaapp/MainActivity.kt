package com.example.wikipediaapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URLEncoder
import android.widget.ImageView


class MainActivity : AppCompatActivity() {
    private var editTextSearch: EditText? = null
    private var buttonSearch: Button? = null
    private var textViewResult: TextView? = null
    private var progressBar: ProgressBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextSearch = findViewById(R.id.editTextSearch)
        buttonSearch = findViewById(R.id.buttonSearch)
        progressBar = findViewById(R.id.progressBar)

        Toast.makeText(this, "Welcome to Wikipedia App", Toast.LENGTH_SHORT).show()


        buttonSearch?.setOnClickListener(View.OnClickListener {
            val searchText = editTextSearch?.text.toString()

            if (searchText.isEmpty()) {
                Toast.makeText(this, "Please input the text", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            progressBar?.visibility = View.VISIBLE


            GlobalScope.launch {
                try {
                    val result = fetchData(URLEncoder.encode(searchText, "UTF-8"))
                    withContext(Dispatchers.Main) {
                        progressBar?.visibility = View.GONE
                        startResultActivity(result)
                       textViewResult?.text = result
                    }
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        progressBar?.visibility = View.GONE
                        Toast.makeText(
                            this@MainActivity,
                            "Error fetching data: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
        })

    }

    private fun startResultActivity(result: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("result", result)
           startActivity(intent)

    }

    private suspend fun fetchData(searchText: String): String {
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect("https://simple.wikipedia.org/w/index.php?search=${URLEncoder.encode(searchText, "UTF-8")}").get()
        }
        return doc.text()
    }
}
