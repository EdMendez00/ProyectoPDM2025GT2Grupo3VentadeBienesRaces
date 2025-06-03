package com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces.R

class FeatureAdapter(
    private val features: List<String>,
    private val onDeleteClick: (position: Int) -> Unit
) : RecyclerView.Adapter<FeatureAdapter.FeatureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feature, parent, false)
        return FeatureViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        holder.bind(features[position])
    }

    override fun getItemCount(): Int = features.size

    inner class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvFeature: TextView = itemView.findViewById(R.id.tvFeature)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteFeature)

        fun bind(feature: String) {
            tvFeature.text = feature
            btnDelete.setOnClickListener {
                onDeleteClick(adapterPosition)
            }
        }
    }
}
