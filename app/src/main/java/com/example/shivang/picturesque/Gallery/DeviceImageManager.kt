package com.example.shivang.picturesque.Gallery

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import java.util.*


class DeviceImageManager {
    fun getPhoneAlbums(context: Context, listener: OnPhoneImagesObtained) {
        // Creating vectors to hold the final albums objects and albums names
        val phoneAlbums = Vector<PhoneAlbum>()
        val albumsNames = Vector<String>()

        // which image properties are we querying
        val projection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)

        // content: style URI for the "primary" external storage volume
        val images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        // Make the query.
        val cur = context.contentResolver.query(images, projection, null, null, null// Ordering
        )// Which columns to return
        // Which rows to return (all rows)
        // Selection arguments (none)

        if (cur != null && cur.count > 0) {
            Log.i("DeviceImageManager", " query count=" + cur.count)

            if (cur.moveToFirst()) {
                var bucketName: String
                var data: String
                var imageId: String
                val bucketNameColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

                val imageUriColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA)

                val imageIdColumn = cur.getColumnIndex(MediaStore.Images.Media._ID)

                do {
                    // Get the field values
                    bucketName = cur.getString(bucketNameColumn)
                    data = cur.getString(imageUriColumn)
                    imageId = cur.getString(imageIdColumn)

                    // Adding a new PhonePhoto object to phonePhotos vector
                    val phonePhoto = PhonePhoto()
                    phonePhoto.setAlbumName(bucketName)
                    phonePhoto.setPhotoUri(data)
                    phonePhoto.setId(Integer.valueOf(imageId))

                    if (albumsNames.contains(bucketName)) {
                        for (album in phoneAlbums) {
                            if (album.getName() == bucketName) {
                                album.getAlbumPhotos().add(phonePhoto)
                                Log.i("DeviceImageManager", "A photo was added to album => $bucketName")
                                break
                            }
                        }
                    } else {
                        val album = PhoneAlbum()
                        Log.i("DeviceImageManager", "A new album was created => $bucketName")
                        album.setId(phonePhoto.getId())
                        album.setName(bucketName)
                        album.setCoverUri(phonePhoto.getPhotoUri()!!)
                        album.getAlbumPhotos().add(phonePhoto)
                        Log.i("DeviceImageManager", "A photo was added to album => $bucketName")

                        phoneAlbums.add(album)
                        albumsNames.add(bucketName)
                    }

                } while (cur.moveToNext())
            }

            cur.close()
            listener.onComplete(phoneAlbums)
        } else {
            listener.onError()
        }
    }
}