package com.fanjiaxing.android.beatbox

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log
import java.io.IOException

private const val SOUNDS_FOLDER = "sample_sounds"
private const val TAG = "BeatBox"

/**
 * BeatBox为资源管理类
 * 构造函数需要传入用Context获取的AssetManager，从而对assets目录进行管理
 */
class BeatBox(private val assets: AssetManager) {

    val sounds: List<Sound>
    private val soundPool =
        SoundPool.Builder().setMaxStreams(5).build()        //创建SoundPool实例，最大同时播放5个
    private var streamIdList = mutableListOf<Int>()         //保存每次播放时的流Id
    private var rate = 1.0f                                 //保存速率，默认为1.0f

    init {
        sounds = loadsSounds()
    }

    private fun loadsSounds(): List<Sound> {

        val soundNames: Array<String>
        try {
            soundNames =
                assets.list(SOUNDS_FOLDER)!!       //AssetsManager.list(String)可获得传入path的全部资源名
        } catch (e: Exception) {
            return emptyList()
        }
        val sounds = mutableListOf<Sound>()
        soundNames.forEach { fileName ->                    //遍历文件名列表，创建Sound并添加至列表
            val assetPath = "$SOUNDS_FOLDER/$fileName"
            val sound = Sound(assetPath)
            try {
                load(sound)         // 预加载每个sound
                sounds.add(sound)
            } catch (ioe: IOException) {
                Log.d(TAG, "Could not load sound $fileName", ioe)
            }
        }
        return sounds
    }

    /**
     * 加载音频文件
     * 使用AssetManager获取sound的AssetFileDescriptor
     * 使用SoundPool.load()方法获得sound的Id
     */
    private fun load(sound: Sound) {
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
        val soundId = soundPool.load(afd, 1)
        sound.soundId = soundId
    }

    /**
     * 播放音频
     * 对sound的Id属性进行判空，调用SoundPool.play()方法播放
     */
    fun play(sound: Sound) {
        sound.soundId?.let {
            val streamId = soundPool.play(it, 1.0f, 1.0f, 1, 0, rate)
            streamIdList.add(streamId)
        }
    }

    /**
     * 释放soundPool，释放资源
     */
    fun release() {
        soundPool.release()
    }

    /**
     * 设置音频播放速率，速录范围为（0.5-2.0）
     */
    fun setRate(progress: Int) {
        rate = if (progress >= 50) {
            (progress - 50) * 1.0f / 50 + 1
        } else {
            1 - (50 - progress) * 0.5f / 50
        }
        if (streamIdList.isNotEmpty()) {
            for (streamId in streamIdList) {
                soundPool.setRate(streamId, rate)
            }
        }

    }

}