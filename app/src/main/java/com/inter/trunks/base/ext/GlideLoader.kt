package ru.mos.polls.kutil

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ru.mos.polls.app.GlideRequest


/**
 *  Загрузка цепочкой с листенером
 */
fun <TranscodeType> GlideRequest<TranscodeType>.loadWithListener(
        callback: ((success: Boolean, exception: GlideException?, bitmap: TranscodeType?) -> Unit)? = null
) = this.listener(object : RequestListener<TranscodeType> {
    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<TranscodeType>?, isFirstResource: Boolean): Boolean {
        callback?.invoke(false, e, null)
        return true
    }

    override fun onResourceReady(resource: TranscodeType?, model: Any?, target: Target<TranscodeType>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        callback?.invoke(true, null, resource)
        return true
    }
})
