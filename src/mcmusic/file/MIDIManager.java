package mcmusic.file;

import mcmusic.MCMusic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 處理MIDI檔案
 */
public class MIDIManager {
    private String fileName;

    private Plugin plugin = MCMusic.plugin;

    private Sequence sequence;

    private Player player;

    private Float bpm;


    /**
     *
     * @param p 要撥放給哪個玩家聽
     * @param fileName 撥放的音樂檔名稱
     */
    public MIDIManager(Player p, String fileName) {
        this.player = p;

        this.fileName = fileName;

        this.sequence = getSequence();

    }

    /**
     * 開始撥放音樂
     */
    public void PlayMusic() {

        if (sequence == null) {

            player.sendMessage("檔案不存在");
            return;
        }

        int trackNum = getTrackNum();

        Thread[] threads = new Thread[trackNum];

        for (int i = 0; i < trackNum; i++) {
            threads[i] = new Thread(
                    new PlayMusic(
                            player, sequence.getTracks()[i], bpm, new Date().getTime() + 1000));
        }

        Thread threadManager = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < trackNum; i++) {

                    threads[i].start();

                }

                for (int i = 0; i < trackNum; i++) {
                    try {
                        threads[i].join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        threadManager.start();
    }

    /**
     * 取得Sequence
     * @return 回傳取得後的Sequence，如果為null代表取得失敗
     */
    private Sequence getSequence() {

        try {
            File music = new File(plugin.getDataFolder(), fileName + ".mid");

            if (!music.exists()) {
                Bukkit.broadcastMessage("找不到檔案");
            }

            Sequence sequence = MidiSystem.getSequence(music);

            this.bpm = Float.valueOf(100);

            Sequencer se = null;
            try {
                se = MidiSystem.getSequencer();

                se.open();

                se.setSequence(sequence);

                this.bpm = se.getTempoInBPM();

            } catch (MidiUnavailableException e) {
            }

            return sequence;

        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }

        return null;
    }

    /**\
     * 取得有幾個音軌
     * @return 音軌數量
     */
    private int getTrackNum() {
        return sequence.getTracks().length;
    }

}
