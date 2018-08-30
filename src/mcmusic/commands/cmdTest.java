package mcmusic.commands;

import mcmusic.file.GetMusic;
import mcmusic.file.TestMIDI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdTest implements CommandExecutor {


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
            Thread t1 = new Thread(new TestMIDI(0));
            Thread t2 = new Thread(new TestMIDI(1));
            Thread threadManager = new Thread(new Runnable() {
                @Override
                public void run() {
                    t1.start();
                    t2.start();
                    try {
                        t1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        t2.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            threadManager.setPriority(Thread.NORM_PRIORITY);
            threadManager.start();

        }


        return true;
    }
}
