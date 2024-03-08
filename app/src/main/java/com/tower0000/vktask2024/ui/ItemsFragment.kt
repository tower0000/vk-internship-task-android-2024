package com.tower0000.vktask2024.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.tower0000.vktask2024.R
import com.tower0000.vktask2024.databinding.FragmentItemsBinding

class ItemsFragment : Fragment() {
    private lateinit var binding: FragmentItemsBinding
    private val adapter by lazy { ItemsAdapter() }
    private val vm by activityViewModels<ItemsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemsBinding.inflate(inflater)
        return inflater.inflate(R.layout.fragment_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}