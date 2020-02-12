package com.example.egy

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.widget.Toast
import com.example.egy.retrofil.CatFactsAPI1
import com.example.egy.retrofil.CatFactsResult
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import com.google.gson.Gson as Gson1
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {


    private val URL_BASE = "https://cat-fact.herokuapp.com/facts/random"
    private val myFav: MutableList<String> = mutableListOf()
    private val datakey="hhTry"


   /* private val brWeatherReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val rawResult = intent.getStringExtra(
                HttpGetTask.KEY_RESULT
            )

            try {
                val rawJson = JSONObject(rawResult)
                val factText = rawJson.getString("text")
                text2.text = factText.toString()

            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofil()
        time()

       newfactbutton.setOnClickListener(){
           retrofil();
       }
        //works the add
        likebutton.setOnClickListener(){
            myFav.add(text2.text.toString())
            saveArrayList(ArrayList(myFav),datakey)

        }

       favoritesbutton.setOnClickListener(){
          val loadedList=getArrayList(datakey)
           if(loadedList != null)
               //text1.text=loadedList.size.toString()
           else
           Toast.makeText(applicationContext, "You do not have favorites yet", Toast.LENGTH_LONG).show()

       }
    }

    fun retrofil(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val currencyAPI = retrofit.create(CatFactsAPI1::class.java)

        val ratesCall = currencyAPI.getFacts("text")
        ratesCall.enqueue(object: Callback<CatFactsResult> {
            override fun onFailure(call: Call<CatFactsResult>, t: Throwable) {
                text2.text = t.message
            }

            override fun onResponse(call: Call<CatFactsResult>, response: Response<CatFactsResult>) {
                text2.text = response.body()?.text.toString()
            }
        })

    }

    fun saveArrayList(list: ArrayList<String>, key: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()     // This line is IMPORTANT !!!
    }

    fun getArrayList(key: String): ArrayList<String>? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val gson = Gson()
        val json = prefs.getString(key, null)
        val type = object : TypeToken<ArrayList<String>>() {

        }.type
        return gson.fromJson(json, type)
    }

    fun time(){
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH, 1)
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)


        val millis = c.getTimeInMillis() - System.currentTimeMillis()

        val reaming= String.format("%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))

        time.text= reaming.toString()
        Handler().postDelayed({
            // println("12")
            handlerHelper()
        }, 500)



        if(millis <1000){
            retrofil()
        }

       // println(reaming)
    }

    fun handlerHelper(){
        Handler().postDelayed({
           // println("12")
            time()
        }, 500)
    }
}
