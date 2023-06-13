package com.example.news

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerview.adapter = mAdapter

    }

    private fun fetchData(){
        val url="https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=38ef1c0bbc264a2ab06a73dccd653ae8"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener{
                 val newsJsonArray = it.getJSONArray("articles")
                 val newsArray = ArrayList<News>()
                 for(i in 0 until newsJsonArray.length())
                 {
                     val newsJsonObject=newsJsonArray.getJSONObject(i)
                     val news = News(
                         newsJsonObject.getString("title"),
                         newsJsonObject.getString("author"),
                         newsJsonObject.getString("url"),
                         newsJsonObject.getString("urlToImage")
                     )
                     newsArray.add(news)
                 }
                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener{
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
            })

        // Add the request to the RequestQueue
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

}
    override fun onItemClicked(item: News) {
        // Toast.makeText(this,"clicked item is $item",Toast.LENGTH_LONG).show()
        val builder= CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url));
    }


}