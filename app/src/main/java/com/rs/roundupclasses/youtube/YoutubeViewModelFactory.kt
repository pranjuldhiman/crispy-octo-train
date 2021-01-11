package com.rs.roundupclasses.youtube

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class YoutubeViewModelFactory (val context: Context
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(YoutubeViewModel::class.java)) {
            return YoutubeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}