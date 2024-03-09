package com.tower0000.vktask2024.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tower0000.vktask2024.R
import com.tower0000.vktask2024.databinding.FragmentItemsBinding
import com.tower0000.vktask2024.ui.util.ResourceState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemsFragment : Fragment() {
    private lateinit var binding: FragmentItemsBinding
    private val productsAdapter by lazy { ItemsAdapter() }
    private val vm by activityViewModels<ItemsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupItemsRv()
        vm.loadItems()
        vm.itemsData.observe(viewLifecycleOwner) {
            when (it) {
                is ResourceState.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }

                is ResourceState.Success -> {
                    binding.progressbar.visibility = View.INVISIBLE
                    productsAdapter.differ.submitList(it.data?.products)
                }

                is ResourceState.Error -> {
                    binding.progressbar.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireContext(),
                        it.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> Unit
            }
        }
    }
    private fun setupItemsRv() {
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = productsAdapter
        }
    }
}