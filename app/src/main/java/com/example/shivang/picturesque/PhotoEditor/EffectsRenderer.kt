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
        GLES20.glViewport(0,0,width, height)
        GLES20.glClearColor(0f,0f,0f,1f)
        generateSquare()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
    }

    private val textures = IntArray(2)
    private var square: Square? = null

    private fun generateSquare() {
        GLES20.glGenTextures(2, textures, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, photo, 0)
        square = Square()
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
}