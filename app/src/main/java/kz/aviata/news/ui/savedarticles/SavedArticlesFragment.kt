package kz.aviata.news.ui.savedarticles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.news_item.*
import kotlinx.android.synthetic.main.saved_articles_fragment.*
import kz.aviata.news.R
import kz.aviata.news.data.models.ArticlesItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedArticlesFragment : DialogFragment() {

    private val viewModel: SavedArticlesViewModel by viewModel()
    private val savedAdapter = SavedAdapter{ item ->
        itemRemove(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.saved_articles_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saved_rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = savedAdapter
            setHasFixedSize(false)
        }
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.getAllArticle().observe(viewLifecycleOwner, { articles ->
            articles.forEach {
                println("### $it")
            }
            savedAdapter.items = articles.toMutableList()
        })
    }

    private fun itemRemove(item: ArticlesItem) {
        viewModel.deleteArticle(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saved_rv.adapter = null
    }

}