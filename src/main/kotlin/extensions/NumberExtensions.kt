package extensions

fun Number.toTime(): String {
  val hours: Int = toInt() / 3600
  val minutes: Int = toInt() % 3600 / 60
  val seconds: Int = toInt() % 60
  return if (toInt() > 3600) {
    String.format("%02d:%02d:%02d", hours, minutes, seconds);
  } else {
    String.format("%02d:%02d", minutes, seconds)
  }
}
