package net.hannesrunelov.passman;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for PasswordGenerator class.
 */
public class PasswordGeneratorTest {

    @Test
    public void testArgs() throws Exception {
        try {
            PasswordGenerator.getPassword("", 24);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            PasswordGenerator.getPassword("a", 0);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            PasswordGenerator.getPassword("a", -1);
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
                    expected1 = "WUDAGQXBJXTBJCVRRBFIUVC";
                    expected2 = "TKFWZPZVWGZCPXZJLYGJWTUPSOGQWORIIOXEHZIIWI";
                    expected3 = "REGESMVGCEWYOGHQVWHTQHYJPNWWKZOYLIEQSMLABEJIRGEONEHCGTSRANJCNGPR";
                    regex = "^[A-Z]+$";
                    break;

                // Lowercase
                case 0b0010:
                    expected1 = "wudagqxbjxtbjcvrrbfiuvc";
                    expected2 = "tkfwzpzvwgzcpxzjlygjwtupsogqworiioxehziiwi";
                    expected3 = "regesmvgcewyoghqvwhtqhyjpnwwkzoylieqsmlabejirgeonehcgtsranjcngpr";
                    regex = "^[a-z]+$";
                    break;

                // Letters
                case 0b0011:
                    expected1 = "WUDagqXBjXTbJCVRrBFiuVC";
                    expected2 = "TkfWZPzVwGzCPxZJlYGjWtUPsogqWOrIIOxEhzIiWI";
                    expected3 = "regESmVgCewyoGhQvwhtQhYjPnwwKzoYlIeqsmlAbEjiRGEONEhCGtSRANjCNgpr";
                    regex = "^[A-Za-z]+$";
                    break;

                // Numbers
                case 0b0100:
                    expected1 = "97291704224643844827727";
                    expected2 = "058148665501828829121276191733893747621517";
                    expected3 = "0317938113171581274452128075745387931701654987796703565676094568";
                    regex = "^[0-9]+$";
                    break;

                // Uppercase + Numbers
                case 0b0101:
                    expected1 = "WUD917XB2XT6JCVR4BF77VC";
                    expected2 = "T58WZP6V5G0CP2ZJ2YG2W2UP1917WO8IIO4E62I5WI";
                    expected3 = "031ES3V1C3171G8Q2744Q2Y2P075K45Y8I93170A6E49RGEONE0CG6SRAN0CN568";
                    regex = "^[A-Z0-9]+$";
                    break;

                // Lowercase + Numbers
                case 0b0110:
                    expected1 = "wud917xb2xt6jcvr4bf77vc";
                    expected2 = "t58wzp6v5g0cp2zj2yg2w2up1917wo8iio4e62i5wi";
                    expected3 = "031es3v1c3171g8q2744q2y2p075k45y8i93170a6e49rgeone0cg6sran0cn568";
                    regex = "^[a-z0-9]+$";
                    break;

                // Letters + Numbers
                case 0b0111:
                    expected1 = "WUDaGQ04J24bJ38r4bF7u27";
                    expected2 = "0K8w48zvwgzCpxzj2ygJ1TU6S9gqW3RIio47HzIiwI";
                    expected3 = "03G7S3V1CewyOgh1VWH45212P075KZoy879q170A6eJI8ge9NehC5t56a60CngpR";
                    regex = "^[A-Za-z0-9]+$";
                    break;

                // Symbols
                case 0b1000:
                    expected1 = "$!.-:[_\\.%)})(]\\\\#.[!.[";
                    expected2 = "?/#@\\#''//?:]%]#.-@.:%!}@$@[+(#-(!\\[}.@{@!";
                    expected3 = "_+@[$+#@:(:!:{]@.[\\\\/%:.]_!{[)/(#[-(:[_:}/\\-#!!$}[?+/}{}[}?$)/}#";
                    regex = "^[@%+\\\\\\/'!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Uppercase
                case 0b1001:
                    expected1 = "WUD-:[XB.XT}JCVR\\BF[!VC";
                    expected2 = "T/#WZP'V/G?CP%ZJ.YG.W%UP@$@[WO#IIO\\E}.I{WI";
                    expected3 = "_+@ES+V@C(:!:G]Q.[\\\\Q%Y.P_!{K)/Y#I-(:[_A}E\\-RGEONE?CG}SRAN?CN/}#";
                    regex = "^[A-Z@%+\\\\\\/'!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Lowercase
                case 0b1010:
                    expected1 = "wud-:[xb.xt}jcvr\\bf[!vc";
                    expected2 = "t/#wzp'v/g?cp%zj.yg.w%up@$@[wo#iio\\e}.i{wi";
                    expected3 = "_+@es+v@c(:!:g]q.[\\\\q%y.p_!{k)/y#i-(:[_a}e\\-rgeone?cg}sran?cn/}#";
                    regex = "^[a-z@%+\\\\\\/'!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Letters
                case 0b1011:
                    expected1 = "WUDaGQ_\\J%)bJ(]r\\bF[u.[";
                    expected2 = "?K#w\\#zvwgzCpxzj.ygJ:TU}S$gqW(RIio\\[HzIiwI";
                    expected3 = "_+G[S+V@CewyOgh@VWH\\/%:.P_!{KZoy#[-q:[_A}eJI#ge$NehC/t{}a}?CngpR";
                    regex = "^[A-Za-z@%+\\\\\\/'!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Numbers
                case 0b1100:
                    expected1 = "972-:[04.24}4384\\82[!27";
                    expected2 = "0/#148'6/5?18%88.91.1%76@$@[33#937\\7}.1{17";
                    expected3 = "_+@79+8@1(:!:5]1.[\\\\5%1.8_!{7)/3#7-(:[_1}5\\-877967?35}5676?94/}#";
                    regex = "^[0-9@%+\\\\\\/'!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Numbers + Uppercase
                case 0b1101:
                    expected1 = "WUD9GQ_\\J%)6J(]4\\8F[7.[";
                    expected2 = "?K#1\\#66550C8288.91J:TU}S$17W(RI37\\[H2I51I";
                    expected3 = "_+G[S+V@C317O58@VWH\\/%:.P_!{KZ53#[-3:[_A}5JI#77$N70C/6{}7}?C456R";
                    regex = "^[A-Z0-9@%+\\\\\\/'!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Numbers + Lowercase
                case 0b1110:
                    expected1 = "wud9gq_\\j%)6j(]4\\8f[7.[";
                    expected2 = "?k#1\\#66550c8288.91j:tu}s$17w(ri37\\[h2i51i";
                    expected3 = "_+g[s+v@c317o58@vwh\\/%:.p_!{kz53#[-3:[_a}5ji#77$n70c/6{}7}?c456r";
                    regex = "^[a-z0-9@%+\\\\\\/'!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Numbers + Letters
                default:
                    expected1 = "W7D-gq0B.24}43V4\\82[u2C";
                    expected2 = "0k#W4Pz6wG?C8x88.91.WtUPs$@[3Or93O\\E}z1{W7";
                    expected3 = "_egES+8@Cew!:5]1.wht5%Y.P_!w7zo3l7-(:[_1}E\\-8G79NEh3G}56AN?94g}r";
                    regex = "^[A-Za-z0-9@%+\\\\\\/'!#$?:.(),{}\\[\\]\\-\\_]+$";
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