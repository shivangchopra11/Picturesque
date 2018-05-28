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


class AlbumAdapter(private val mContext: Context, var id1:Int) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    private var mAlbumList: ArrayList<PhoneAlbum>? = null
    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    lateinit var view : View

    init {
        this.mAlbumList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        view = if(id1==2)
            mInflater.inflate(R.layout.gallery_list_card, parent, false)
        else
            mInflater.inflate(R.layout.gallery_list_card, parent, false)
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