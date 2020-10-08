package kz.aviata.news.ui.baseclass

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

abstract class BaseFragment(layout: Int): Fragment(layout){
    abstract fun initViewModel()
    fun close() = findNavController().popBackStack()
}