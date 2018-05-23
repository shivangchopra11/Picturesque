package com.example.shivang.picturesque


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class CameraFragment : Fragment() {

    companion object {

        /**
         * Id of the camera to access. 0 is the first camera.
         */
        private const val CAMERA_ID = 0

        fun newInstance() = CameraFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView =  inflater?.inflate(R.layout.activity_camera, container, false)
//        var clickPic : Button = rootView!!.findViewById(R.id.btn_click_pic)
        return rootView
    }


}