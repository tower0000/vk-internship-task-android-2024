package com.tower0000.vktask2024.ui.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tower0000.vktask2024.data.model.Item
import com.tower0000.vktask2024.databinding.RvItemBinding
import kotlin.math.roundToInt

class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>() {

    inner class ItemsViewHolder(private val binding: RvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.apply {
                Glide.with(itemView).load(item.itemThumb).into(imgItemThumb)
                item.offerPercentage.let {
                    val remainingPricePercentage = (100f - it) * 0.01
                    val priceAfterOffer = remainingPricePercentage * item.price
                    tvNewPrice.text = String.format("$%.2f", priceAfterOffer)

                    val discountInPercent = it.roundToInt()
                    tvDiscountPercent.text = String.format("-%d%%", discountInPercent)
                }
                tvOldPrice.text = String.format("%.2f", item.price)
                tvOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

                tvItemTitle.text = item.title.uppercase()
                tvRating.text = String.format("%.2f", item.itemRating)
                tvItemDescription.text = item.description
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Item>() {

        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        return ItemsViewHolder(
            RvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    var onClick: ((Item) -> Unit)? = null
}