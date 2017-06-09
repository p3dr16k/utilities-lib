package org.pedrick.util;


import java.nio.ByteBuffer;

/*=======================================================================

*FILE:         ByteUtil.java
*
*DESCRIPTION:  Class for primitive/bytes conversion
*REQUIREMENTS: 
*AUTHOR:       Patrick Facco
*VERSION:      1.0
*CREATED:      5-nov-2014
*LICENSE:      GNU/GPLv3
*COPYRIGTH:    Patrick Facco 2014
*This program is free software: you can redistribute it and/or modify
*it under the terms of the GNU General Public License as published by
*the Free Software Foundation, either version 3 of the License, or
*(at your option) any later version.
*
*This program is distributed in the hope that it will be useful,
*but WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*GNU General Public License for more details.
*
*You should have received a copy of the GNU General Public License
*along with this program.  If not, see <http://www.gnu.org/licenses/>.
*========================================================================
* 
*/

public class ByteUtil
{
    public static final int NUM_BYTES_IN_SHORT = 2;
    public static final int NUM_BYTES_IN_INT = 4;
    public static final int NUM_BYTES_IN_LONG = 8;
    

    /**
     * Short to bytes conversion
     * @param s the short to convert
     * @return an array of 2 bytes that contains the short rapresentation
     */
    public static byte[] shortToBytes(short s)
    {

            ByteBuffer byteBuffer = ByteBuffer.allocate(NUM_BYTES_IN_SHORT);
            byteBuffer.putShort(s);
            return byteBuffer.array();

    }

    /**
     * bytes to short conversion
     * @param bytes the array to convert
     * @return the short rapresentation of array
     */
    public static short bytesToShort(byte[] bytes)
    {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return byteBuffer.getShort();
    }

    /**
     * Short to a hexadecimal conversion
     * @param s the short to convert
     * @return an hexadecimal rapresentation of the short
     */
    public static String shortToHex(short s)
    {
        int i = (int) s;
        return Integer.toHexString(i);
    }

    /**
     * Hexadecimal to short conversion 
     * @param s the hexadecimal value as a String
     * @return a short rapresentation of the hexadecimal value
     */
    public static short hexToShort(String s)
    {
        return Short.parseShort(s, 16);
    }

    /**
     * Convert a Java int to a 4-byte array.
     *
     * @param i
     *            A Java int value.
     * @return A 4-byte array representing the int value.
     */
    public static byte[] intToBytes(int i)
    {

            ByteBuffer byteBuffer = ByteBuffer.allocate(NUM_BYTES_IN_INT);
            byteBuffer.putInt(i);
            return byteBuffer.array();

    }

    /**
     * Convert a byte array to a Java int.
     *
     * @param bytes
     *            A byte array.
     * @return A Java int.
     */
    public static int bytesToInt(byte[] bytes)
    {

            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            return byteBuffer.getInt();

    }

    /**
     * Convert an int to a hex representation.
     *
     * @param i A Java int.
     * @return A hex representation of the int.
     */
    public static String intToHex(int i)
    {

            return Integer.toHexString(i);

    }

    /**
     * Convert a hex representation to a Java int.
     *
     * @param s
     *            A hex representation.
     * @return A Java int.
     */
    public static int hexToInt(String s)
    {

            return Integer.parseInt(s, 16);

    }

    /**
     * Convert a Java long to a 4-byte array.
     *
     * @param l
     *            A Java long value.
     * @return A 4-byte array representing the int value.
     */
    public static byte[] longToBytes(long l)
    {

            ByteBuffer byteBuffer = ByteBuffer.allocate(NUM_BYTES_IN_LONG);
            byteBuffer.putLong(l);
            return byteBuffer.array();

    }

    /**
     * Convert a byte array to a Java long.
     *
     * @param bytes
     *            A byte array.
     * @return A Java long.
     */
    public static long bytesToLong(byte[] bytes)
    {

            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            return byteBuffer.getLong();

    }

    /**
     * Convert a long to a hex representation.
     *
     * @param l A Java long.
     * @return A hex representation of the long.
     */
    public static String longToHex(long l)
    {

            return Long.toHexString(l);

    }

    /**
     * Convert a hex representation to a Java long.
     *
     * @param s
     *            A hex representation.
     * @return A Java long.
     */
    public static long hexToLong(String s) {

            return Long.parseLong(s, 16);

    }

    /**
     * Get a byte array in a printable binary form.
     *
     * @param bytes
     *            The byte to be writen.
     * @return A String reprentation of the byte.
     */
    public static String writeBytes(byte[] bytes)
    {

            StringBuilder stringBuffer = new StringBuilder();

            for (int i = 0; i < bytes.length; i++)
            {

                    // New line every 4 bytes
                    if (i % 4 == 0)
                    {

                            stringBuffer.append("\n");
                    }

                    stringBuffer.append(writeBits(bytes[i])).append(" ");
            }

            return stringBuffer.toString();

    }

    /**
     * Get a byte array in a printable binary form.
     *
     * @param bytes
     *            The byte to be writen.
     * @param packetLength
     * @return A String reprentation of the byte.
     */
    public static String writeBytes(byte[] bytes, int packetLength)
    {

            StringBuilder stringBuffer = new StringBuilder();

            for (int i = 0; i < packetLength; i++)
            {

                    // New line every 4 bytes
                    if (i % 4 == 0)
                    {

                            stringBuffer.append("\n");
                    }

                    stringBuffer.append(writeBits(bytes[i])).append(" ");

            }

            return stringBuffer.toString();

    }

    /**
     * Get a byte in a printable binary form.
     *
     * @param b
     *            The byte to be writen.
     * @return A String reprentation of the byte.
     */
    public static String writeBits(byte b)
    {

            StringBuilder stringBuffer = new StringBuilder();
            int bit = 0;

            for (int i = 7; i >= 0; i--)
            {

                    bit = (b >>> i) & 0x01;
                    stringBuffer.append(bit);

            }

            return stringBuffer.toString();

    }

}