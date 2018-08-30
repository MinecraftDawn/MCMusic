package mcmusic.file;

import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

public class GetMusic implements Runnable {
    private static YMLManager yml = YMLManager.getInstance();

    private static YamlConfiguration data = yml.data;

    private String musicName;
    private Player player;

    public GetMusic(String name, Player p) {
        this.musicName = name;
        this.player = p;
    }


    public static void Set(int tone, int delay) {
        yml.loadData();
        int lenth = data.getConfigurationSection(yml.str2DotStr("星球墜落", "樂曲")).getKeys(false).size();

        data.set(yml.str2DotStr("星球墜落", "樂曲", Integer.toString(lenth), "delay"), delay);
        data.set(yml.str2DotStr("星球墜落", "樂曲", Integer.toString(lenth), "音調"), tone);

        yml.saveData();

    }


    @Override
    public void run() {
        yml.loadData();
        yml.saveData();
        for (String str : data.getKeys(false)) {

            if (str.equals(musicName)) {

                Set<String > music = data.getConfigurationSection(yml.str2DotStr(str, "樂曲")).getKeys(false);

                Double tempo = data.getDouble(yml.str2DotStr(str, "節奏"));
                tempo = 60 / tempo;
                tempo = tempo / 4;
                tempo = tempo * 1000;

                for (String s : music) {
                    if(! data.isSet(yml.str2DotStr(str, "樂曲", s))) continue;

                    Integer tone = data.getInt(yml.str2DotStr(str, "樂曲", s, "音調"));
                    Integer delay = data.getInt(yml.str2DotStr(str, "樂曲", s, "delay"));

                    String sound = "piano.piano";

                    if (tone < 100) {
                        sound += "0" + tone;
                    } else {
                        sound += tone;
                    }

                    player.playSound(player.getLocation(), sound, SoundCategory.MASTER, 1, 1);

                    try {
                        Thread.sleep((long) (delay * tempo));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }
}
