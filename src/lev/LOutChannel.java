/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lev;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Justin Swanson
 */
public class LOutChannel extends LExport {

    FileOutputStream out;
    FileChannel channel;
    Stack<LengthPair> lengthStackTracker = new Stack<>();
    ArrayList<LengthPair> posQueueTracker = new ArrayList<>();

    class LengthPair {

	long pos;
	int size;

	LengthPair(long posi, int sizei) {
	    pos = posi;
	    size = sizei;
	}
    }

    /**
     *
     * @param path Path to open a channel to.
     */
    public LOutChannel(final String path) throws FileNotFoundException {
	super(path);
    }

    /**
     *
     * @param f File to open a channel to.
     */
    public LOutChannel(final File f) throws FileNotFoundException {
	super(f);
    }

    @Override
    public void openOutput(String path) throws FileNotFoundException {
	out = new FileOutputStream(path);
	channel = out.getChannel();
    }

    @Override
    public void write(byte[] array) throws IOException {
	out.write(array);
    }

    @Override
    public void close() throws IOException {
	out.close();
	channel.close();
    }

    public void markLength(int size) throws IOException {
	lengthStackTracker.push(new LengthPair(pos(), size));
	writeZeros(size);
    }

    public void closeLength() throws IOException {
	if (!lengthStackTracker.isEmpty()) {
	    LengthPair last = lengthStackTracker.pop();
	    long curPos = pos();
	    pos(last.pos);
	    write((int) (curPos - last.pos) - last.size, last.size);
	    pos(curPos);
	}
    }

    public void setPosMarker(int size) throws IOException {
	posQueueTracker.add(new LengthPair(pos(), size));
	writeZeros(size);
    }

    public void fillPosMarker() throws IOException {
	if (!posQueueTracker.isEmpty()) {
	    LengthPair last = posQueueTracker.remove(0);
	    long curPos = pos();
	    pos(last.pos);
	    write((int)curPos, last.size);
	    pos(curPos);
	}
    }

    public void pos(long set) throws IOException {
	channel.position(set);
    }

    public long pos() throws IOException {
	return channel.position();
    }
}
