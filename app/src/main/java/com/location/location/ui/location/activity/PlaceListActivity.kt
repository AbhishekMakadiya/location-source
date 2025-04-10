package com.location.location.ui.location.activity

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.location.location.R
import com.location.location.data.viewmodels.PlaceViewModel
import com.location.location.databinding.ActivityPlaceListBinding
import com.location.location.databinding.BottomSheetSortBinding
import com.location.location.models.PlaceModel
import com.location.location.ui.base.BaseActivity
import com.location.location.ui.location.adapter.PlaceAdapter
import com.location.location.utils.LogHelper
import com.location.location.utils.gone
import com.location.location.utils.itemDecoration.SpacesGridItemDecoration
import com.location.location.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceListActivity : BaseActivity<ActivityPlaceListBinding>(), PlaceAdapter.Callback,
    View.OnClickListener  {

    private var mAdapterPlace: PlaceAdapter? = null
    private var mArrayListPlace: ArrayList<PlaceModel?> = ArrayList()
    private var mLayoutManager: LinearLayoutManager? = null
    private val mPlaceViewModel: PlaceViewModel by viewModels()

    override fun getViewBinding() = ActivityPlaceListBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(mBinding.root)
        setLightStatusBarAppearance(true)
        applyWindowInsetsListener(mBinding.root)
    }

    override fun bindViews() {
        setUpToolBar(getString(R.string.place_list))

        val spacingVertical = resources.getDimensionPixelSize(R.dimen._4dp)
        val spacingHorizontal = resources.getDimensionPixelSize(R.dimen._4dp)

        mLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mAdapterPlace = PlaceAdapter(mContext, mArrayListPlace, this)

        with(mBinding.mRecyclerViewCategory) {
            layoutManager = mLayoutManager
            addItemDecoration(SpacesGridItemDecoration(spacingVertical, spacingHorizontal, true))
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapterPlace
        }

        observePlaceList()

        mBinding.imgButton.setOnClickListener(this)
        mBinding.btnPreviewMap.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v){
            mBinding.imgButton -> {
                AutocompleteDemoActivity.startActivity(mContext)
                finish()
            }
            mBinding.btnPreviewMap -> {
                if (mArrayListPlace.isNullOrEmpty().not()){
                    MapPreviewActivity.startActivity(mContext, mArrayListPlace)
                }

            }

        }
    }

    private fun observePlaceList() {
        mPlaceViewModel.allPlaces.observe(this) { placeList ->
            mArrayListPlace.clear()
            mArrayListPlace.addAll(placeList)
            LogHelper.e("TAG_LIST", mArrayListPlace.toString())
            mAdapterPlace?.notifyDataSetChanged()
            // Show No Data Vide
            if (mArrayListPlace.isEmpty()) {
                mBinding.lyNoInternet.root.visible()
                mBinding.lyNoInternet.txtMsg.text = getString(R.string.error_no_data)
            } else {
                mBinding.lyNoInternet.root.gone()
            }
        }
    }

    override fun onDeleteItemClick(place: PlaceModel?) {
        // Handle delete click here
        place?.let {
            mPlaceViewModel.deletePlace(it)
        }
    }

    override fun onEditItemClick(place: PlaceModel?) {
        // Handle edit click her
        place?.let {
            AutocompleteDemoActivity.startActivity(mContext, it)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        menu.add(
            MENU_SORT,
            MENU_SORT,
            MENU_SORT,
            "Sort"
        )?.setIcon(R.drawable.ic_sort)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MENU_SORT -> {
                showBottomsheetSort(mContext)
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun showBottomsheetSort(mContext: Context) {
        try {
            val dialog = BottomSheetDialog(this.mContext, R.style.AppBottomSheetDialogTheme)
            val bindingBottomSheet = BottomSheetSortBinding.inflate(layoutInflater)
            dialog.setContentView(bindingBottomSheet.root)
            dialog.show()


            bindingBottomSheet.txtSheetTitle.setOnClickListener {
                dialog.dismiss()
            }

            bindingBottomSheet.txtShortAsc.setOnClickListener {
                sortPlaceListByDistance(isAscending = true)
                dialog.dismiss()
            }

            bindingBottomSheet.txtShortDes.setOnClickListener {
                sortPlaceListByDistance(isAscending = false)
                dialog.dismiss()
            }

        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
        }
    }

    private fun sortPlaceListByDistance(isAscending: Boolean) {
        val primaryPlace = mArrayListPlace.firstOrNull { it?.isPrimary == 1 }

        if (primaryPlace == null || primaryPlace.latitude == null || primaryPlace.longitude == null) {
            Toast.makeText(this, "Primary place not found!", Toast.LENGTH_SHORT).show()
            return
        }

        mArrayListPlace.sortWith(compareBy { place ->
            if (place?.latitude != null && place.longitude != null) {
                calculateDistance(
                    primaryPlace.latitude!!, primaryPlace.longitude!!,
                    place.latitude!!, place.longitude!!
                )
            } else {
                Double.MAX_VALUE
            }
        })

        if (!isAscending) mArrayListPlace.reverse()

        mAdapterPlace?.notifyDataSetChanged()
    }

    fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0].toDouble()
    }

    companion object {
        const val MENU_SORT = 1
        fun startActivity(mContext: Context) {
            val intent = Intent(mContext, PlaceListActivity::class.java)
            mContext.startActivity(intent)
        }
    }


}
