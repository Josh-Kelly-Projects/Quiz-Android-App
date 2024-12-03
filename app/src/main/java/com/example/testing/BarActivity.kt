package com.example.testing

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.testing.database.DatabaseHelper
import com.example.testing.session.PreferencesHelper

class BarActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bar_menu)

        dbHelper = DatabaseHelper(this)
        val preferencesHelper = PreferencesHelper(applicationContext)

        val item = dbHelper.fetchMenuItems()

        //Bar items
        val containerItemsFood = findViewById<LinearLayout>(R.id.linearLayoutInsideFood)
        val containerItemsDrinks = findViewById<LinearLayout>(R.id.linearLayoutInsideDrinks)

        for (items in item) {
            if (items.itemType == "food") {
                val itemListLayout =
                    LayoutInflater.from(this).inflate(R.layout.bar_list, containerItemsFood, false)

                val textViewItemName = itemListLayout.findViewById<TextView>(R.id.textViewItemName)
                textViewItemName.text = items.itemName

                val textViewItemIngredients =
                    itemListLayout.findViewById<TextView>(R.id.textViewIngred)
                textViewItemIngredients.text = items.itemDetail

                val textViewItemPrice = itemListLayout.findViewById<TextView>(R.id.textViewPrice)
                textViewItemPrice.text = "R" + items.itemPrice.toString()

                val imageUri = Uri.parse("android.resource://com.example.testing/drawable/"+items.itemPicture)
                val textViewItemPic = itemListLayout.findViewById<ImageView>(R.id.menu_item_image)
                textViewItemPic.setImageURI(imageUri)

                containerItemsFood.addView(itemListLayout)

            } else if (items.itemType == "drink"){
                val itemListLayout =
                    LayoutInflater.from(this).inflate(R.layout.bar_list, containerItemsDrinks, false)

                val textViewItemName = itemListLayout.findViewById<TextView>(R.id.textViewItemName)
                textViewItemName.text = items.itemName

                val textViewItemIngredients =
                    itemListLayout.findViewById<TextView>(R.id.textViewIngred)
                textViewItemIngredients.text = items.itemDetail

                val textViewItemPrice = itemListLayout.findViewById<TextView>(R.id.textViewPrice)
                textViewItemPrice.text = "R " + items.itemPrice.toString()

                val imageUri = Uri.parse("android.resource://com.example.testing/drawable/"+items.itemPicture)
                val textViewItemPic = itemListLayout.findViewById<ImageView>(R.id.menu_item_image)
                textViewItemPic.setImageURI(imageUri)

                containerItemsDrinks.addView(itemListLayout)
            }
        }



        val profileSetting = findViewById<ImageButton>(R.id.profileButton)
        profileSetting.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val home = findViewById<ImageButton>(R.id.homeButton)
        home.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }

        val notification = findViewById<ImageButton>(R.id.notificationButton)
        notification.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        val menu = findViewById<ImageButton>(R.id.barButton)
        menu.setOnClickListener {
            val intent = Intent(this, BarActivity::class.java)
            startActivity(intent)
        }
    }
}
