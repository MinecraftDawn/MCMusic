package mcmusic.file;

import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class PlayMusic implements Runnable {

    private Track track;

    private Long preTick;

    private MidiEvent event;

    private Player player;

    public PlayMusic(Player p, Track track) {

        this.player = p;

        this.track = track;

        this.event = track.get(0);

        this.preTick = event.getTick();
    }

    @Override
    public void run() {

        for (int i = 0; i < track.size(); i++) {

            event = track.get(i);

            MidiMessage eventMsg = event.getMessage();

            if (eventMsg instanceof ShortMessage) {

                ShortMessage sm = (ShortMessage) eventMsg;

                if (sm.getCommand() == 0x90) {
                    int tone = sm.getData1();

                    String sound = "piano.piano";

                    if (tone < 100) {
                        sound += "0" + tone;
                    } else {
                        sound += tone;
                    }

                    player.playSound(player.getLocation(), sound, SoundCategory.MASTER, 1, 1);


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
