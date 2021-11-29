package com.fanjiaxing.android.beatbox

import android.content.res.AssetManager

private const val SOUNDS_FOLDER = "sample_sounds"

/**
 * BeatBox为资源管理类
 * 构造函数需要传入用Context获取的AssetManager，从而对assets目录进行管理
 */
class BeatBox(private val assets: AssetManager) {

    val sounds: List<Sound>

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
            sounds.add(sound)
        }
        return sounds
    }
}