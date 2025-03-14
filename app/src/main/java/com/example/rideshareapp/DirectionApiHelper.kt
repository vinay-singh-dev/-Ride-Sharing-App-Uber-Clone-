

package com.example.rideshareapp

import android.os.AsyncTask
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject
import java.net.URL

class DirectionApiHelper(private val mMap: GoogleMap) : AsyncTask<String, Void, String>() {
    override fun doInBackground(vararg params: String?): String {
        val url = params[0]
        return URL(url).readText()
    }

    override fun onPostExecute(result: String?) {
        val json = JSONObject(result)
        val routes = json.getJSONArray("routes")
        val polyline = routes.getJSONObject(0)
            .getJSONObject("overview_polyline")
            .getString("points")

        val polylineOptions = PolylineOptions().addAll(DecodePoly.decode(polyline))
        mMap.addPolyline(polylineOptions)
    }
}
