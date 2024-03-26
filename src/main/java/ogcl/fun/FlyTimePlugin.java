package ogcl.fun;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class FlyTimePlugin extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        getLogger().info("FlyTimePlugin已启用"); // 添加日志输出
        // 注册命令执行器
        this.getCommand("flytime").setExecutor(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("FlyTimePlugin已禁用"); // 添加日志输出
        // 插件关闭逻辑
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        getLogger().info("收到命令：" + cmd.getName()); // 添加日志输出
        if (cmd.getName().equalsIgnoreCase("flytime")) {
            if (!sender.isOp() && !(sender instanceof Player && sender.hasPermission("flytime.use"))) {
                sender.sendMessage("您没有权限使用该命令！");
                return true;
            }

            if (args.length != 2) {
                sender.sendMessage("用法：/flytime <分钟数> <玩家名>");
                return true;
            }

            int minutes;
            try {
                minutes = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage("错误：时间必须是数字。");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("玩家未找到！");
                return true;
            }

            target.setAllowFlight(true);
            sender.sendMessage("玩家 " + target.getName() + " 现在可以飞行 " + minutes + " 分钟。");

            new BukkitRunnable() {
                @Override
                public void run() {
                    target.setAllowFlight(false);
                    target.sendMessage("你的飞行权限已关闭。");
                }
            }.runTaskLater(this, minutes * 60 * 20); // 20 ticks = 1 second

            return true;
        }
        return false;
    }
}
