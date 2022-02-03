package extensions

import net.md_5.bungee.api.ChatColor

fun getProgressBar(
  current: Int,
  max: Int,
  totalBars: Int = 50,
  symbol: String = "|",
  completedColor: ChatColor = ChatColor.GREEN,
  notCompletedColor: ChatColor = ChatColor.GRAY,
): String {
  val progressPercentage = current.toDouble() / max.toDouble()
  val sb = StringBuilder()
  for (i in 0 until totalBars) {
    if (i < totalBars * progressPercentage) {
      sb.append("$completedColor$symbol") //what to append if percentage is covered (e.g. GREEN '|'s)
    } else {
      sb.append("$notCompletedColor$symbol") //what to append if percentage is not covered (e.g. GRAY '|'s)
    }
  }
  return sb.toString()
}
