package iss.nus.edu.sg.ca.matchgame

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DownloadService : Service() {

    inner class LocalBinder: Binder(){
        fun getService(): DownloadService{
            return this@DownloadService
        }
    }

    val binder: IBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    //function to get image from given URL
    //return a list of bitmap (images)
    fun getImage(imgUrl: MutableList<String>): MutableList<Bitmap> {
        val tempImg: MutableList<Bitmap> = mutableListOf()
        //establish HttpURLConnection variable
        var conn: HttpURLConnection? = null

        //counter for downloaded image
        var counter = 0;

        for (i in imgUrl) {
            try {
                //check for thread interruption before starting the download
                if (Thread.interrupted()) {
                    Log.e("Thread", "Thread interrupted, stopping download.")
                    return mutableListOf() //return empty list if interrupted
                }

                Log.e("Simulating", "Downloading: $i")


                //giving download a brief pause
                Thread.sleep(500)

                //parse as URL
                val url = URL(i);

                //open connection with HttpURLConnection
                conn = url.openConnection() as HttpURLConnection

                //Use get method
                conn.requestMethod = "GET"

                //set user-agent header as Mozilla
                conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
                )

                //if image can be downloaded
                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    Log.e("HTTP Response", "Image get successfully: $i")

                    //broadcast to the fetch activity to update progress bar and text

                    val intent = Intent()
                    intent.setAction("Update")
                    intent.putExtra("Counter",counter+1)

                    //send broadcast with intent and the current counter
                    sendBroadcast(intent)

                    //pass input stream of connection to show image
                    val inputStream: InputStream = conn.inputStream
                    //decode image from URL into bitmap
                    val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                    //add to list of image
                    tempImg.add(bitmap)
                } else {
                    Log.e("HTTP Response", "Failed to connect to image URL: $i")
                }

            } catch (e: InterruptedException) {
                // Handle interruption properly, if interrupted return a null bitmap list
                Log.e("Thread", "Thread was interrupted during sleep. Stopping download.")
                return mutableListOf()
            } catch (e: Exception) {
                Log.e("HTTP Response", "Failed to connect to image URL: ${e.message}")
            } finally {
                //disconnect the connection
                conn?.disconnect()
            }

        }
        //return the 20 images
        return tempImg
    }

    fun getHtml(urlString: String):MutableList<String>{
        //set up list for image urls
        var imgUrl : MutableList<String> = mutableListOf()

        //set up connection variable
        var conn: HttpURLConnection? = null
        try {
            //get URL
            val url = URL(urlString)
            conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
            )

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                // Read the HTML structure from URL
                // And fetch the list of 20 image URLS
                imgUrl = readFromServer(conn)
            }
        } catch (e: Exception) {
            Log.e("HTML response", "Error: ${e.message}", e)
        } finally {
            conn?.disconnect()
        }
        //return list of images url
        return imgUrl;
    }

    //function to extract src from <img> tags
    fun readFromServer(conn: HttpURLConnection): MutableList<String>{
        //list to keep the images
        val temp : MutableList<String> = mutableListOf()

        //counter to keep track how many img URLs have been extracted
        var i = 0;

        //set up buffered reader
        val inp = BufferedReader(InputStreamReader(conn.inputStream))

        //regular expression to search for <img... src="...".../>
        val srcRegex = """<img\s+[^>]*src="([^"]+)"""".toRegex()

        //read per line
        var line : String? = inp.readLine()

        //if 20 stop reading from server and return the URLs or there are lines left
        while (line != null&&i<20) {
            //if tag has <img> tag
            if(line.contains("<img")){

                //extract
                //result would be groupValues[0] as the full thing, ie <img src="http...."/>
                //then groupValues[1], groupValues[2] would be t
                val matchResult = srcRegex.find(line)

                //if there is src inside
                if(matchResult!=null){
                    val src = matchResult.groupValues[1]

                    // Extract the image source (src) attribute using the regular expression
                    // The matchResult will capture the full tag as groupValues[0] (e.g., <img src="http..."/>)
                    // and the actual URL in groupValues[1]
                    if(src.endsWith(".jpg")||src.endsWith(".png")){
                        //add to list of URL
                        temp.add(src)

                        //update counter
                        i++
                    }
                }

            }
            //read input
            line = inp.readLine()
        }

        //close input
        inp.close()

        //return list of URLs
        return temp
    }
}