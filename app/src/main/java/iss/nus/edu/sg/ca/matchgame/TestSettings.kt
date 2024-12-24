package iss.nus.edu.sg.ca.matchgame

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


object TestSettings {
    private val imgLinks = listOf(
        //"https://p4.wallpaperbetter.com/wallpaper/291/663/679/stones-background-stones-spa-wallpaper-preview.jpg",
        "https://cdn.stocksnap.io/img-thumbs/960w/architecture-background_WVGMMJMQ7U.jpg",
        "https://cdn.stocksnap.io/img-thumbs/960w/christmas-baking_HL7K6WXSPC.jpg",
        "https://cdn.stocksnap.io/img-thumbs/960w/holiday-lights_JDBC9M9UJU.jpg",
        "https://cdn.stocksnap.io/img-thumbs/960w/landscape-sky_TKZMRNCCKH.jpg",
        "https://cdn.stocksnap.io/img-thumbs/960w/sea-sunset_3X99ALGN5N.jpg",
        "https://cdn.stocksnap.io/img-thumbs/960w/macro-christmas_ZEWQPPPTSN.jpg",
        )

    fun getImgLink(index: Int): String {
        return imgLinks[index]
    }

    fun getImgLinks(): List<String> {
        return imgLinks
    }

    fun getImgBitmap(index: Int): Bitmap {
        val mWebPath = getImgLink(index)
        return Picasso.get().load(mWebPath).get()
    }

}
