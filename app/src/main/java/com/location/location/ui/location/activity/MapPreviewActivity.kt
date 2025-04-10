package com.location.location.ui.location.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.location.location.R
import com.location.location.databinding.ActivityMapPreviewBinding
import com.location.location.models.PlaceModel
import com.location.location.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

@AndroidEntryPoint
class MapPreviewActivity : BaseActivity<ActivityMapPreviewBinding>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var locationList: ArrayList<PlaceModel?> = arrayListOf()

    override fun getViewBinding(): ActivityMapPreviewBinding = ActivityMapPreviewBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(mBinding.root)
        setLightStatusBarAppearance(true)
        applyWindowInsetsListener(mBinding.root)
    }

    override fun bindViews() {
        setUpToolBar(getString(R.string.preview_map), R.drawable.ic_back)

        locationList = intent.getSerializableExtra("PLACE_LIST") as ArrayList<PlaceModel?>

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        drawMarkersAndPath()
    }

    private fun drawMarkersAndPath() {
        if (locationList.isEmpty()) return

        val boundsBuilder = LatLngBounds.Builder()

        for ((index, place) in locationList.withIndex()) {
            place?.let {
                val position = LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0)
                mMap.addMarker(
                    MarkerOptions().position(position).title(it.name)
                )
                boundsBuilder.include(position)

                // Draw path to next marker if available
                if (index < locationList.size - 1) {
                    val origin = "${it.latitude},${it.longitude}"
                    val destPlace = locationList[index + 1]
                    val destination = "${destPlace?.latitude},${destPlace?.longitude}"
                    fetchAndDrawPath(origin, destination)
                }
            }
        }

        // Move camera to fit all markers
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100))
    }

    private fun fetchAndDrawPath(origin: String, destination: String) {
        val url = "https://maps.googleapis.com/maps/api/directions/json" +
                "?origin=$origin&destination=$destination&key=AIzaSyBSNyp6GQnnKlrMr7hD2HGiyF365tFlK5U&mode=driving"

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val result = URL(url).readText()
                val json = JSONObject(result)
                val routes = json.getJSONArray("routes")
                if (routes.length() > 0) {
                    val points = routes.getJSONObject(0)
                        .getJSONObject("overview_polyline")
                        .getString("points")

                    val decodedPath = PolyUtil.decode(points)

                    withContext(Dispatchers.Main) {
                        mMap.addPolyline(
                            PolylineOptions()
                                .addAll(decodedPath)
                                .width(8f)
                                .color(Color.BLUE)
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("MapPreview", "Failed to fetch directions", e)
            }
        }
    }

    companion object {
        const val MENU_SORT = 1
        fun startActivity(mContext: Context, mArrayListPlace: ArrayList<PlaceModel?>) {
            val intent = Intent(mContext, MapPreviewActivity::class.java)
            intent.putExtra("PLACE_LIST", mArrayListPlace)
            mContext.startActivity(intent)
        }
    }
}
