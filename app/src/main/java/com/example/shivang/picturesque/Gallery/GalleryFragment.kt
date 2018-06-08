package com.example.shivang.picturesque.Gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shivang.picturesque.MainActivity
import com.example.shivang.picturesque.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.support.v7.widget.DividerItemDecoration



class GalleryFragment : Fragment(),OnPhoneImagesObtained {

    lateinit var mContext : Context
    companion object {
        fun newInstance() = GalleryFragment()
    }

    private lateinit var mAdapter: AlbumAdapter
    private lateinit var mRecyclerView: RecyclerView
    lateinit var title : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = container!!.context
        var rootLayout = inflater.inflate(R.layout.activity_gallery_albums,container,false)
        mRecyclerView = rootLayout.findViewById(R.id.list_albums)

        mRecyclerView.addItemDecoration(SpacesItemDecoration(4))
        mRecyclerView.layoutManager = GridLayoutManager(container!!.context,2)
        mAdapter = AlbumAdapter(container!!.context,this,activity as Activity)
        mRecyclerView.adapter = mAdapter
        var deviceImageManager = DeviceImageManager()
        deviceImageManager.getPhoneAlbums(container!!.context,this)
        return rootLayout
    }

    override fun onComplete(albums: Vector<PhoneAlbum>) {
        mAdapter.setAlbumList(albums)
    }

    override fun onError() {
        Log.v("GALLERY","Couldn't get albums")
    }



    fun startPhotoFragment() {
        var mAdapter = PhotoAdapter(mContext,activity as Activity)
        mRecyclerView.adapter = mAdapter
//        val intent = Intent(mContext, MainActivity::class.java)
        var bundle : Bundle = activity!!.intent.extras
        title = activity!!.intent.getStringExtra("album")
        var photoList = bundle.getStringArrayList("photos") as List<String>
        mAdapter.setPhotoList(photoList)
        (activity as MainActivity).setActionBarTitle(title)
    }



}