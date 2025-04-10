package com.location.location.ui.location.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.location.location.databinding.ItemPlaceBinding
import com.location.location.models.PlaceModel

class PlaceAdapter(private val mContext: Context, val mArrayList: ArrayList<PlaceModel?>, val callback: Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            ItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            val mData = mArrayList[position]
            if (mData != null)
                holder.bindData(mData)
        }
    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    internal inner class MyViewHolder(private val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(mData: PlaceModel?) {
            // set Data here
            binding.txtPlaceName.text = mData?.name
            binding.txtAddress.text = mData?.address

            binding.txtPrimary.visibility = View.VISIBLE.takeIf { mData?.isPrimary == 1 } ?: View.GONE

        }

        init {
            binding.imgDelete.setOnClickListener {
                if (bindingAdapterPosition != -1) {
                    callback.onDeleteItemClick(mArrayList[bindingAdapterPosition])
                }
            }

            binding.imgEdit.setOnClickListener {
                if (bindingAdapterPosition != -1) {
                    callback.onEditItemClick(mArrayList[bindingAdapterPosition])
                }
            }
        }
    }

    interface Callback {
        fun onDeleteItemClick(bannerModel: PlaceModel?)
        fun onEditItemClick(bannerModel: PlaceModel?)
    }
}