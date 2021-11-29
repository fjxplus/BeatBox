package com.fanjiaxing.android.beatbox

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter

private const val TAG = "BeatBox"

/**
 * 视图模型层，保存和视图相关联的数据
 * 需要和布局文件进行沟通，使用绑定的Observable接口(BaseObservable)，或使用LivaData和数据绑定
 */
class SoundViewModel : BaseObservable() {

    var sound: Sound? = null
        set(sound) {
            field = sound
            notifyChange()
        }

    @get:Bindable
    val title: String?
        get() = sound?.name

    fun onButtonClicked(view: View) {

        title?.let {
            Log.d(TAG, it)
            Toast.makeText(view.context, title, Toast.LENGTH_SHORT).show()
        }
    }

}

/**
 * 使用BindingAdapter注解自定义属性
 * 第一个参数为传入的注解用到的Button，第二个参数为这个Button在定义该属性使传入的参数
 * 只要数据绑定碰到有该属性的button就会传入这个Button和绑定表达式结果，调用这个自定义的函数
 * 可以将Button延伸到View
 */
@BindingAdapter("app:soundName")
fun bindAssetSound(button: Button, assetFileName: String) {
    Log.d(TAG, assetFileName)
}