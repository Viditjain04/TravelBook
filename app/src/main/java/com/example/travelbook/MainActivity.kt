package com.example.travelbook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import java.lang.Exception
class MainActivity : AppCompatActivity() {

    var placesArray = ArrayList<Place>()


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.map) {

            val intent = Intent(applicationContext,
                MapsActivity::class.java)
            intent.putExtra("info","new")
            startActivity(intent)

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {

            val database = openOrCreateDatabase("Places", Context.MODE_PRIVATE,null)
            val cursor = database.rawQuery("SELECT * FROM places",null)

            val addressIndex = cursor.getColumnIndex("address")
            val latitudeIndex = cursor.getColumnIndex("latitude")
            val longitudeIndex = cursor.getColumnIndex("longitude")

            while(cursor.moveToNext()) {

                val addressFromDatabase = cursor.getString(addressIndex)
                val latitudeFromDatabase = cursor.getDouble(latitudeIndex)
                val longitudeFromDatabase = cursor.getDouble(longitudeIndex)

                val myPlace = Place(
                    addressFromDatabase,
                    latitudeFromDatabase,
                    longitudeFromDatabase
                )

                placesArray.add(myPlace)

            }

            cursor.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val customAdapter =
            CustomAdapter(placesArray, this)
       findViewById<ListView>( R.id.listView).adapter = customAdapter

        findViewById<ListView>( R.id.listView).setOnItemClickListener{parent, view, position, id ->
            val intent = Intent(this@MainActivity,
                MapsActivity::class.java)
            intent.putExtra("info","old")
            intent.putExtra("selectedPlace",placesArray.get(position))
            startActivity(intent)
        }

    }
}