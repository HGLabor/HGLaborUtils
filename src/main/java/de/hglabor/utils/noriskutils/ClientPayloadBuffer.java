package de.hglabor.utils.noriskutils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;

import java.nio.charset.StandardCharsets;

/**
 * Payload buffer to encode or decode
 * client plugin messages
 * Encoding and decoding flow from
 * wiki.vg
 *
 * @version 1.0
 */
public final class ClientPayloadBuffer {  //final because it seems to be convention in here

    /**
     * The bytebuffer to store the bytes
     */
    private final ByteBuf byteBuf;

    /**
     * Constructor to create a buffer out of an existing
     * payload
     *
     * @param payload The payload byte array
     */
    public ClientPayloadBuffer(byte[] payload) {
        this.byteBuf = Unpooled.wrappedBuffer(payload);
    }

    /**
     * Constructor for creating a new
     * buffer being empty
     */
    public ClientPayloadBuffer() {
        //Creating new buffer
        this.byteBuf = Unpooled.buffer();
    }

    /**
     * Getter for the byte buf
     * because it's needed to send it to the player
     *
     * @return The bytebuf of the buffer
     */
    public ByteBuf getByteBuf() {
        return this.byteBuf;
    }

    /**
     * Writing a string into the buffer
     *
     * @param stringToWrite The string to write
     */
    public void writeString(String stringToWrite) {
        byte[] stringBytes = stringToWrite.getBytes(StandardCharsets.UTF_8);

        if (stringBytes.length > Short.MAX_VALUE) {
            throw new EncoderException("String was too big");
        }
        this.writeLengthVarInt(stringBytes.length);
        this.byteBuf.writeBytes(stringBytes);
    }

    /**
     * Writing the length as varint into the buffer
     * code of minecraft protocol
     *
     * @param length The length of the string byte
     */
    private void writeLengthVarInt(int length) {
        while ((length & -128) != 0) {
            this.byteBuf.writeByte(length & 127 | 128);
            length >>>= 7;
        }
        this.byteBuf.writeByte(length);
    }

    /**
     * Reading the length out of a varInt
     * using mc protocol decoding
     *
     * @return The length
     */
    private int readLengthVarInt() {
        int readCount = 0;
        int result = 0;
        byte readByte;

        do {
            readByte = this.byteBuf.readByte();
            result |= (readByte & 127) << readCount++ * 7;
            if (readCount > 5) {
                throw new DecoderException("Invalid war int size");
            }
        } while ((readByte & 128) == 128);

        return result;
    }

    /**
     * Reading a string out of
     * the byte buf
     *
     * @return The read string
     */
    public String readString() {
        int stringByteArrayLength = this.readLengthVarInt();

        if (stringByteArrayLength < 0 || stringByteArrayLength > 4 * Short.MAX_VALUE) {
            throw new DecoderException("Invalid string length");
        }
        byte[] stringBytes = new byte[stringByteArrayLength];
        this.byteBuf.readBytes(stringBytes);

        return new String(stringBytes, StandardCharsets.UTF_8);
    }
}
