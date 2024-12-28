package iss.nus.edu.sg.ca.matchgame

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.view.LayoutInflater
import android.widget.ImageButton

class ImageAdapter(
    private val context: Context,
    protected var images: MutableList<Bitmap>
) :  ArrayAdapter<Bitmap>(
    context, R.layout.image_card_view, images
){
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var _view = view
        if (_view == null) {
            val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            _view = inflater.inflate(R.layout.image_card_view, parent, false)
        }

        val imageView = _view!!.findViewById<ImageButton>(R.id.picPlaceholder)
        imageView.setImageBitmap(images[position])
        return _view
    }
}