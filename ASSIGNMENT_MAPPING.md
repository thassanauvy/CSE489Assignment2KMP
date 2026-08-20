# Assignment requirement → implementation mapping

| Assignment requirement | Project implementation |
|---|---|
| Navigation drawer | `MainActivity.kt`, `activity_main.xml`, `drawer_menu.xml` |
| Broadcast spinner | `BroadcastReceiverFragment.kt` |
| Custom text input in next activity | `TextInputActivity.kt` |
| Custom receiver in third activity | `CustomReceiverActivity.kt` dynamic `BroadcastReceiver` |
| Battery percentage broadcast | `BatteryReceiverActivity.kt` using `ACTION_BATTERY_CHANGED` |
| Internet image | `ImageScaleFragment.kt` HTTPS download |
| Pinch scaling | `ZoomableImageView.kt` + `ScaleGestureDetector` |
| Video inside app | `VideoFragment.kt` + bundled `sample_video.mp4` |
| Audio inside app | `AudioFragment.kt` + bundled `sample_audio.mp3` |
| Kotlin Multiplatform | `shared` KMP module with Android + desktop JVM targets |
