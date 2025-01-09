package com.parsempo.ImageCropTools

import android.graphics.Bitmap
import android.net.Uri
import com.canhub.cropper.CropImageOptions
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.canhub.cropper.CropImageView
import com.facebook.react.uimanager.events.RCTEventEmitter
import java.io.File
import java.util.*

class ImageCropViewManager: SimpleViewManager<CropImageView>() {
    companion object {
        const val REACT_CLASS = "CropView"
        const val ON_IMAGE_SAVED = "onImageSaved"
        const val SOURCE_URL_PROP = "sourceUrl"
        const val KEEP_ASPECT_RATIO_PROP = "keepAspectRatio"
        const val ASPECT_RATIO_PROP = "cropAspectRatio"
        const val SAVE_IMAGE_COMMAND = 1
        const val ROTATE_IMAGE_COMMAND = 2
        const val SAVE_IMAGE_COMMAND_NAME = "saveImage"
        const val ROTATE_IMAGE_COMMAND_NAME = "rotateImage"
        const val CROP_IMAGE_OPTIONS_PROP = "cropImageOptions"
    }

    override fun createViewInstance(reactContext: ThemedReactContext): CropImageView {
        val view =  CropImageView(reactContext)
        view.setOnCropImageCompleteListener { _, result ->
            if (result.isSuccessful) {
                val map = Arguments.createMap()
                map.putString("uri", result.getUriFilePath(reactContext, true).toString())
                map.putInt("x", result.cropRect!!.left)
                map.putInt("y", result.cropRect!!.top)
                map.putInt("width", result.cropRect!!.width())
                map.putInt("height", result.cropRect!!.height())
                reactContext.getJSModule(RCTEventEmitter::class.java)?.receiveEvent(
                        view.id,
                        ON_IMAGE_SAVED,
                        map
                )
            }
        }
        return view
    }

    override fun getName(): String {
        return REACT_CLASS
    }

    override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any> {
        return MapBuilder.of(
                ON_IMAGE_SAVED,
                MapBuilder.of("registrationName", ON_IMAGE_SAVED)
        )
    }

    override fun getCommandsMap(): MutableMap<String, Int> {
        return MapBuilder.of(
                SAVE_IMAGE_COMMAND_NAME, SAVE_IMAGE_COMMAND,
                ROTATE_IMAGE_COMMAND_NAME, ROTATE_IMAGE_COMMAND
        )
    }

    override fun receiveCommand(root: CropImageView, commandId: Int, args: ReadableArray?) {
        when (commandId) {
            SAVE_IMAGE_COMMAND -> {
                val preserveTransparency = args?.getBoolean(0) ?: false
                var extension = "jpg"
                var format = Bitmap.CompressFormat.JPEG
                if (preserveTransparency && root.getCroppedImage(0, 0)!!.hasAlpha()) {
                    extension = "png"
                    format = Bitmap.CompressFormat.PNG
                }
                val path = File(root.context.cacheDir, "${UUID.randomUUID()}.$extension").toURI().toString()
                val quality = args?.getInt(1) ?: 100

                root.croppedImageAsync(format, quality, customOutputUri = Uri.parse(path))
            }
            ROTATE_IMAGE_COMMAND -> {
                val clockwise = args?.getBoolean(0) ?: true
                root.rotateImage(if (clockwise) 90 else -90)
            }
        }
    }

    @ReactProp(name = SOURCE_URL_PROP)
    fun setSourceUrl(view: CropImageView, url: String?) {
        url?.let {
            view.setImageUriAsync(Uri.parse(it))
        }
    }

    @ReactProp(name = KEEP_ASPECT_RATIO_PROP)
    fun setFixedAspectRatio(view: CropImageView, fixed: Boolean) {
//        view.setFixedAspectRatio(fixed)
    }

    @ReactProp(name = ASPECT_RATIO_PROP)
    fun setAspectRatio(view: CropImageView, aspectRatio: ReadableMap?) {
//        if (aspectRatio != null) {
//            view.setAspectRatio(aspectRatio.getInt("width"), aspectRatio.getInt("height"))
//        }else {
//            view.clearAspectRatio()
//        }
    }

    @ReactProp(name = CROP_IMAGE_OPTIONS_PROP)
    fun setCropImageOptions(view: CropImageView, options: ReadableMap?) {
        if (options == null) return

        view.setImageCropOptions(
            CropImageOptions(
                cropShape = options.getString("cropShape")?.let { CropImageView.CropShape.valueOf(it) } ?: CropImageView.CropShape.RECTANGLE,
                cornerShape = options.getString("cornerShape")?.let { CropImageView.CropCornerShape.valueOf(it) } ?: CropImageView.CropCornerShape.RECTANGLE,
                cropCornerRadius = options.getDoubleOrDefault("cropCornerRadius", 10.0).toFloat().dpToPx(),
                snapRadius = options.getDoubleOrDefault("snapRadius", 3.0).toFloat().dpToPx(),
                touchRadius = options.getDoubleOrDefault("touchRadius", 24.0).toFloat().dpToPx(),
                scaleType = options.getString("scaleType")?.let { CropImageView.ScaleType.valueOf(it) } ?: CropImageView.ScaleType.FIT_CENTER,
                autoZoomEnabled = options.getBooleanOrDefault("autoZoomEnabled", false),
                multiTouchEnabled = options.getBooleanOrDefault("multiTouchEnabled", false),
                centerMoveEnabled = options.getBooleanOrDefault("centerMoveEnabled", true),
                canChangeCropWindow = options.getBooleanOrDefault("canChangeCropWindow", true),
                maxZoom = options.getIntOrDefault("maxZoom", 4),
                fixAspectRatio = options.getBooleanOrDefault("fixAspectRatio", false),
                aspectRatioX = options.getIntOrDefault("aspectRatioX", 1),
                aspectRatioY = options.getIntOrDefault("aspectRatioY", 1),
                minCropWindowWidth = options.getIntOrDefault("minCropWindowWidth", 42).toFloat().dpToPx().toInt(),
                minCropWindowHeight = options.getIntOrDefault("minCropWindowHeight", 42).toFloat().dpToPx().toInt(),
                minCropResultWidth = options.getIntOrDefault("minCropResultWidth", 40),
                minCropResultHeight = options.getIntOrDefault("minCropResultHeight", 40),
                maxCropResultWidth = options.getIntOrDefault("maxCropResultWidth", 99999),
                maxCropResultHeight = options.getIntOrDefault("maxCropResultHeight", 99999),
            )
        )
    }
}
