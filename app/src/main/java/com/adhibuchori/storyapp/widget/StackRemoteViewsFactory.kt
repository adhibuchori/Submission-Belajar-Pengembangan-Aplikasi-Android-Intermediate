package com.adhibuchori.storyapp.widget

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.adhibuchori.storyapp.R
import com.adhibuchori.storyapp.data.local.room.StoryDatabase
import com.adhibuchori.storyapp.data.remote.response.Story
import com.adhibuchori.storyapp.data.remote.utils.getBitMap

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = mutableListOf<Story>()
    private val database = StoryDatabase.getInstance(mContext)
    private val dao = database.storyDao()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        val stories = dao.getStories()

        mWidgetItems.addAll(stories)
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)


        val photoBitmap = getBitMap(mWidgetItems[position].photoUrl)
        rv.setImageViewBitmap(R.id.imageView, photoBitmap)

        val extras = bundleOf(StoryWidget.EXTRA_ITEM to mWidgetItems[position].description)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}