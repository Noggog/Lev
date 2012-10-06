package lev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A FileChannel setup that supports easy extraction/getting of information.
 *
 * @author Justin Swanson
 */
public class LFileChannel extends LChannel {

    FileInputStream iStream;
    FileChannel iChannel;
    long end;

    /**
     *
     */
    public LFileChannel() {
    }

    /**
     *
     * @param path Path to open a channel to.
     * @throws IOException
     */
    public LFileChannel(final String path) {
	openFile(path);
    }

    /**
     *
     * @param f File to open a channel to.
     * @throws IOException
     */
    public LFileChannel(final File f) {
	openFile(f);
    }

    /**
     *
     * @param rhs
     * @param allocation
     * @throws IOException
     */
    public LFileChannel(LFileChannel rhs, long allocation) {
	LFileChannel fc = (LFileChannel) rhs;
	iStream = fc.iStream;
	iChannel = fc.iChannel;
	end = pos() + allocation;
    }

    /**
     *
     * @param path Path to open a channel to.
     * @throws IOException 
     */
    final public void openFile(final String path) {
	try {
	    iStream = new FileInputStream(path);
	    iChannel = iStream.getChannel();
	    end = iChannel.size();
	} catch (IOException ex) {
	    Logger.getLogger(LFileChannel.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    /**
     *
     * @param f File to open a channel to.
     * @throws IOException
     */
    final public void openFile(final File f) {
	openFile(f.getPath());
    }

    /**
     * Reads in a byte and moves forward one position in the file.
     *
     * @return The next int in the file.
     * @throws IOException
     */
    @Override
    final public int read(){
	try {
	    return iStream.read();
	} catch (IOException ex) {
	    Logger.getLogger(LFileChannel.class.getName()).log(Level.SEVERE, null, ex);
	}
	return -1;
    }

    /**
     * Reads in the desired bytes and wraps them in a ByteBuffer.
     *
     * @param skip Bytes to skip
     * @param read Bytes to read and convert
     * @return ByteBuffer containing read bytes.
     * @throws IOException
     */
    final public ByteBuffer extractByteBuffer(int skip, int read) {
	super.skip(skip);
	ByteBuffer buf = ByteBuffer.allocate(read);
	try {
	    iChannel.read(buf);
	} catch (IOException ex) {
	    Logger.getLogger(LFileChannel.class.getName()).log(Level.SEVERE, null, ex);
	}
	buf.flip();
	return buf;
    }

    /**
     *
     * @param pos Position to move to.
     * @throws IOException
     */
    @Override
    final public void pos(long pos) {
	try {
	    iChannel.position(pos);
	} catch (IOException ex) {
	    Logger.getLogger(LFileChannel.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    /**
     *
     * @return Current position.
     * @throws IOException
     */
    @Override
    final public long pos() {
	try {
	    return iChannel.position();
	} catch (IOException ex) {
	    Logger.getLogger(LFileChannel.class.getName()).log(Level.SEVERE, null, ex);
	}
	return -1;
    }

    /**
     * Closes streams.
     *
     * @throws IOException
     */
    @Override
    final public void close() {
	if (iStream != null) {
	    try {
		iStream.close();
		iChannel.close();
	    } catch (IOException ex) {
		Logger.getLogger(LFileChannel.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    /**
     *
     * @return Bytes left to read in the file.
     * @throws IOException
     */
    @Override
    final public int available() {
	return (int) (end - pos());
    }

    /**
     *
     * @return
     * @throws IOException
     */
    @Override
    public Boolean isDone() {
	return pos() == end;
    }

    /**
     *
     * @param amount
     * @return
     * @throws IOException
     */
    @Override
    public byte[] extract(int amount) {
	ByteBuffer allocate = ByteBuffer.allocate(amount);
	try {
	    iChannel.read(allocate);
	} catch (IOException ex) {
	    Logger.getLogger(LFileChannel.class.getName()).log(Level.SEVERE, null, ex);
	}
	return allocate.array();
    }

    @Override
    public byte[] extractUntil(int delimiter) {
	int counter = 1;
	while (!isDone()) {
	    if (read() != delimiter) {
		counter++;
	    } else {
		jumpBack(counter);
		byte[] out = extract(counter - 1);
		skip(1);
		return out;
	    }
	}
	return new byte[0];
    }
}
