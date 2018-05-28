package com.example.shivang.picturesque.Gallery

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shivang.picturesque.R
import java.util.*

class GalleryFragment : Fragment(),OnPhoneImagesObtained {

    companion object {
        fun newInstance() = GalleryFragment()
    }

    private lateinit var mAdapter: AlbumAdapter
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootLayout = inflater.inflate(R.layout.activity_gallery_albums,container,false)
        mRecyclerView = rootLayout.findViewById(R.id.list_albums)
        mRecyclerView.layoutManager = GridLayoutManager(container!!.context,2)
        mAdapter = AlbumAdapter(container!!.context,1)
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
}