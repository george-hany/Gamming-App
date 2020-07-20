package com.feature.rank.ui.rank

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import com.core.base.BaseFragment
import com.feature.rank.BR

import com.feature.rank.R
import com.feature.rank.databinding.FragmentRankBinding
import com.feature.rank.ui.rank.adapter.RanksListAdapter
import com.feature.rank.ui.rank.model.RankType
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.core.utils.SpacingItemDecoration

/**
 * A simple [Fragment] subclass.
 */
const val WEEK = "week"
const val MONTH = "month"
const val YEAR = "year"

class RankFragment : BaseFragment<FragmentRankBinding, RankViewModel>() {
    val rankViewModel: RankViewModel by viewModel()
    var opened = false
    override fun bindingVariable(): Int = BR.viewModel

    override fun layoutId(): Int = R.layout.fragment_rank

    override fun getViewModel(): RankViewModel = rankViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isSubFragment = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRanksRecycler()
        selectedDateObserver()
        rankMediatorLiveDataObserver()
        ranksUIListLiveDataObserver()
    }

    private fun setupRanksRecycler() {
        viewDataBinding.run {
            adapter = RanksListAdapter(arrayListOf())
            ranksRecycler.addItemDecoration(SpacingItemDecoration(0, 0, 20, 20))
        }
    }

    private fun ranksUIListLiveDataObserver() {
        rankViewModel.ranksUIListLiveData.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                viewDataBinding.adapter?.run {
                    ranksList.clear()
                    ranksList.addAll(it)
                    notifyDataSetChanged()
                }
            }
        })
    }

    private fun rankMediatorLiveDataObserver() {
        rankViewModel.rankMediatorLiveData.observe(viewLifecycleOwner, Observer {})
    }

    private fun selectedDateObserver() {
        rankViewModel.selectedDate.observe(viewLifecycleOwner, Observer {
            rankViewModel.getRanks()
        })
    }

    companion object {
        fun newInstance(rankType: RankType): RankFragment = RankFragment().apply {
            arguments = Bundle().apply {
                putParcelable(RANK_TYPE, rankType)
            }
        }

        val RANK_TYPE = "MODEL"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        opened = false
        userVisibleHint = true
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && !opened && baseActivity != null) {
            val rankType = arguments?.getParcelable<RankType>(RANK_TYPE)
            rankViewModel.run {
                when (rankType) {
                    RankType.WEEK -> {
                        rankUIType = getString(R.string.week)
                        rankNetworkType = WEEK
                    }
                    RankType.MONTH -> {
                        rankUIType = getString(R.string.month)
                        rankNetworkType = MONTH
                    }
                    RankType.YEAR -> {
                        rankUIType = getString(R.string.year)
                        rankNetworkType = YEAR
                    }
                }
                selectedDate.value = "$rankUIType $selectedNum"
            }
            opened = true
        }
    }
}
