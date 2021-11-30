package com.fanjiaxing.android.beatbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fanjiaxing.android.beatbox.databinding.ActivityMainBinding
import com.fanjiaxing.android.beatbox.databinding.ListItemSoundBinding

private const val TAG = "BeatBox"

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this).get(
            MainActivityViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =                  //使用DataBindingUtil获取布局并和Activity天涯绑定
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel.beatBox = BeatBox(assets)         //创建BeatBox实例并用ViewModel保存

        binding.seekBar.apply {
            progress = 50       //初始时进度条为50
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {       //为进度条设置监听器
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    viewModel.beatBox.setRate(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }

        binding.initProgress.setOnClickListener {
            viewModel.beatBox.setRate(50)       //重置速率和进度条
            binding.seekBar.progress = 50
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = SoundAdapter(viewModel.beatBox.sounds)
        }

    }

    /**
     * Sounds为配合SoundAdapter使用的ViewHolder
     */
    private inner class SoundHolder(private val binding: ListItemSoundBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewModel = SoundViewModel(viewModel.beatBox)
        }

        fun bind(sound: Sound) {
            binding.apply {
                viewModel?.sound = sound
                executePendingBindings()
            }
        }
    }

    /**
     * SoundAdapter为RecyclerView的适配器，泛型指定为SoundHolder
     */
    private inner class SoundAdapter(private val sounds: List<Sound>) :
        RecyclerView.Adapter<SoundHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            //使用DataBindingUtil加载子项布局
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(
                layoutInflater,
                R.layout.list_item_sound,
                parent,
                false
            )
            return SoundHolder(binding)
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            val sound = sounds[position]
            holder.bind(sound)
        }

        override fun getItemCount(): Int {
            return sounds.size
        }

    }
}