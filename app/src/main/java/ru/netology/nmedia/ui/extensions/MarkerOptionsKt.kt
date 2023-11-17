package ru.netology.nmedia.ui.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

fun MarkerOptions.icon(drawable: Drawable) {
    val canvas = Canvas()
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    canvas.setBitmap(bitmap)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    drawable.draw(canvas)
    icon(BitmapDescriptorFactory.fromBitmap(bitmap))
}
fun stringToLatLong (position: String): LatLng {
    var posToParse = position.substringAfter("lat/lng:")

    "(".forEach {
        posToParse = posToParse.replace(it.toString(),"")
    }

    ")".forEach {
        posToParse = posToParse.replace(it.toString(),"")
    }
    val latlong = posToParse.split(",".toRegex()).dropLastWhile { it.isEmpty() }
        .toTypedArray()
    val latitude = latlong[0].toDouble()
    val longitude = latlong[1].toDouble()
    val location: LatLng = LatLng (latitude,longitude)
    return location
}