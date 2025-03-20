package love.forte.net.i2p.crypto.eddsa

import kotlin.experimental.xor

/**
 * Constant-time byte comparison.
 *
 * ```Java
 * public static int equal(int b, int c) {
 *     int result = 0;
 *     int xor = b ^ c;
 *     for (int i = 0; i < 8; i++) {
 *         result |= xor >> i;
 *     }
 *     return (result ^ 0x01) & 0x01;
 * }
 * ```
 *
 * @receiver a byte
 * @param c a byte
 * @return `1` if b and c are equal, `0` otherwise.
 */
internal infix fun Int.constantTimeEqual(c: Int): Int {
    var result = 0
    val xor = this xor c
    for (i in 0 until 8) {
        result = result or (xor shr i)
    }
    return (result xor 0x01) and 0x01
}

/**
 * Constant-time determine if byte is negative.
 *
 * ```Java
 * public static int negative(int b) {
 *     return (b >> 8) & 1;
 * }
 * ```
 * @receiver the byte to check.
 * @return `1` if the byte is negative, `0` otherwise.
 */
internal fun Int.constantTimeNegative(): Int = (this shr 8) and 1

/**
 * Constant-time byte array comparison.
 *
 * ```Java
 * public static int equal(byte[] b, byte[] c) {
 *     int result = 0;
 *     for (int i = 0; i < 32; i++) {
 *         result |= b[i] ^ c[i];
 *     }
 *
 *     return equal(result, 0);
 * }
 * ```
 *
 * @param c a byte[]
 * @return `1` if b and c are equal, 0 otherwise.
 */
internal infix fun ByteArray.constantTimeEqual(c: ByteArray): Int {
    var result: Int = 0
    for (i in 0..31) {
        result = result or (this[i] xor c[i]).toInt()
    }
    return result constantTimeEqual 0
}

/**
 * Get the [i]'th bit of a byte array.
 *
 * ```Java
 * public static int bit(byte[] h, int i) {
 *     return (h[i >> 3] >> (i & 7)) & 1;
 * }
 * ```
 *
 * @receiver the byte array.
 * @param i the bit index.
 * @return `0` or `1`, the value of the [i]'th bit in [this]
 */
internal infix fun ByteArray.bit(i: Int): Int {
    return (this[i shr 3].toInt() ushr (i and 7)) and 1
}

/**
 * Converts a hex string to bytes.
 *
 * ```Java
 * public static byte[] hexToBytes(String s) {
 *     int len = s.length();
 *     if (len % 2 != 0) {
 *         throw new IllegalArgumentException("Hex string must have an even length");
 *     }
 *     byte[] data = new byte[len / 2];
 *     for (int i = 0; i < len; i += 2) {
 *         data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
 *                 + Character.digit(s.charAt(i+1), 16));
 *     }
 *     return data;
 * }
 * ```
 *
 * @receiver the hex string to be converted.
 * @return the bytes
 */
internal fun String.hexToBytes(): ByteArray {
    val len = length
    require(len % 2 == 0) {
        "Hex string must have an even length"
    }

    val data = ByteArray(len / 2)
    for (i in 0 until len step 2) {
        data[i / 2] = ((this[i].digitToInt(16) shl 4)
                + this[i + 1].digitToInt(16)).toByte()
    }

    return data
}

/**
 * Converts bytes to a hex string.
 *
 * ```Java
 * public static String bytesToHex(byte[] raw) {
 *     if ( raw == null ) {
 *         return null;
 *     }
 *     final StringBuilder hex = new StringBuilder(2 * raw.length);
 *     for (final byte b : raw) {
 *         hex.append(Character.forDigit((b & 0xF0) >> 4, 16))
 *         .append(Character.forDigit((b & 0x0F), 16));
 *     }
 *     return hex.toString();
 * }
 * ```
 *
 * @receiver the byte[] to be converted.
 * @return the hex representation as a string.
 */
internal fun ByteArray.bytesToHex(): String {
    val hex = StringBuilder(2 * size)
    for (b in this) {
        hex.append(((b.toInt() and 0xF0) ushr 4).digitToChar(16))
            .append((b.toInt() and 0x0F).digitToChar(16))
    }
    return hex.toString()
}
