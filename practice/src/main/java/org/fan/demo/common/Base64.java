package org.fan.demo.common;

/**
 * base64算法
 * base64算法概述：
 * 1）将给定的字符串转换成对应的字符编码（如：GBK、UTF-8）
 *
 * 2）将获得该字符编码转换成二进制码
 *
 * 3）对获得的二进制码进行分组操作
 *
 * 第一步：每3个字节（8位二进制）为一组，一共24个二进制位
 *
 * 第二步：将这个24个二进制位分成4组，每个组有6个二进制位，不足6位的，后面补0。
 *
 * 第三步：在每个组前面加两个0，这样每个组就又变成了8位，即每个组一个字节，4个组就4个字节了。
 * 字节数不能被3整除的，在后面加=号，需要补几个字节就加几个等号
 * @version 1.0
 * @author: Fan
 * @date 2020.7.6 10:51
 */
public class Base64 {

    private static final char[] toBase64 = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/','='
    };

    static private final byte[] codes = new byte[256];
    static {
        for (int i = 0; i < 256; i++)
            codes[i] = -1;
        for (int i = 'A'; i <= 'Z'; i++)
            codes[i] = (byte) (i - 'A');
        for (int i = 'a'; i <= 'z'; i++)
            codes[i] = (byte) (26 + i - 'a');
        for (int i = '0'; i <= '9'; i++)
            codes[i] = (byte) (52 + i - '0');
        codes['+'] = 62;
        codes['/'] = 63;
    }

    private static byte[] encode(byte[] data) {
        int length = data.length;
        int outLength = (length + 2) / 3 * 4;
        byte[] out = new byte[outLength];
        for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
            boolean quad = false;
            boolean trip = false;
            int val = (0xFF & (int) data[i]);
            val <<= 8;
            if ((i + 1) < data.length) {
                val |= (0xFF & (int) data[i + 1]);
                trip = true;
            }
            val <<= 8;
            if ((i + 2) < data.length) {
                val |= (0xFF & (int) data[i + 2]);
                quad = true;
            }
            out[index + 3] = (byte)toBase64[(quad ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 2] = (byte)toBase64[(trip ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 1] = (byte)toBase64[val & 0x3F];
            val >>= 6;
            out[index] = (byte)toBase64[val & 0x3F];
        }
        return out;
    }

    /**
     * 将base64编码的数据解码成原始数据
     */
    static public byte[] decode(char[] data)
    {
        int len = ((data.length + 3) / 4) * 3;
        if(data.length > 0 && data[data.length - 1] == '=')
            --len;
        if(data.length > 1 && data[data.length - 2] == '=')
            --len;
        byte[] out = new byte[len];
        int shift = 0;
        int accum = 0;
        int index = 0;
        for(int ix = 0; ix < data.length; ix++)
        {
            int value = codes[data[ix] & 0xFF];
            if(value >= 0)
            {
                accum <<= 6;
                shift += 6;
                accum |= value;
                if(shift >= 8)
                {
                    shift -= 8;
                    out[index++] = (byte)((accum >> shift) & 0xff);
                }
            }
        }
        if(index != out.length)
            throw new Error("miscalculated data length!");
        return out;
    }
}
