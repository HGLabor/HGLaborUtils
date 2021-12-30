package extensions

import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffectType

fun LivingEntity.clearPotionEffects(vararg effects: PotionEffectType = emptyArray()) {
  if (effects.isEmpty())
    activePotionEffects.forEach { removePotionEffect(it.type) }
  else
    effects.forEach { removePotionEffect(it) }
}
