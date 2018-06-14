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
import java.nio.IntBuffer
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth




class EffectsRenderer() : GLSurfaceView.Renderer {

    private var photo: Bitmap? = null
    private var photoWidth: Int = 0
    private var photoHeight: Int = 0
    private var effectContext: EffectContext? = null
    private var effect: Effect? = null
    var bitmap: Bitmap? = null
    private var mFactor : Float = 0.0f


    private var mIndex: Int? = null
    fun setCurEffect(pos : Int) {
        mIndex = pos
    }

    fun setFactor(factor : Float) {
        mFactor = factor
    }

    constructor(context: Context,curUri : String) : this() {
        Log.v("PIC",curUri)
        photo = MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(curUri))
        photoWidth = photo!!.width
        photoHeight = photo!!.height
        mIndex = 0
    }



    override fun onDrawFrame(gl: GL10?) {
        if(effectContext==null) {
            effectContext = EffectContext.createWithCurrentGlContext()
        }
        if(effect!=null){
            effect!!.release()
        }
        Log.v("Index",mIndex.toString())
        when(mIndex) {
            0 -> blackAndWhiteEffect(mFactor)
            1 -> documentaryEffect()
            2 -> duotoneEffect()
            3 -> grainEffect(mFactor)
            4 -> grayScaleEffect()
            5 -> negativeEffect()
            6 -> posterizeEffect()
            7 -> sepiaEffect()
            8 -> vignetteEffect(mFactor)
        }
//        grayScaleEffect()
        square!!.draw(textures[1])

//        bitmap = takeScreenshot(gl!!)


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
//        bitmap = takeScreenshot(gl!!)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        var coords = computeOutputVertices()
        generateSquare(coords)
//        bitmap = takeScreenshot(gl!!)
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



    private fun blackAndWhiteEffect(factor : Float = 0.5f) {
        val factory = effectContext!!.factory
        effect = factory.createEffect(EffectFactory.EFFECT_BLACKWHITE)
        effect!!.setParameter("black", factor)
        effect!!.setParameter("white", factor)
        effect!!.apply(textures[0], photoWidth, photoHeight, textures[1])
    }

    private fun documentaryEffect() {
        val factory = effectContext!!.factory
        effect = factory.createEffect(EffectFactory.EFFECT_DOCUMENTARY)
        effect!!.apply(textures[0], photoWidth, photoHeight, textures[1])
    }

    private fun duotoneEffect() {
        val factory = effectContext!!.factory
        effect = factory.createEffect(EffectFactory.EFFECT_DUOTONE)
        effect!!.apply(textures[0], photoWidth, photoHeight, textures[1])
    }

    private fun grainEffect(factor : Float = 0.5f) {
        val factory = effectContext!!.factory
        effect = factory.createEffect(EffectFactory.EFFECT_GRAIN)
        effect!!.setParameter("strength", factor)
        effect!!.apply(textures[0], photoWidth, photoHeight, textures[1])
    }

    private fun grayScaleEffect() {
        val factory = effectContext!!.factory
        effect = factory.createEffect(EffectFactory.EFFECT_GRAYSCALE)
        effect!!.apply(textures[0], photoWidth, photoHeight, textures[1])
    }

    private fun negativeEffect() {
        val factory = effectContext!!.factory
        effect = factory.createEffect(EffectFactory.EFFECT_NEGATIVE)
        effect!!.apply(textures[0], photoWidth, photoHeight, textures[1])
    }

    private fun posterizeEffect() {
        val factory = effectContext!!.factory
        effect = factory.createEffect(EffectFactory.EFFECT_POSTERIZE)
        effect!!.apply(textures[0], photoWidth, photoHeight, textures[1])
    }

    private fun sepiaEffect() {
        val factory = effectContext!!.factory
        effect = factory.createEffect(EffectFactory.EFFECT_SEPIA)
        effect!!.apply(textures[0], photoWidth, photoHeight, textures[1])
    }

    private fun vignetteEffect(factor : Float = 0.5f) {
        val factory = effectContext!!.factory
        effect = factory.createEffect(EffectFactory.EFFECT_VIGNETTE)
//        Log.v("CURFAC",factor.toString())
        effect!!.setParameter("scale", factor)
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

//    fun takeScreenshot(mGL: GL10): Bitmap {
//        val mWidth = photoWidth
//        val mHeight = photoHeight
//        val ib = IntBuffer.allocate(mWidth * mHeight)
//        val ibt = IntBuffer.allocate(mWidth * mHeight)
//
//
//        mGL.glReadPixels(0, 0, mWidth, mHeight, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib)
//
//        // Convert upside down mirror-reversed image to right-side up normal
//        // image.
//        for (i in 0 until mHeight) {
//            for (j in 0 until mWidth) {
//                ibt.put((mHeight - i - 1) * mWidth + j, ib.get(i * mWidth + j))
//            }
//        }
//
//        val mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
//        mBitmap.copyPixelsFromBuffer(ibt)
//        return mBitmap
//    }

    fun getResizedBitmap(image: Bitmap, bitmapWidth: Int, bitmapHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true)
    }
}