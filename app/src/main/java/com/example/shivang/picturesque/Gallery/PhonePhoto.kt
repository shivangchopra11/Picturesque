package com.example.shivang.picturesque.Gallery

class PhonePhoto {
    private var id: Int = 0
    private var albumName: String? = null
    private var photoUri: String? = null

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getAlbumName(): String? {
        return albumName
    }

    fun setAlbumName(name: String) {
        this.albumName = name
    }

    fun getPhotoUri(): String? {
        return photoUri
    }

    fun setPhotoUri(photoUri: String) {
        this.photoUri = photoUri
    }
}