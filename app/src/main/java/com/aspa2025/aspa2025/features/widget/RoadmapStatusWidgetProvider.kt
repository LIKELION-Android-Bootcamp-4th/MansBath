package com.aspa2025.aspa2025.features.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import com.aspa2025.aspa2025.R

class RoadmapStatusWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.roadmap_status_widget_init).apply {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("aspa://roadmap?fromWidget=true")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                setOnClickPendingIntent(R.id.linearlayout_roadmap_status_widget_init, pendingIntent)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent?.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {

            if (!intent.hasExtra("ROADMAP_TITLE")) return  // 로드맵 선택 이전에는 초기 위젯 디자인 유지 (위젯 업데이트 금지)

            val title = intent.getStringExtra("ROADMAP_TITLE") ?: "로드맵"
            val completed = intent.getIntExtra("ROADMAP_COMPLETED", 0)
            val all = intent.getIntExtra("ROADMAP_ALL", 0)


            val manager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context!!, RoadmapStatusWidgetProvider::class.java)
            val appWidgetIds = manager.getAppWidgetIds(thisWidget)

            for (appWidgetId in appWidgetIds) {
                updateWidget(context, manager, appWidgetId, title, completed, all)
            }
        }
    }

    companion object {
        fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            title: String,
            completed: Int,
            all: Int
        ) {
            val progress: Int = if (all > 0) ((completed.toFloat()) / all * 100).toInt() else 0
            Log.d("MYTAG", "all: $all completed: $completed")

            Log.d("MYTAG", "progress: $progress")

            val views = RemoteViews(context.packageName, R.layout.roadmap_status_widget).apply {
                setTextViewText(R.id.tv_title, title)
                setTextViewText(R.id.tv_completed, completed.toString())
                setTextViewText(R.id.tv_all, all.toString())
                setProgressBar(R.id.progress_bar, 100, progress, false)

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("aspa://roadmap?fromWidget=true")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                setOnClickPendingIntent(R.id.linearlayout_roadmap_status_widget, pendingIntent)

            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}