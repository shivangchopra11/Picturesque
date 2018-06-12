package com.example.shivang.picturesque.PhotoEditor

import android.content.Context
import android.opengl.GLSurfaceView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.shivang.picturesque.Gallery.PhoneAlbum
import com.example.shivang.picturesque.R

class EffectsAdapter(private val mContext: Context) : RecyclerView.Adapter<EffectsAdapter.EffectsViewHolder>() {

    private var mEffectList: ArrayList<String>? = null
    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    lateinit var view : View

    init {
        this.mEffectList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EffectsViewHolder {
        view = mInflater.inflate(R.layout.effect_list_card, parent, false)
        return EffectsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mEffectList?.size ?: 0
    }

    override fun onBindViewHolder(holder: EffectsViewHolder, position: Int) {
        val curEffect = mEffectList!![position]
        holder.effectName.text = curEffect
        holder.itemView.setOnClickListener({
            (mContext as PhotoEditor).onEffectClicked(position)
        })
    }

    fun setEffectList(effectList: List<String>) {
        this.mEffectList!!.clear()
        this.mEffectList!!.addAll(effectList)
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged()
    }


    inner class EffectsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById(R.id.img_effect_preview) as ImageView
        var effectName = itemView.findViewById(R.id.effect_name) as TextView
    }
}