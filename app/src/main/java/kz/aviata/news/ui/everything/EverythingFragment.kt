package kz.aviata.news.ui.everything

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.everything_fragment.*
import kz.aviata.news.R
import kz.aviata.news.data.models.ArticlesItem
import kz.aviata.news.ui.PagingAdapter
import kz.aviata.news.ui.baseclass.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class EverythingFragment
    : BaseFragment(R.layout.everything_fragment){

    private val viewModel: EverythingViewModel by viewModel()
    private val pagingAdapter = PagingAdapter { item ->
        itemClick(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        everything_rv.apply {
            adapter = pagingAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }
        initViewModel()
        swipe.setColorSchemeResources(R.color.blue, R.color.green, R.color.yellow, R.color.red)
        swipe.setOnRefreshListener{
            viewModel.refresh()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        everything_rv.adapter = null
    }

    override fun initViewModel() {
        viewModel.everythingPagedList.observe(viewLifecycleOwner, {
            pagingAdapter.submitList(it)
            swipe.isRefreshing = false
        })
    }

    private fun itemClick(item: ArticlesItem) {}

}