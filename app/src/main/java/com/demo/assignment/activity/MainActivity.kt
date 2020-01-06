
package com.demo.assignment.activity

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.demo.assignment.Const
import com.playablo.school.myapplication.R
import com.demo.assignment.RequestJsonCreator
import com.demo.assignment.adapter.FundsAdapter
import com.demo.assignment.data.FundDetails
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {
    lateinit var mContext:Context
    val client = OkHttpClient()
    var res:String=""
    var list= ArrayList<String>()
    var currentText:String= ""
    var adapter: FundsAdapter?= null
    var pref:SharedPreferences?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(R.layout.activity_main)
        pref = mContext.getSharedPreferences("storage.manager", Activity.MODE_PRIVATE);

        val type =object : TypeToken<ArrayList<String>>(){}.type
        list = Gson().fromJson<ArrayList<String>>(pref!!.getString("listSuggestion","[]"),type)

        if (savedInstanceState != null) {
            currentText = savedInstanceState.getString("search query","")
        }
        search_view.onActionViewExpanded()
        search_view.setQuery(currentText,true)

        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.item_label)
        val cursorAdapter = androidx.cursoradapter.widget.SimpleCursorAdapter(
            mContext,
            R.layout.search_item,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )

        search_view.suggestionsAdapter = cursorAdapter



       search_view.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener,
           SearchView.OnQueryTextListener {
           override fun onQueryTextChange(newText: String?): Boolean {
               val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
               newText.let {
                   list.forEachIndexed { index, suggestion ->
                       if (suggestion.contains(newText!!, true))
                           cursor.addRow(arrayOf(index, suggestion))
                   }
               }

               cursorAdapter.changeCursor(cursor)



               return true
           }

           override fun onQueryTextSubmit(query: String?): Boolean {

               if(query!!.length>=3) {
                   callSearchAPI(query.toString())
                   if(!list.contains(query)) {
                       list.add(query.toString())
                       val editor = pref!!.edit()
                       editor.putString("listSuggestion",Gson().toJson(list))
                       editor.commit()
                   }

               }





               return true
           }

       })
    }

    fun callSearchAPI(keyword: String)
    {
        lifecycleScope.launch{
            val result = searchApi(keyword)
            if (result)
            {
                val jsonData = JSONObject(res)
                val jsonArr = jsonData.getJSONArray("data")
                val listType: Type = object : TypeToken<List<FundDetails?>?>() {}.type
                val dataList = Gson().fromJson<List<FundDetails>>(jsonArr.toString(), listType)
                val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(mContext)
                rv_serchResult!!.layoutManager = mLayoutManager
                adapter = FundsAdapter(
                    mContext,
                    ArrayList(dataList)
                )
                rv_serchResult!!.adapter= adapter
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if(adapter!= null) {
            adapter!!.notifyDataSetChanged()
        }

    }

    private suspend fun searchApi(keyword:String):Boolean
    {
        var success = false
        withContext(Dispatchers.IO)
        {

            val jsonReq = RequestJsonCreator().createSearchJSon(keyword)
            val body= jsonReq.toString().toRequestBody(Const.mediaType)
            val request: Request = Request.Builder()
                .url(Const.url)
                .addHeader("x-api-key",
                    Const.api
                )
                .post(body)
                .build()

            try {
                val response = client.newCall(request).execute()
                success = response.code == 200
                if (success) {
                    res = response.body!!.string()
                    success = true
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
                success = false

            }

        }

        return success

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("search query",search_view.query.toString())
    }
}
