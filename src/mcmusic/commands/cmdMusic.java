package mcmusic.commands;

import mcmusic.file.GetMusic;
import mcmusic.file.MIDIManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * 處理指令部分
 * 如果有兩個以上參數，就可以播歌
 * 第三個參數為歌名
 */
public class cmdMusic implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;

        if (args.length == 0) {
            Thread t = new Thread(new GetMusic("星球墜落", p));
            t.setPriority(Thread.NORM_PRIORITY);
            t.start();

        } else if (args.length == 2) {
            GetMusic.Set(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        } else if (args.length > 2) {

            MIDIManager midiManager = new MIDIManager(p, args[2]);

            midiManager.PlayMusic();
        }


        return true;
    }
}
