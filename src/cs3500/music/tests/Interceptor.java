package cs3500.music.tests;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

/**
 * A reciever compatible with javax.sound.midi.Reciever
 */
public final class Interceptor implements Receiver {
    private final StringBuilder record = new StringBuilder();

    /**
     * Indicates that the application has finished using the receiver, and that limited resources
     * it requires may be released or made available.
     *
     * Clears the record.
     */
    @Override
    public void close() {
       this.record.delete(0, this.record.length());
    }

    /**
     * Sends a MIDI message and time-stamp to this receiver.
     *
     * Time-stamping is not supported for this receiver, and as such the timestamp will be ignored.
     */
    @Override
    public void send(MidiMessage message, long timestamp) {
        byte[] mess = message.getMessage();
        if (mess[0] >= -112 && mess[0] <= -97) { // note on message, any instrument
            record.append("Beat: " + timestamp + ". NOTE ON.  note " + mess[1] + ", velocity " +
                    mess[2] + "\n");
        }
        else if (mess[0] >= -128 && mess[0] <= -113) { // note off message, any instrument
            record.append("Beat: " + timestamp + ". NOTE OFF. note " + mess[1] + "\n");
        }
        else if (mess[0] == -80 && mess[1] == 123) { // all notes off message. no instrument assoc.
            record.append("ALL NOTES OFF \n");
        }
        else { // it is an unsupported operation for the interceptor.
            for (int i = 0; i < mess.length; i += 1) {
                record.append(mess[i] + " ");
            }
            record.append("\n");
        }

        return;
    }

    /**
     * Returns the record of all MIDI messages sent to this reciever.
     */
    @Override
    public String toString() {
        return this.record.toString();
    }
}
