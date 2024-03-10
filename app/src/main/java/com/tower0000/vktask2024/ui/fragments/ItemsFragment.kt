package com.tower0000.vktask2024.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tower0000.vktask2024.R
import com.tower0000.vktask2024.databinding.FragmentItemsBinding
import com.tower0000.vktask2024.ui.adapters.ItemsAdapter
import com.tower0000.vktask2024.ui.util.PageMode
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
        var categories: List<String>? = null
        setupItemsRv()

        binding.nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                vm.loadMoreItems()
            }
        })

        productsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("item", it) }
            findNavController().navigate(R.id.action_itemsFragment_to_itemInfoFragment, b)
        }

        binding.buttonSearch.setOnClickListener {
            if (binding.edSearchRequest.text.toString() == "") return@setOnClickListener
            vm.switchToSearchMode(binding.edSearchRequest.text.toString())
            binding.buttonResetSearch.visibility = View.VISIBLE
        }

        binding.buttonResetCategory.setOnClickListener {
            vm.switchToBaseMode()
        }

        binding.buttonResetSearch.setOnClickListener {
            vm.switchToBaseMode()
        }

        binding.buttonCategoryMenu.setOnClickListener {
            showPopupMenu(it, categories)
        }

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

        vm.categories.observe(viewLifecycleOwner) {
            when (it) {
                is ResourceState.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }

                is ResourceState.Success -> {
                    binding.progressbar.visibility = View.INVISIBLE
                    categories = it.data
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

        vm.pageMode.observe(viewLifecycleOwner) {
            when (it) {
                is PageMode.Base -> {
                    hideCategory()
                    hideSearchResult()
                }

                is PageMode.Search -> {
                    hideCategory()
                    showSearchResult(it.data)
                }

                is PageMode.Category -> {
                    hideSearchResult()
                    showCategory(it.data)
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

    private fun showPopupMenu(view: View, categories: List<String>?) {
        if (categories == null) return
        val popupMenu = PopupMenu(requireContext(), view)
        for (cat in categories) {
            popupMenu.menu.add(cat)
        }

        popupMenu.setOnMenuItemClickListener { item ->
            val categoryName = item.title.toString()
            vm.switchToCategoryMode(categoryName)
            true
        }

        popupMenu.show()
    }

    private fun hideCategory() {
        binding.tvCategory.visibility = View.GONE
        binding.buttonResetCategory.visibility = View.GONE
    }

    private fun hideSearchResult() {
        binding.edSearchRequest.text = null
        binding.buttonResetSearch.visibility = View.INVISIBLE
        binding.tvSearchResults.visibility = View.INVISIBLE
    }

    private fun showCategory(category: String?) {
        binding.tvCategory.text = getString(
            R.string.category_results,
            category
        )
        binding.tvCategory.visibility = View.VISIBLE
        binding.buttonResetCategory.visibility = View.VISIBLE
    }

    private fun showSearchResult(query: String?) {
        binding.tvSearchResults.text = getString(
            R.string.search_results,
            query
        )
        binding.buttonResetSearch.visibility = View.VISIBLE
        binding.tvSearchResults.visibility = View.VISIBLE
    }
}