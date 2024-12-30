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
    protected var images: MutableList<Bitmap>,
    private val buttonStates: MutableList<Boolean>,
    private var buttonEnabled: Boolean
) :  ArrayAdapter<Bitmap>(
    context, R.layout.image_card_view, images
){
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var _view = view

        //inflate grid view with image_card_view
        if (_view == null) {
            val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            _view = inflater.inflate(R.layout.image_card_view, parent, false)
        }

        //set the imageButton with the image in position
        val imageButton = _view!!.findViewById<ImageButton>(R.id.picPlaceholder)
        imageButton.setImageBitmap(images[position])

        //buttonStates is a list to check whether a button is clicked
        //if clicked then set alpha to 0.5f (dim)
        //if it is clicked again then toggle back the state to unclick
        if (buttonStates[position]) {
            imageButton.alpha = 0.5f // Dim the button when pressed
        } else {
            imageButton.alpha = 1.0f // Reset to full opacity when not pressed
        }

        //buttonEnabled is variable to track whether button can be
        //clicked or not
        //disabled when first load
        //enabled after image has been loaded
        if(buttonEnabled){
            imageButton.isEnabled = true
        }
        else{
            imageButton.isEnabled = false
        }

        //set function on click
        //the function is in FetchActivity (toggleButtonStatee)
        imageButton.setOnClickListener {
            //get toggle function from main activity
            (context as FetchActivity).toggleButtonState(position)
        }

        return _view
    }

    //function to update image with given array of images
    fun updateImages(newImages: MutableList<Bitmap>) {
        images.clear()
        images.addAll(newImages)
        notifyDataSetChanged()
    }

    //function to disable or enable image buttons
    fun updateEnableButtons(isEnabled: Boolean){
        buttonEnabled = isEnabled
        notifyDataSetChanged()
    }
}