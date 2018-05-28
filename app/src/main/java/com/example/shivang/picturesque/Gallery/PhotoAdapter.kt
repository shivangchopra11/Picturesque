package com.example.shivang.picturesque.Gallery

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.shivang.picturesque.R
import com.squareup.picasso.Picasso

class PhotoAdapter(private val mContext: Context) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private var mPhotoList: ArrayList<String>? = null
    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    lateinit var view : View

    init {
        this.mPhotoList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        view = mInflater.inflate(R.layout.photos_list_card, parent, false)
        return PhotoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPhotoList?.size ?: 0
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = mPhotoList!![position]
        Picasso.with( mContext )
                .load( "file:" + photo )
                .centerCrop()
                .fit()
                .placeholder( R.color.colorPrimary )
                .into( holder.imageView )
    }

    fun setPhotoList(movieList: List<String>) {
        this.mPhotoList!!.clear()
        this.mPhotoList!!.addAll(movieList)
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged()
    }


    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.img) as ImageView
        val pos = layoutPosition

    }
}