package com.example.shivang.picturesque.Gallery

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import com.example.shivang.picturesque.PhotoEditor.PhotoEditor
import com.example.shivang.picturesque.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_photo_full_size.view.*
import java.util.*
import kotlin.math.sign

class PhotoFullSize : AppCompatActivity(),View.OnClickListener {


    lateinit var mPager: ViewPager
    var currentPage : Int = 0
    var numPages : Int = 0
    lateinit var imageList: ArrayList<String>
     lateinit var editBtn : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_full_size)
        editBtn = findViewById(R.id.btn_edit)
        editBtn.setOnClickListener(this)


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
        mPager.adapter = SlidingImageAdapter(this,imageList,this)
        mPager.setCurrentItem(currentPage, true)
        numPages = imageList.size
        setActionBarTitle(currentPage)

        
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


    override fun onClick(v: View?) {
        if(v!!.id==R.id.btn_edit) {
            var intent = Intent(this,PhotoEditor::class.java)
            intent.putExtra("curPic","file:"+imageList[mPager.currentItem])
//            Log.v("PIC",imageList[currentPage])
            startActivity(intent)
        }

    }

    fun setActionBarTitle(cur: Int) {
        supportActionBar!!.title = cur.toString() + " of " + numPages.toString()
    }
}
