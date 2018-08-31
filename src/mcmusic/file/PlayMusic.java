package mcmusic.file;

import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import java.util.Date;

public class PlayMusic implements Runnable {

    private Track track;

    private Long preTick;

    private MidiEvent event;

    private Player player;

    private Long startTime;

    private Float bpm;

    public PlayMusic(Player p, Track track, Float bpm, Long startTime) {

        this.player = p;

        this.track = track;

        this.event = track.get(0);

        this.bpm = bpm;

        this.preTick = event.getTick();

        this.startTime = startTime;
    }

    @Override
    public void run() {
        if (new Date().getTime() - startTime > 0) {

            try {
                Thread.sleep(new Date().getTime() - startTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
                            Thread.sleep((long) ((event.getTick() - preTick)));
//                            Thread.sleep((long) ((event.getTick() - preTick)*60/bpm));
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
