package com.parsempo.ImageCropTools

import android.content.res.Resources
import android.util.TypedValue
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType

fun ReadableMap.getBooleanOrDefault(key: String, defaultValue: Boolean): Boolean {
    return if (hasKey(key) && getType(key) == ReadableType.Boolean) getBoolean(key) else defaultValue
}

fun ReadableMap.getIntOrDefault(key: String, defaultValue: Int): Int {
    return if (hasKey(key) && getType(key) == ReadableType.Number) getInt(key) else defaultValue
}

fun ReadableMap.getDoubleOrDefault(key: String, defaultValue: Double): Double {
    return if (hasKey(key) && getType(key) == ReadableType.Number) getDouble(key) else defaultValue
}

fun Float.dpToPx(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)
}