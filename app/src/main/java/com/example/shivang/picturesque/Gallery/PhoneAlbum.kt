package com.example.shivang.picturesque.Gallery

import java.util.*

class PhoneAlbum {
    private var id: Int = 0
    private var name: String? = null
    private var coverUri: String? = null
    private var albumPhotos: Vector<PhonePhoto>? = null

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getCoverUri(): String? {
        return coverUri
    }

    fun setCoverUri(albumCoverUri: String) {
        this.coverUri = albumCoverUri
    }

    fun getAlbumPhotos(): Vector<PhonePhoto> {
        if (albumPhotos == null) {
            albumPhotos = object : Vector<PhonePhoto>(){}
        }
        return albumPhotos!!
    }

    fun setAlbumPhotos(albumPhotos: Vector<PhonePhoto>) {
        this.albumPhotos = albumPhotos
    }
}