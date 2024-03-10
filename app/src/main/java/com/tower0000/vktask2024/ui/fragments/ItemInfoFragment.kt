package com.tower0000.vktask2024.ui.fragments

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.tower0000.vktask2024.R
import com.tower0000.vktask2024.databinding.FragmentItemInfoBinding
import com.tower0000.vktask2024.ui.adapters.ViewPagerImagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class ItemInfoFragment : Fragment() {
    private lateinit var binding: FragmentItemInfoBinding
    private val args by navArgs<ItemInfoFragmentArgs>()
    private val viewPagerAdapter by lazy { ViewPagerImagesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewpager()
        setupImageIndicators()
        val currentItem = args.item

        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.apply {
            currentItem.offerPercentage.let {
                val remainingPricePercentage = (100f - it) * 0.01
                val priceAfterOffer = remainingPricePercentage * currentItem.price
                tvProductNewPrice.text = String.format("$%.2f", priceAfterOffer)

                val discountInPercent = it.roundToInt()
                tvDiscountPercentProduct.text = String.format("-%d%%", discountInPercent)
            }
            tvProductOldPrice.text = String.format("%.2f", currentItem.price)
            tvProductOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

            tvProductTitle.text = currentItem.title
            tvRating.text = String.format("%.2f", currentItem.itemRating)
            tvProductDescription.text = String.format("Description:\n%s", currentItem.description)
            tvBrand.text = String.format("Brand:  %s", currentItem.itemBrand)
            tvInStock.text = String.format("In stock:  %s", currentItem.stockQuantity)
            tvCategory.text = String.format("Category:  %s", currentItem.itemCategory)
        }
        viewPagerAdapter.differ.submitList(currentItem.itemImages)
    }

    private fun setupImageIndicators() {
        val dotsVerticalMargin = 0
        val dotsHorizontalMargin = 8
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(
                dotsHorizontalMargin,
                dotsVerticalMargin,
                dotsHorizontalMargin,
                dotsVerticalMargin
            )
        }
        val product = args.item
        val dotsImage = Array(product.itemImages.size) { ImageView(requireContext()) }

        dotsImage.forEach {
            it.setImageResource(R.drawable.non_active_dot)
            binding.slideDotLL.addView(it, params)
        }

        dotsImage[0].setImageResource(R.drawable.active_dot)

        val pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                dotsImage.mapIndexed { index, imageView ->
                    if (position == index) imageView.setImageResource(R.drawable.active_dot)
                    else imageView.setImageResource(R.drawable.non_active_dot)
                }
                super.onPageSelected(position)
            }
        }
        binding.viewPagerProductImages.registerOnPageChangeCallback(pageChangeListener)
    }

    private fun setupViewpager() {
        binding.apply {
            viewPagerProductImages.adapter = viewPagerAdapter
        }
    }
}