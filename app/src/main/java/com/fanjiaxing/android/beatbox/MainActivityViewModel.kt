package com.fanjiaxing.android.beatbox

import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    lateinit var beatBox: BeatBox       //保存beatBox

    override fun onCleared() {
        super.onCleared()
        beatBox.release()       //当程序退出时释放beatBox
    }
}