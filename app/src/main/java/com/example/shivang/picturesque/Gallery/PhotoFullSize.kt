package com.example.shivang.picturesque.Gallery

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.widget.ImageView
import com.example.shivang.picturesque.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_photo_full_size.view.*
import java.util.*

class PhotoFullSize : AppCompatActivity() {

    lateinit var mPager: ViewPager
    var currentPage : Int = 0
    var numPages : Int = 0
    lateinit var imageList: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_full_size)



//        var photoImageView : ImageView = findViewById(R.id.photo_img_view)
//        var curUri = intent.getStringExtra("photo")
//        Picasso.with( this )
//                .load("file:$curUri")
//                .centerCrop()
//                .fit()
//                .placeholder( R.color.colorPrimary )
//                .into(photoImageView)


        var bundle : Bundle = this.intent.extras
        imageList = bundle.getStringArrayList("photoList") as ArrayList<String>
        currentPage = intent.getIntExtra("pos",0)
        mPager = findViewById(R.id.photo_img_view_container)
        mPager.adapter = SlidingImageAdapter(this,imageList)
        mPager.setCurrentItem(currentPage, true)
        numPages = imageList.size

        
//        For auto slideshow functionality


//        val handler = Handler()
//        val Update = Runnable {
//            if (currentPage === numPages) {
//                currentPage = 0
//            }
//            mPager.setCurrentItem(currentPage++, true)
//        }
//        val swipeTimer = Timer()
//        swipeTimer.schedule(object : TimerTask() {
//            override fun run() {
//                handler.post(Update)
//            }
//        }, 3000, 3000)


    }
}
