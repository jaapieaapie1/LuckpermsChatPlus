package nl.jaapie.luckpermschatplus

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class LPCPCommand(private val plugin: LuckpermsChatPlus): TabExecutor {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): MutableList<String> {
        if (args == null) {
            return mutableListOf()
        }

        if (args.size == 1) {
            return mutableListOf("reload")
        }

        return mutableListOf()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args == null) {
            return false
        }

        if (args.size != 1) {
            return false
        }

        if (args[0] == "reload") {
            plugin.reload()
            sender.sendMessage(
                Component.text("Reloaded config")
                    .color(NamedTextColor.GREEN)
            )
            return true
        }

        return false
    }
}