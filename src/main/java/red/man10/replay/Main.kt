package red.man10.replay

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.projectiles.ProjectileSource
import org.bukkit.scheduler.BukkitRunnable
import red.man10.camera.Command
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread


class Main : JavaPlugin() ,Listener {

    companion object {
        val version = "2023/4/10"
        val prefix = "§6[§dMan10Replay§6]§f"
        lateinit var plugin: JavaPlugin
        var duration = 900
        var enabled = true
        var lastMin = -1
        var minList = mutableListOf<Int>()
    }

    override fun onEnable() {
        plugin = this
        saveDefaultConfig()
        loadConfig()

        plugin.server.pluginManager.registerEvents(this, plugin)
        getCommand("mreplay")!!.setExecutor(Command)
        // 1tick毎のタスクを設定する
        Bukkit.getServer().scheduler.scheduleSyncRepeatingTask(this, {

            if(!enabled){
                return@scheduleSyncRepeatingTask
            }
            // 59分から00分ちょうどの切り替わりに処理を行う
            val min = Calendar.getInstance().get(Calendar.MINUTE)
            if(min == lastMin){
                return@scheduleSyncRepeatingTask
            }

            // minListに一致する分になったら記録を開始する
            if(minList.contains(min)){
                // 現在の時刻でyyyymmdd_hh という形式でファイル名を作成する　month,day,hoursは01,02という形式になる
                val date = Calendar.getInstance()
                val fileName = String.format("%04d%02d%02d_%02d%02d"
                    ,date.get(Calendar.YEAR)
                    ,date.get(Calendar.MONTH)+1
                    ,date.get(Calendar.DAY_OF_MONTH)
                    ,date.get(Calendar.HOUR_OF_DAY)
                    ,min
                )

                info("記録開始 $fileName")
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"replay start ${fileName}:${duration}")
            }

            lastMin = min
        }, 0L, 20L)


        info("Man10 Replay Plugin Enabled")
    }
    private fun loadConfig(){
        reloadConfig()
        enabled =  config.getBoolean("enabled")
        duration = config.getInt("duration")
        minList = config.getIntegerList("minList").toMutableList()
    }

}
public fun updateConfig(){
    Main.plugin.config.set("enabled", Main.enabled)
    Main.plugin.config.set("duration", Main.duration)
    Main.plugin.saveConfig()
}

// 通常ログ
fun info(message:String,sender:CommandSender? = null){
    Bukkit.getLogger().info(Main.prefix+message)
    sender?.sendMessage(message)
}
// エラーログ
fun error(message:String,sender:CommandSender? = null) {
    Bukkit.getLogger().severe(Main.prefix+message)
    sender?.sendMessage(message)
}

