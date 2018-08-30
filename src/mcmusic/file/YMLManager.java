package mcmusic.file;

import mcmusic.MCMusic;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 *負責處理yml檔
 */
public class YMLManager {

    private Plugin plugin;

    private static YMLManager instance;

    private File file;

    public YamlConfiguration data;

    //建構子(Constructor)，設定Backpack.yml檔案
    private YMLManager() {
        plugin = MCMusic.plugin;

        file = new File(plugin.getDataFolder(), "music.yml");

        if (!file.exists()) {
            plugin.saveResource("music.yml", false);
        }

        data = new YamlConfiguration();

        loadData();
        saveData();

    }

    /**
     * 單例模式，只產生一個實例，若其他Class需要使用yml，則需要取得此I實例
     * @return 回傳出一個YMLManager實例
     */
    //單例模式，只產生一個實例
    public static YMLManager getInstance() {
        if (instance == null) {
            synchronized (YMLManager.class) {
                instance = new YMLManager();
            }

        }
        return instance;
    }

    /**
     * 重新讀取yml檔案
     * @return 是否成功讀取，成功為true，失敗為false
     */
    //讀取Backpack.yml檔案內容
    public Boolean loadData() {
        try {
            data.load(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 儲存修改後的yml檔案
     * @return 是否成功儲存，成功為true，失敗為false
     */
    //儲存Backpack.yml檔案內容
    public Boolean saveData() {
        try {
            data.save(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //將每個args之間插入"."，以符合yml索引方式
    public static String str2DotStr(String... args) {
        String str = "";

        if (args.length > 1) {
            for (int i = 0; i < args.length - 1; i++) {
                str += args[i] + ".";
            }
        }

        str += args[args.length - 1];

        return str;
    }

}