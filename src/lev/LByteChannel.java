/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lev;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Justin Swanson
 */
public class LByteChannel extends LChannel {

    byte[] input;
    int pos;
    int end;

    /**
     *
     */
    public LByteChannel() {
    }

    /**
     *
     * @param input 
     * @throws FileNotFoundException
     */
    public LByteChannel(final byte[] input) {
	openStream(input);
    }

    /**
     *
     * @param input
     */
    public final void openStream(byte[] input) {
	this.input = input;
	pos = 0;
	end = input.length;
    }

    /**
     *
     * @param in
     * @throws IOException
     */
    public final void openStream(LShrinkArray in) {
	openStream(in.extractAllBytes());
    }

    /**
     *
     * @return
     * @throws IOException
     */
    @Override
    public int read() {
	return Ln.bToUInt(input[pos++]);
    }

    /**
     *
     * @param pos
     * @throws IOException
     */
    @Override
    public void pos(long pos) {
	this.pos = (int) pos;
    }

    /**
     *
     * @return
     * @throws IOException
     */
    @Override
    public long pos() {
	return pos;
    }

    /**
     *
     * @throws IOException
     */
    @Override
    public void close() {
	input = null;
    }

    /**
     *
     * @return
     * @throws IOException
     */
    @Override
    public int available() {
	return end - pos;
    }

    /**
     *
     * @return
     * @throws IOException
     */
    @Override
    public Boolean isDone() {
	return pos == end;
    }

    @Override
    public void skip(int skip) {
	pos += skip;
    }

    /**
     *
     * @param amount
     * @throws IOException
     */
    @Override
    public void jumpBack(int amount) {
	skip(-amount);
    }

    /**
     *
     * @param read
     * @return
     * @throws IOException
     */
    @Override
    public byte[] extract(int read) {
	byte[] out = new byte[read];
	for (int i = 0 ; i < read ; i++) {
	    out[i] = input[pos + i];
	}
	skip(read);
	return out;
    }

}
