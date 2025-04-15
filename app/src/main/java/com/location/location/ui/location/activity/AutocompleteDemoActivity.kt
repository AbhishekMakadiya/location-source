package com.location.location.ui.location.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.kotlin.PlaceSelectionError
import com.google.android.libraries.places.widget.kotlin.PlaceSelectionSuccess
import com.google.android.libraries.places.widget.kotlin.placeSelectionEvents
import com.location.location.R
import com.location.location.data.viewmodels.PlaceViewModel
import com.location.location.databinding.ActivityAutocompleteBinding
import com.location.location.models.PlaceModel
import com.location.location.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AutocompleteDemoActivity : BaseActivity<ActivityAutocompleteBinding>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val mPlaceViewModel: PlaceViewModel by viewModels()

    private var isEditMode = false
    private var existingPlace: PlaceModel? = null

    override fun getViewBinding(): ActivityAutocompleteBinding = ActivityAutocompleteBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(mBinding.root)
        setLightStatusBarAppearance(true)
        applyWindowInsetsListener(mBinding.root)

        existingPlace = intent.getSerializableExtra("PLACE_DATA") as? PlaceModel
        isEditMode = existingPlace != null
    }


    override fun bindViews() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS,
                Place.Field.DISPLAY_NAME,
            )
        )

        // If editing, show existing place
        existingPlace?.let { place ->
            val latLng = LatLng(place.latitude ?: 0.0, place.longitude ?: 0.0)
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(latLng).title(place.name))?.showInfoWindow()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            autocompleteFragment.setText(place.name ?: "")
        }

        lifecycleScope.launch {
            autocompleteFragment.placeSelectionEvents().collect { event ->
                when (event) {
                    is PlaceSelectionSuccess -> {
                        val name = event.place.name
                        val address = event.place.address
                        val latLng = event.place.latLng

                        if (latLng != null && ::mMap.isInitialized) {
                            mMap.clear()
                            mMap.addMarker(MarkerOptions().position(latLng).title(name))?.showInfoWindow()
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                            val placeModel = PlaceModel(
                                placeId = existingPlace?.placeId.takeIf { isEditMode },
                                name = name,
                                address = address,
                                latitude = latLng.latitude,
                                longitude = latLng.longitude,
                                isPrimary = if (isEditMode) existingPlace?.isPrimary ?: 0 else 0
                            )

                            if (isEditMode) {
                                mPlaceViewModel.updatePlace(placeModel)
                                Toast.makeText(this@AutocompleteDemoActivity, "Place updated", Toast.LENGTH_SHORT).show()
                            } else {
                                mPlaceViewModel.insertPlace(placeModel) // placeId = 0 lets Room auto-generate
                                Toast.makeText(this@AutocompleteDemoActivity, "Place added", Toast.LENGTH_SHORT).show()
                            }
                            PlaceListActivity.startActivity(mContext)
                            finish()
                        }
                    }

                    is PlaceSelectionError -> {
                        Toast.makeText(
                            mContext,
                            "Failed to get place '${event.status.statusMessage}'",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    companion object {
        fun startActivity(mContext: Context, place: PlaceModel? = null) {
            val intent = Intent(mContext, AutocompleteDemoActivity::class.java)
            intent.putExtra("PLACE_DATA", place)
            mContext.startActivity(intent)
        }
    }
}

