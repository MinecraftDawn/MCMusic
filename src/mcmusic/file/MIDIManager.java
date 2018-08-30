package mcmusic.file;

import mcmusic.MCMusic;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MIDIManager {
    private String fileName;

    private Plugin plugin = MCMusic.plugin;

    private Sequence sequence;

    private Player player;


    public MIDIManager(Player p, String fileName) {
        this.player = p;

        this.fileName = fileName;

        this.sequence = getSequence();

        if (sequence == null) {

            p.sendMessage("檔案不存在");

        } else {
            PlayMusic();
        }
    }

    private void PlayMusic(){

        int trackNum = getTrackNum();

        Thread[] threads = new Thread[trackNum];

        for (int i = 0; i < trackNum; i++) {
            threads[i] = new Thread(
                    new PlayMusic(player,sequence.getTracks()[i],new Date().getTime()+500));
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

    private Sequence getSequence() {

        try {
            File music = new File(plugin.getDataFolder(), fileName + ".mid");

            Sequence sequence = MidiSystem.getSequence(music);

            return sequence;

        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private int getTrackNum() {
        return sequence.getTracks().length;
    }

    private void playMusic(){

    }

}
