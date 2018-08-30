package mcmusic.file;

import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class TestMIDI implements Runnable {
    private int num;

    public TestMIDI(int num) {
        this.num = num;
    }

    @Override
    public void run() {
        Sequence sequence = null;
        try {
            sequence = MidiSystem.getSequence(new File("打上花火.mid"));
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Track track = sequence.getTracks()[num];
        long preTick;
        MidiEvent event = track.get(0);
        preTick = event.getTick();

        for (int i = 0; i < track.size(); i++) {
            event = track.get(i);

            MidiMessage eventMsg = event.getMessage();
            if (eventMsg instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) eventMsg;

                if (sm.getCommand() == 0x90) {
                    int key = sm.getData1();

                    String sound = "piano.piano";

                    if (key < 100) {
                        sound += "0" + key;
                    } else {
                        sound += key;
                    }

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), sound, SoundCategory.MASTER, 1, 1);
                    }

                    try {
                        long sleep = event.getTick() - preTick;

                        if (sleep > 0) {
                            Thread.sleep(event.getTick() - preTick);
                        }

                        preTick = event.getTick();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}
