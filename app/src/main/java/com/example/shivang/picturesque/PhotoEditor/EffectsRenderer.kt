package com.example.shivang.picturesque.PhotoEditor

import android.content.Context
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log
import android.media.effect.Effect
import android.media.effect.EffectContext
import android.media.effect.EffectFactory








class EffectsRenderer() : GLSurfaceView.Renderer {

    private var photo: Bitmap? = null
    private var photoWidth: Int = 0
    private var photoHeight: Int = 0
    private var effectContext: EffectContext? = null
    private var effect: Effect? = null


    constructor(context: Context,curUri : String) : this() {
        Log.v("PIC",curUri)
        photo = MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse("file:$curUri"))
        photoWidth = photo!!.width
        photoHeight = photo!!.height
    }

    override fun onDrawFrame(gl: GL10?) {
        if(effectContext==null) {
            effectContext = EffectContext.createWithCurrentGlContext()
        }
        if(effect!=null){
            effect!!.release()
        }
        grayScaleEffect()
        square!!.draw(textures[1])
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        mViewHeight = height
        mViewWidth = width
        photo = getResizedBitmap(photo!!,mViewWidth,mViewHeight)
        photoWidth = photo!!.width
        photoHeight = photo!!.height
        GLES20.glViewport(0,0,width, height)
        GLES20.glClearColor(0f,0f,0f,1f)
        var coords = computeOutputVertices()
        generateSquare(coords)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        var coords = computeOutputVertices()
        generateSquare(coords)
    }

    private val textures = IntArray(2)
    private var square: Square? = null

    private fun generateSquare(arr : FloatArray) {
        GLES20.glGenTextures(2, textures, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, photo, 0)
        Log.v("resize",""+arr[0]+arr[1]+arr[2]+arr[3]+arr[4]+arr[5])
        square = Square(arr)
    }

    private fun grayScaleEffect() {
        val factory = effectContext!!.factory
        effect = factory.createEffect(EffectFactory.EFFECT_GRAYSCALE)
        effect!!.apply(textures[0], photoWidth, photoHeight, textures[1])
    }

    private fun documentaryEffect() {
        val factory = effectContext!!.factory
        effect = factory.createEffect(EffectFactory.EFFECT_DOCUMENTARY)
        effect!!.apply(textures[0], photoWidth, photoHeight, textures[1])
    }

    private fun brightnessEffect() {
        val factory = effectContext!!.factory
        effect = factory.createEffect(EffectFactory.EFFECT_BRIGHTNESS)
        effect!!.setParameter("brightness", 2f)
        effect!!.apply(textures[0], photoWidth, photoHeight, textures[1])
    }


    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0

    private fun computeOutputVertices() : FloatArray {
//        if (mPosVertices != null) {
            var photoWidth1 : Float = photoWidth * 1.0f
            var photoHeight1 : Float = photoHeight * 1.0f
            val imgAspectRatio = photoWidth1 / photoHeight1
            var mViewWidth1 : Float = mViewWidth * 1.0f
            var mViewHeight1 : Float = mViewHeight * 1.0f
            val viewAspectRatio = mViewWidth1/ mViewHeight1
            val relativeAspectRatio = viewAspectRatio / imgAspectRatio
            val x0: Float
            val y0: Float
            val x1: Float
            val y1: Float
            if (relativeAspectRatio > 1.0f) {
                x0 = -1.0f / relativeAspectRatio
                y0 = -1.0f
                x1 = 1.0f / relativeAspectRatio
                y1 = 1.0f
            } else {
                x0 = -1.0f
                y0 = -relativeAspectRatio
                x1 = 1.0f
                y1 = relativeAspectRatio
            }
        //            mPosVertices.put(coords).position(0)
            return floatArrayOf(x0, y0, x1, y0, x0, y1, x1, y1)
            Log.v("resize","$x0$y0$x1$y0$x0$y1$x1$y1")
//        }
    }

    fun getResizedBitmap(image: Bitmap, bitmapWidth: Int, bitmapHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true)
    }
}