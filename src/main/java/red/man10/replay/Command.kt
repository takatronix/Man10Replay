package red.man10.camera

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import red.man10.replay.Main
import red.man10.replay.updateConfig


object Command : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if(!sender.hasPermission("red.man10.replay.op")){
            sender.sendMessage("このコマンドを実行する権限がない")
            return  false
        }

        if(args.isEmpty()){
            showHelp(label,sender)
            return true
        }

        when(args[0]){
            "help" -> showHelp(label,sender)
            "on" -> {
                Main.enabled = true
                updateConfig()
                sender.sendMessage("§a記録を開始します")
            }
            "off"-> {
                Main.enabled = false
                updateConfig()
                sender.sendMessage("§a記録を停止します")
            }
        }

        return false
    }


    // ヘルプメッセージ
    private fun showHelp(label:String,sender: CommandSender){
        sender.sendMessage("§b===============[Man10 Replay System ver.${Main.version}]====================")


        sender.sendMessage("§b=======[Author: takatronix /  https://man10.red]=============")
    }

    // タブ補完
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>?): List<String>? {

        return null
    }

}