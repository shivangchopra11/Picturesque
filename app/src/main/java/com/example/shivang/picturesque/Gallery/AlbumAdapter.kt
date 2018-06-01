package com.example.shivang.picturesque.Gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.shivang.picturesque.MainActivity
import com.example.shivang.picturesque.R
import com.squareup.picasso.Picasso


class AlbumAdapter(private val mContext: Context, val galleryFragment: GalleryFragment,val activity : Activity) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    private var mAlbumList: ArrayList<PhoneAlbum>? = null
    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    lateinit var view : View

    init {
        this.mAlbumList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        view = mInflater.inflate(R.layout.gallery_list_card, parent, false)
        return AlbumViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mAlbumList?.size ?: 0
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = mAlbumList!![position]
        Picasso.with( mContext )
                .load( "file:" + album.getCoverUri() )
                .centerCrop()
                .fit()
                .placeholder( R.color.colorPrimary )
                .into( holder.imageView )
        holder.tvName.text = album.getName()
        holder.itemView.setOnClickListener{
//            var intent = Intent(mContext, MainActivity::class.java)
            var photoList : ArrayList<String> = object : ArrayList<String>(){}
            for(i : PhonePhoto in album.getAlbumPhotos()) {
                photoList.add(i.getPhotoUri()!!)
//                Log.v("photo",i.getPhotoUri())
            }
            var bundle = Bundle()
            bundle.putStringArrayList("photos",photoList)
            activity.intent.putExtras(bundle)
            activity.intent.putExtra("status",1)
//            var act1 = activity as MainActivity
//            act1.startPhotoFragment()

            galleryFragment.startPhotoFragment()

        }
    }



    fun setAlbumList(movieList: List<PhoneAlbum>) {
        this.mAlbumList!!.clear()
        this.mAlbumList!!.addAll(movieList)
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged()
    }


    inner class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.img) as ImageView
        var tvName: TextView = itemView.findViewById(R.id.title) as TextView
        val pos = layoutPosition

    }
}