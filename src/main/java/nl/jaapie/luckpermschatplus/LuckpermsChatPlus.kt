package nl.jaapie.luckpermschatplus

import net.luckperms.api.LuckPerms
import org.bukkit.plugin.java.JavaPlugin

class LuckpermsChatPlus: JavaPlugin() {
    lateinit var luckperms: LuckPerms
        private set
    lateinit var config: LPCPConfig
        private set
    override fun onEnable() {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }

        this.luckperms = server.servicesManager.load(LuckPerms::class.java)
            ?: throw IllegalStateException("LuckPerms not found")

        this.config = LPCPConfig.read(dataFolder.toPath().resolve("config.toml"))

        server.pluginManager.registerEvents(LPCPListener(this), this)

        getCommand("lpcp")?.setExecutor(LPCPCommand(this))
    }

    fun reload() {
        this.config = LPCPConfig.read(dataFolder.toPath().resolve("config.toml"))
    }
}