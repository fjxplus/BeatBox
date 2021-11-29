package com.fanjiaxing.android.beatbox

private const val WAV = ".wav"

class Sound(private val assetPath: String) {
    val name = assetPath.split("/").last().removeSuffix(WAV)        //获取无后缀的文件名
}