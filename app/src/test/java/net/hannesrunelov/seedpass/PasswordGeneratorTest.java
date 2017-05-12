package net.hannesrunelov.seedpass;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for PasswordGenerator class.
 */
public class PasswordGeneratorTest {

    @Test
    public void testArgs() throws Exception {
        try {
            PasswordGenerator.getPassword("", 24, 0b1111);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            PasswordGenerator.getPassword("a", 0, 0b1111);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            PasswordGenerator.getPassword("a", -1, 0b1111);
            fail();
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testPasswords() {
        final String key1 = "a";
        final String key2 = "asdf";
        final String key3 = "Bacon ipsum dolor amet turducken meatball laborum nostrud leberk";
        final int length1 = 23;
        final int length2 = 42;
        final int length3 = 64;
        for (int i = 0b0001; i <= 0b1111; ++i) {
            String pass1 = PasswordGenerator.getPassword(key1, length1, i);
            String pass2 = PasswordGenerator.getPassword(key2, length2, i);
            String pass3 = PasswordGenerator.getPassword(key3, length3, i);
            String expected1 = null;
            String expected2 = null;
            String expected3 = null;
            String regex = null;

            switch (i) {

                // Uppercase
                case 0b0001:
                    expected1 = "UDAGQXBJXTBJWCVRRBFIUVC";
                    expected2 = "KFWZPZVWGZCPXZJLYGJWTTUPSOGQWORIIOXEHZIIWI";
                    expected3 = "EMSMGXWKEEAIUVETCVPMRSVTZAOYPQURKCESUJDQTSNARCOUVIZYIHARGJVYFKND";
                    regex = "^[A-Z]+$";
                    break;

                // Lowercase
                case 0b0010:
                    expected1 = "udagqxbjxtbjwcvrrbfiuvc";
                    expected2 = "kfwzpzvwgzcpxzjlygjwttupsogqworiioxehziiwi";
                    expected3 = "emsmgxwkeeaiuvetcvpmrsvtzaoypqurkcesujdqtsnarcouvizyihargjvyfknd";
                    regex = "^[a-z]+$";
                    break;

                // Letters
                case 0b0011:
                    expected1 = "DagqXBjXTbJCWVRrBuFiuVC";
                    expected2 = "fWZPzVwGzkCPxZJlYGjWTtUPsogqWOrIIOxEhzIiWI";
                    expected3 = "mSMgXwKeeaiUvEtcvpMrSvTzaoYpqUrKcesudJQtSnaRCOUVIzYIhARGJvYFkend";
                    regex = "^(?=.*[A-Z])(?=.*[a-z])[A-Za-z]+$";
                    break;

                // Numbers
                case 0b0100:
                    expected1 = "72917042246493844827727";
                    expected2 = "581486655018288291210276191733893747621517";
                    expected3 = "7531183575157878100587426333419413937285614925352363581658232386";
                    regex = "^[0-9]+$";
                    break;

                // Uppercase + Numbers
                case 0b0101:
                    expected1 = "D917XB2XT6JCWVR4B7F77VC";
                    expected2 = "8WZP6V5G05CP2ZJ2YG2WT2UP1917WO8IIO4E62I5WI";
                    expected3 = "5SM1X3K7515U8E8100M8S4T633Y41U4K39378JQ6S49RCOUVI6YI8ARGJ2YF3786";
                    regex = "^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$";
                    break;

                // Lowercase + Numbers
                case 0b0110:
                    expected1 = "d917xb2xt6jcwvr4b7f77vc";
                    expected2 = "8wzp6v5g05cp2zj2yg2wt2up1917wo8iio4e62i5wi";
                    expected3 = "5sm1x3k7515u8e8100m8s4t633y41u4k39378jq6s49rcouvi6yi8argj2yf3786";
                    regex = "^(?=.*[a-z])(?=.*[0-9])[a-z0-9]+$";
                    break;

                // Letters + Numbers
                case 0b0111:
                    expected1 = "a2GQ04J24bJ3W8r4buF7u27";
                    expected2 = "w48zvwgzCkpxzj2ygJ1TTU6S9gqW3RIio47HzIiwI8";
                    expected3 = "35Mgx35EE1I7VE8CVPMR7vtzaOyP1URkc9sUdJ5tsnarc35vi6Y5HA6gJVYF3en6";
                    regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])[A-Za-z0-9]+$";
                    break;

                // Symbols
                case 0b1000:
                    expected1 = "@!$+@#}:?[.}#_[{$}?([{@";
                    expected2 = ")--?@)@%.#)(#}%:.}[@([_]!{._?_+++--)({)@).";
                    expected3 = "(!$]{?_(??]{_[-_?]}[.})){-$[)$}-]-!{[_$@.+[{[%}!_+@@[@%[?:}_[$+(";
                    regex = "^[@%+!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Uppercase
                case 0b1001:
                    expected1 = "D$+@XB:XT.JCWVR$B@F([VC";
                    expected2 = "-WZP)V%G#)CP#ZJ:YG[WT[UP!{._WO+IIO-E({I@WI";
                    expected3 = "!SM{X_K??]{U[E_?]}M.S)T{-$Y)$U-K-!{[$JQ.S[{RCOUVI@YI@ARGJ}YF$(+(";
                    regex = "^(?=.*[A-Z])(?=.*[@%+!#$?:.(),{}\\[\\]\\-\\_])[A-Z@%+!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Lowercase
                case 0b1010:
                    expected1 = "d$+@xb:xt.jcwvr$b@f([vc";
                    expected2 = "-wzp)v%g#)cp#zj:yg[wt[up!{._wo+iio-e({i@wi";
                    expected3 = "!sm{x_k??]{u[e_?]}m.s)t{-$y)$u-k-!{[$jq.s[{rcouvi@yi@argj}yf$(+(";
                    regex = "^(?=.*[a-z])(?=.*[@%+!#$?:.(),{}\\[\\]\\-\\_])[a-z@%+!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Letters
                case 0b1011:
                    expected1 = "a!GQ#}J?[bJ_W[r$buF(u{@";
                    expected2 = "w?@zvwgzCkpxzj:ygJ@TTU]S{gqW_RIio-)HzIiwI-";
                    expected3 = "$!Mgx_(EE]I_VE_CVPMR}vtzaOyP$URkc!sUdJ@tsnarc}!vi@Y[HA[gJVYF$en(";
                    regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[@%+!#$?:.(),{}\\[\\]\\-\\_])[A-Za-z@%+!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Numbers
                case 0b1100:
                    expected1 = "2$+@04:24.43984$8@2([27";
                    expected2 = "-148)6%5#)18#88:91[10[76!{._33+937-7({1@17";
                    expected3 = "!31{8_5??]{7[7_?]}5.7)2{-$3)$9-1-!{[$25.1[{253523@35@1658}32$(+(";
                    regex = "^(?=.*[0-9])(?=.*[@%+!#$?:.(),{}\\[\\]\\-\\_])[0-9@%+!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Numbers + Uppercase
                case 0b1101:
                    expected1 = "9!GQ#}J?[6J_W[4$87F(7{@";
                    expected2 = "1?@66550C58288:91J@TTU]S{17W_RI37-)H2I51I-";
                    expected3 = "$!M18_(EE]I_VE_CVPMR}4263O3P$UR13!3U8J@614925}!23@Y[HA[5JVYF$78(";
                    regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@%+!#$?:.(),{}\\[\\]\\-\\_])[A-Z0-9@%+!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Numbers + Lowercase
                case 0b1110:
                    expected1 = "9!gq#}j?[6j_w[4$87f(7{@";
                    expected2 = "1?@66550c58288:91j@ttu]s{17w_ri37-)h2i51i-";
                    expected3 = "$!m18_(ee]i_ve_cvpmr}4263o3p$ur13!3u8j@614925}!23@y[ha[5jvyf$78(";
                    regex = "^(?=.*[a-z])(?=.*[0-9])(?=.*[@%+!#$?:.(),{}\\[\\]\\-\\_])[a-z0-9@%+!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Numbers + Letters
                default:
                    expected1 = "g2q0B:24.43VW4$$8u2(u2C";
                    expected2 = "4Pz6-wG#Ck8x88:91[WtTUPs{._3Or93O-E(z1@W78";
                    expected3 = "M5g8wKe?ai7[7_cvp5rSvTza$3)$9-1c$!su$J5.Sna2C35VIz3Ih16GJ}32ke+d";
                    regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@%+!#$?:.(),{}\\[\\]\\-\\_])[A-Za-z0-9@%+!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;
            }

            assertEquals(expected1, pass1);
            assertEquals(expected2, pass2);
            assertEquals(expected3, pass3);
            assertTrue(pass1.matches(regex));
            assertTrue(pass2.matches(regex));
            assertTrue(pass3.matches(regex));
        }
    }
}