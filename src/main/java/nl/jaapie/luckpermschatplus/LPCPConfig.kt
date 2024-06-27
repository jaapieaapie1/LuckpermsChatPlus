package nl.jaapie.luckpermschatplus

import com.electronwill.nightconfig.core.CommentedConfig
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import java.nio.file.Path
import java.util.stream.Collectors

class LPCPConfig(
    val parseLegacy: Boolean,
    val format: Component,
    val groupFormats: Map<String, Component>,
) {
    companion object {
        fun read(path: Path): LPCPConfig {
            val defaultConfig = Companion::class.java.classLoader.getResource("default-config.toml")
                ?: throw IllegalStateException("Default config not found")

            val config = CommentedFileConfig.builder(path)
                .defaultData(defaultConfig)
                .autosave()
                .preserveInsertionOrder()
                .sync()
                .build()

            config.load()

            val miniMessage = MiniMessage.miniMessage()

            val parseLegacy = config.get<Boolean>("parse-legacy-color-codes")
                ?: throw IllegalStateException("Parse legacy not found in config")

            val rawFormat = config.get<String>("chat-format")
                ?: throw IllegalStateException("Format not found in config")

            val format = miniMessage.deserialize(rawFormat)

            val rawGroupFormats = config.get<CommentedConfig>("group-formats")
                ?: throw IllegalStateException("Group formats not found in config")

            val groupFormats = rawGroupFormats.entrySet().stream()
                .map { entry -> entry.key to miniMessage.deserialize(entry.getValue()) }
                .collect(Collectors.toMap({ it.first }, { it.second }))

            return LPCPConfig(parseLegacy, format, groupFormats)
        }
    }
}