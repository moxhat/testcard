package com.madcrew.testcardapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.madcrew.testcardapp.R
import com.madcrew.testcardapp.models.ImagePreviewModel

class PageRecyclerAdapter(
    private var list: MutableList<ImagePreviewModel>,
    private val listenerImage: OnImageClickListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private inner class ViewHolderImageList internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal var image: ImageView? = null

        init {
            image = itemView.findViewById(R.id.rec_item_image)
            itemView.setOnClickListener(this)
        }

        internal fun bind(images: ImagePreviewModel){

        Glide.with(itemView.context)
            .load(images.image)
            .override(images.width, images.height)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(image!!)

        }

        override fun onClick(itemView: View?) {
            listenerImage.onImageClick(itemView, adapterPosition)
        }

    }

    interface OnImageClickListener {
        fun onImageClick(itemView: View?, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.page_recycler_item, parent, false)
        view.layoutParams = ViewGroup.LayoutParams((parent.width * 0.5).toInt(),ViewGroup.LayoutParams.WRAP_CONTENT)
        return ViewHolderImageList(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val images = list[position]
        (holder as PageRecyclerAdapter.ViewHolderImageList).bind(images)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}