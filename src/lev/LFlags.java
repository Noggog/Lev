package lev;

import java.io.Serializable;

/**
 * An object that is meant to hold a set of boolean flags.
 * Takes in byte arrays and converts each bit to its own flag.
 * @author Justin Swanson
 */
public class LFlags implements Serializable {

    byte[] flags;

    /**
     *
     * @param size number of bytes-worth of flags to initialize.
     */
    public LFlags(int size) {
        flags = new byte[size];
    }

    /**
     *
     * @param inFlags bytes to initialize flags to.
     */
    public LFlags(byte[] inFlags) {
        set(inFlags);
    }

    public LFlags(LFlags rhs) {
	flags = new byte[rhs.flags.length];
	System.arraycopy(rhs.flags, 0, flags, 0, rhs.flags.length);
    }

    /**
     * Resizes LFlags to contain bytes and their associated flags
     * @param inFlags bytes to set LFlags to.
     */
    public final void set(byte[] inFlags) {
        flags = inFlags;
    }

    /**
     *
     * @param bit Bit/Flag to check
     * @return True if bit/Flag is on
     */
    public final boolean get(int bit) {
	byte byt = flags[bit / 8];
	return ((byt >>> (bit % 8)) & 1) != 0;
    }

    /**
     *
     * @param bit Bit/Flag to check
     * @param on Sets the bit/flag on/off
     */
    public final void set(int bit, boolean on) {
	int index = bit / 8;
	flags[index] = (byte) (on
		? flags[index] | (1 << (bit % 8))
		: flags[index] & ~(1 << (bit % 8)));

    }

    /**
     * Converts the boolean flags to a byte array.
     * @return Byte array containing all the flags as bits.
     */
    public final byte[] export() {
        return flags;
    }

    /**
     *
     * @return Length of the byte array representation
     */
    public final int length() {
        return flags.length;
    }

    /**
     * Sets all flags to false.
     */
    public final void clear() {
        for (int i = 0; i < flags.length; i++) {
            flags[i] = 0;
        }
    }

    /**
     *
     * @return True if all flags are set to false
     */
    public boolean isZeros() {
	for (byte b : flags) {
	    if (b != 0) {
		return false;
	    }
	}
	return true;
    }

    /**
     *
     * @return String of 1's and 0's.  Beep boop beep.
     */
    @Override
    public final String toString() {
        String out = "";
        for (byte b : flags) {
            out += Integer.toBinaryString(b);
        }
        return out;
    }
}
