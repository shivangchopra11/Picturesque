package com.example.shivang.picturesque.Camera


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.support.v4.app.Fragment
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.*
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.ImageButton
import com.example.shivang.picturesque.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*







class CameraFragment : Fragment(), View.OnClickListener {


    companion object {
        fun newInstance() = CameraFragment()
        lateinit var bitmap: Bitmap
        var galleryFolder : File? = null
    }
    private lateinit var cameraManager : CameraManager
    private var cameraFacing : Int = 0
    private lateinit var cameraId : String
    var curCameraDevice: CameraDevice? = null
    private lateinit var stateCallback: CameraDevice.StateCallback
    private lateinit var surfaceTextureListener: TextureView.SurfaceTextureListener
    var curCameraCaptureSession : CameraCaptureSession? = null
    var captureRequestBuilder: CaptureRequest.Builder? = null
    private var previewSize: Size? = null
    private var backgroundThread: HandlerThread? = null
    var backgroundHandler: Handler? = null
    private lateinit var textureView : TextureView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView =  inflater.inflate(R.layout.activity_camera, container, false)

        var clickBtn : ImageButton = rootView.findViewById(R.id.btn_click_pic)
        clickBtn.setOnClickListener(this)
        var revCamBtn : ImageButton = rootView.findViewById(R.id.btn_rev_cam)
        revCamBtn.setOnClickListener(this)

        cameraManager = activity!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraFacing = CameraCharacteristics.LENS_FACING_BACK
        textureView = rootView.findViewById(R.id.imgv_photo) as TextureView
        surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
                setUpCamera()
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, width: Int, height: Int) {

            }

            override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {

            }
        }

        stateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(cameraDevice: CameraDevice) {
                curCameraDevice = cameraDevice
                createPreviewSession()
            }

            override fun onDisconnected(cameraDevice: CameraDevice) {
                cameraDevice.close()
                curCameraDevice = null
            }

            override fun onError(cameraDevice: CameraDevice, error: Int) {
                cameraDevice.close()
                curCameraDevice = null
            }
        }
//        var clickPic : Button = rootView!!.findViewById(R.id.btn_click_pic)
        return rootView
    }


//    fun checkCameraHardware(context : Context) : Boolean {
//        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
//    }



    private fun setUpCamera() {
//        try {
            for (cameraId in cameraManager.cameraIdList) {
                val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == cameraFacing) {
                    val streamConfigurationMap = cameraCharacteristics.get(
                            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    previewSize = streamConfigurationMap!!.getOutputSizes(SurfaceTexture::class.java)[0]
                    this.cameraId = cameraId
                }
            }
//        } catch (e: CameraAccessException) {
//            e.printStackTrace()
//        }
        createImageGallery()
    }


    private fun openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(activity!!.baseContext , android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraManager.openCamera(cameraId, stateCallback, backgroundHandler)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }




    private fun openBackgroundThread() {
        backgroundThread = HandlerThread("camera_background_thread")
        backgroundThread!!.start()
        backgroundHandler = Handler(backgroundThread!!.looper)
    }

    override fun onResume() {
        super.onResume()
        openBackgroundThread()
        if (textureView.isAvailable) {
            setUpCamera()
            openCamera()
        } else {
            textureView.surfaceTextureListener = surfaceTextureListener
        }
    }

    override fun onStop() {
        super.onStop()
        closeCamera()
        closeBackgroundThread()
    }

    private fun closeCamera() {
        if (curCameraCaptureSession != null) {
            curCameraCaptureSession!!.close()
            curCameraCaptureSession = null
        }

        if (curCameraDevice != null) {
            curCameraDevice!!.close()
            curCameraDevice = null
        }
    }

    private fun closeBackgroundThread() {
        if (backgroundHandler != null) {
            backgroundThread!!.quitSafely()
            backgroundThread = null
            backgroundHandler = null
        }
    }



    private fun createPreviewSession() {
        try {
            val surfaceTexture = textureView.surfaceTexture
            surfaceTexture.setDefaultBufferSize(previewSize!!.width, previewSize!!.height)
            val previewSurface = Surface(surfaceTexture)
            captureRequestBuilder = curCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder!!.addTarget(previewSurface)

            curCameraDevice!!.createCaptureSession(Collections.singletonList(previewSurface),
                    object : CameraCaptureSession.StateCallback() {

                        override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                            if (curCameraDevice == null) {
                                return
                            }

                            try {
                                val captureRequest = captureRequestBuilder!!.build()
                                curCameraCaptureSession = cameraCaptureSession
                                curCameraCaptureSession!!.setRepeatingRequest(captureRequest, null, backgroundHandler)
                            } catch (e: CameraAccessException) {
                                e.printStackTrace()
                            }

                        }

                        override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {

                        }
                    }, backgroundHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    private fun createImageGallery() {
        val storageDirectory : File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        galleryFolder = object : File(storageDirectory, resources.getString(R.string.app_name)){}
        if(!galleryFolder!!.exists()) {
            val wasCreated : Boolean = galleryFolder!!.mkdirs()
            if (!wasCreated) {
                Log.e("CapturedImages", "Failed to create directory")
            }
        }
    }

    private fun createImageFile(galleryFolder : File) : File {
        val timeStamp : String = object : SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()){}.format(object : Date(){})
        val imageFileName : String = "image_" + timeStamp + "_"
        return File.createTempFile(imageFileName, ".jpg", galleryFolder)
    }

    private fun lock() {
        curCameraCaptureSession!!.capture(captureRequestBuilder!!.build(), null, backgroundHandler)
    }

    private fun unlock() {
        curCameraCaptureSession!!.setRepeatingRequest(captureRequestBuilder!!.build(), null, backgroundHandler)
    }

    override fun onClick(v: View?) {

        Log.v("CLICKED", "PIC CLICKED")
        if(v!!.id== R.id.btn_click_pic) {
//            AsyncTask.execute({
//                val outputPhoto = FileOutputStream(createImageFile(galleryFolder!!))
//                lock()
//                bitmap = textureView.bitmap
//                unlock()
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputPhoto)
//                outputPhoto.close()
//            })


//            val outputPhoto = FileOutputStream(createImageFile(galleryFolder!!))
            lock()
            bitmap = textureView.bitmap
            unlock()
            var intent = Intent(context,CameraPreview::class.java)
            startActivity(intent)
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, outputPhoto)
//            outputPhoto.close()


//            val handler = Handler()
//
//            val r = Runnable {
//                lock()
//                val outputPhoto: FileOutputStream?
//                outputPhoto = FileOutputStream(createImageFile(galleryFolder!!))
//                textureView.bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputPhoto)
//                unlock()
//                outputPhoto.close()
//            }
//            handler.post(r)


        }
        else if(v!!.id== R.id.btn_rev_cam) {
            closeCamera()
            closeBackgroundThread()
            cameraFacing = if(cameraFacing==CameraCharacteristics.LENS_FACING_FRONT)
                CameraCharacteristics.LENS_FACING_BACK
            else
                CameraCharacteristics.LENS_FACING_FRONT
            openBackgroundThread()
            setUpCamera()
            openCamera()
        }
    }

}

