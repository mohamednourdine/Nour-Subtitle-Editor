package nour.subtitle.editor.dictionary;

public class GoogleTK {
    public static String TL(String a) {
        long b = 406644L;
        long b1 = 3293161072L;
        long b2;
        int[] e = new int[a.getBytes().length];
        int f = 0;
        int g = 0;
        for (; g < a.length(); g++) {
            int m = a.charAt(g);
            if (123 > m) {
                e[f++] = m;
            } else {

                if (2048 > m) {
                    e[f++] = m >> 6 | 192;
                } else {
                    if (55296 == (m & 64512) && g + 1 < a.length() && 56320 == (a.charAt(g + 1) & 64512)) {
                        m = 65536 + ((m & 1023) << 10) + (a.charAt(++g) & 1023);
                        e[f++] = m >> 18 | 240;
                        e[f++] = m >> 12 & 63 | 128;
                    } else {
                        e[f++] = m >> 12 | 224;
                        e[f++] = m >> 6 & 63 | 128;
                    }
                    e[f++] = m & 63 | 128;
                }

            }
        }

        b2 = b;
        f = 0;
        for (; f < e.length; f++) {
            b2 += e[f];
            b2 = RL(b2, "+-a^+6");
        }
        b2 = RL(b2, "+-3^+b+-f");
        b2 ^= b1;
        if (0 > b2) {
            b2 = (b2 & 2147483647) + 2147483648L;
        }
        b2 %= 1E6;
        return b2 + "." + (b2 ^ b);
    }

    private static long RL(long a, String b) {
        int c = 0;
        long temp_d;
        for (; c < b.length() - 2; c += 3) {
            char d = b.charAt(c + 2);
            temp_d = d;
            if (d >= "a".charAt(0)) {
                temp_d = temp_d - 87;
            } else {
                temp_d = temp_d - 48;
            }
            if (b.charAt(c + 1) == "+".charAt(0)) {
                temp_d = a >>> temp_d;
            } else {
                temp_d = a << temp_d;
            }
            if (b.charAt(c) == "+".charAt(0)) {
                a = a + temp_d & 4294967295L;
            } else {
                a = a ^ temp_d;
            }
        }
        return a;
    }
}
