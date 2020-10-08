package kz.aviata.news.ui.topheadlines

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.top_heade_lines_fragment.*
import kz.aviata.news.R
import kz.aviata.news.data.models.ArticlesItem
import kz.aviata.news.ui.PagingAdapter
import kz.aviata.news.ui.baseclass.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopHeadLinesFragment : BaseFragment(R.layout.top_heade_lines_fragment) {

    private val viewModel: TopHeadLinesViewModel by viewModel()
    private val topHeadAdapter = PagingAdapter { item ->
        itemClick(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        top_recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = topHeadAdapter
            setHasFixedSize(false)
        }
        initViewModel()
        fb.setOnClickListener {
            findNavController().navigate(R.id.action_topHeadLinesFragment_to_savedArticlesFragment)
        }
    }

    override fun initViewModel() {
        viewModel.getTopHeadLines()
        viewModel.topHeadLinesPagedList.observe(viewLifecycleOwner) {
            topHeadAdapter.submitList(it)
        }
    }

    private fun itemClick(item: ArticlesItem) {
        viewModel.insertArticle(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        top_recycler.adapter = null
    }

}