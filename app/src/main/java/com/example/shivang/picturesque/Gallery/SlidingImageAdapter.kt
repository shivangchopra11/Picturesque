package com.example.shivang.picturesque.Gallery

import android.app.Activity
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.shivang.picturesque.MainActivity
import com.example.shivang.picturesque.R
import com.squareup.picasso.Picasso

class SlidingImageAdapter(val context:Context, private val images: ArrayList<String>,val activity: Activity) : PagerAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.fragment_detail_image, view, false)!!

        val imageView = imageLayout.findViewById(R.id.photo_img_view) as ImageView


        Picasso.with( context )
                .load("file:${images[position]}")
                .centerCrop()
                .fit()
                .placeholder( R.color.colorPrimary )
                .into(imageView)

        (activity as PhotoFullSize).setActionBarTitle(position)

        view.addView(imageLayout, 0)

        return imageLayout
    }
}