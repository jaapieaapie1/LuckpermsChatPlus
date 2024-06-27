package nl.jaapie.luckpermschatplus

import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import java.util.stream.Collectors

class LPCPListener(private val plugin: LuckpermsChatPlus) : Listener {
    companion object {
        private val miniMessage = MiniMessage.miniMessage()
        private val legacyMessage = LegacyComponentSerializer.legacyAmpersand()
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onChat(event: AsyncChatEvent) {
        val metaData = plugin.luckperms.getPlayerAdapter(Player::class.java).getMetaData(event.player)
        val group = metaData.primaryGroup

        event.renderer { player, displayName, message, _ ->
            plugin.config.groupFormats[group] ?: plugin.config.format
                .replaceAndFormat("{prefix}", metaData.prefix ?: "")
                .replaceAndFormat("{suffix}", metaData.suffix ?: "")
                .replaceAndFormat(
                    "{prefixes}",
                    metaData.prefixes.entries.stream().sorted(Comparator.comparingInt { it.key })
                        .map { it.value }
                        .collect(Collectors.joining())
                )
                .replaceAndFormat(
                    "{suffixes}",
                    metaData.suffixes.entries.stream().sorted(Comparator.comparingInt { it.key })
                        .map { it.value }
                        .collect(Collectors.joining())
                )
                .replace("{world}", player.world.name)
                .replace("{name}", player.name)
                .replace("{displayname}", displayName)
                .replace("{message}", message)
        }
    }

    private fun String.parse(): Component {
        if (plugin.config.parseLegacy) {
            return miniMessage.deserialize(miniMessage.serialize(legacyMessage.deserialize(this)))
        }

        return miniMessage.deserialize(this)
    }

    private fun Component.replace(replace: String, with: String): Component {
        return this.replaceText(
            TextReplacementConfig.builder()
                .matchLiteral(replace)
                .replacement(with)
                .build()
        )
    }

    private fun Component.replaceAndFormat(replace: String, with: String): Component {
        return this.replace(replace, with.parse())
    }

    private fun Component.replace(replace: String, with: ComponentLike): Component {
        return this.replaceText(
            TextReplacementConfig.builder()
                .matchLiteral(replace)
                .replacement(with)
                .build()
        )
    }
}