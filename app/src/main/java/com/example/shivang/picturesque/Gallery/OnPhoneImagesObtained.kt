package com.example.shivang.picturesque.Gallery

import java.util.*

interface OnPhoneImagesObtained {
    fun onComplete(albums: Vector<PhoneAlbum>)
    fun onError()
}