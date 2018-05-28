package com.example.shivang.picturesque.Gallery

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shivang.picturesque.MainActivity
import com.example.shivang.picturesque.R
import java.util.*


class PhotoFragment : Fragment() {
    companion object {
        fun newInstance() = GalleryFragment()
    }

    private lateinit var mAdapter: PhotoAdapter
    private lateinit var mRecyclerView: RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootLayout = inflater.inflate(R.layout.activity_gallery_photos,container,false)
        mRecyclerView = rootLayout.findViewById(R.id.list_photos)
        mRecyclerView.layoutManager = GridLayoutManager(container!!.context,2)
        mAdapter = PhotoAdapter(container!!.context)
        mRecyclerView.adapter = mAdapter
        val intent = Intent(container!!.context, MainActivity::class.java)
//        var bundle : Bundle = intent.extras
        var photoList = intent.extras.getStringArrayList("photos") as List<String>
        mAdapter.setPhotoList(photoList)
        return rootLayout
    }
}