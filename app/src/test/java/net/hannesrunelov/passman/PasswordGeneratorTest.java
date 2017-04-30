package net.hannesrunelov.passman;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for PasswordGenerator class.
 */
public class PasswordGeneratorTest {

    @Test
    public void testRanges() throws Exception {
        try {
            PasswordGenerator.getPassword("",24);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            PasswordGenerator.getPassword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",24);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            PasswordGenerator.getPassword("asdf",0);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            PasswordGenerator.getPassword("asdf",-1);
            fail();
        } catch (IllegalArgumentException e) {}
        try {
            PasswordGenerator.getPassword("asdf",65);
            fail();
        } catch (IllegalArgumentException e) {}

        try {
            PasswordGenerator.getPassword("a",24);
            PasswordGenerator.getPassword("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",24);
            PasswordGenerator.getPassword("asdf",1);
            PasswordGenerator.getPassword("asdf",64);
        } catch (IllegalArgumentException e) {
            fail(e.getStackTrace().toString());
        }
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
                    expected1 = "LXEGSHIWWLJIEYKMUONOMFI";
                    expected2 = "ATATUYORGKXLWFGXUDRHUCURGFDCOVTGQHASLDOEDQ";
                    expected3 = "RDJQKKXTFXYNKWCOMLMMLIGHFZTTNCMSMAPFDXRGVKJCDJEYCPEXKMLMESPEEJZA";
                    regex = "^[A-Z]+$";
                    break;

                // Lowercase
                case 0b0010:
                    expected1 = "lxegshiwwljieykmuonomfi";
                    expected2 = "atatuyorgkxlwfgxudrhucurgfdcovtgqhasldoedq";
                    expected3 = "rdjqkkxtfxynkwcomlmmlighfzttncmsmapfdxrgvkjcdjeycpexkmlmespeejza";
                    regex = "^[a-z]+$";
                    break;

                // Letters
                case 0b0011:
                    expected1 = "LXeGShIwwlJIEYkMuoNomfI";
                    expected2 = "aTaTuYORgkXlwfGxuDrhuCurgFDCovtGqHaslDOedq";
                    expected3 = "rDjQKKxtfXyNKwcOmLmMLIgHfzTtncMSMaPFDxrgVkJCDJEYcPeXkMLMESpEEjZa";
                    regex = "^[A-Za-z]+$";
                    break;

                // Numbers
                case 0b0100:
                    expected1 = "82775417304177733565707";
                    expected2 = "787853949584987212201392704170079097403725";
                    expected3 = "8469194002989793109729564208071939600845672344173210154951859005";
                    regex = "^[0-9]+$";
                    break;

                // Uppercase + Numbers
                case 0b0101:
                    expected1 = "XPOWKNMU7NB3YIMY9UNUOP3";
                    expected2 = "9TO09MWF3UTV3XCZKHL49K9HQ8H93L8GS6WMBLKS4O";
                    expected3 = "6TPIGG8ZHPGRO3Y55PKITAAXPT6F6M79AKTF6Z0MZQBOH4EY7TCTIYXMYGRE3PJO";
                    regex = "^[A-Z0-9]+$";
                    break;

                // Lowercase + Numbers
                case 0b0110:
                    expected1 = "xpowknmu7nb3yimy9unuop3";
                    expected2 = "9to09mwf3utv3xczkhl49k9hq8h93l8gs6wmblks4o";
                    expected3 = "6tpigg8zhpgro3y55pkitaaxpt6f6m79aktf6z0mzqboh4ey7tctiyxmygre3pjo";
                    regex = "^[a-z0-9]+$";
                    break;

                // Letters + Numbers
                case 0b0111:
                    expected1 = "rtMeM6igCNl73MuWaqZqUZC";
                    expected2 = "sDSnWGutQc2NKfErwDxFu1eDqhHimVbampQ1fPiQ2W";
                    expected3 = "ft63Mar0HZGromyKeP1cnAeRpTpVpgw3o7XjrnDIDeNkffkCcB1pK7PEgy03ErbW";
                    regex = "^[A-Za-z0-9]+$";
                    break;

                // Symbols
                case 0b1000:
                    expected1 = "#.!!{)@[(?)@[[[(+/'/!_[";
                    expected2 = "!#!#{(-)$/#)$#!%:..?:+-.[?\\:[_?!-_-[)_+!%{";
                    expected3 = "#\\}$:-)?_.-]-[$+:_-[.$/}\\._]_[@-(-}?_]\\/'[%+\\)@[(.:_@/\\$/:]/$_?{";
                    regex = "^[@%+\\\\\\/'!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Uppercase
                case 0b1001:
                    expected1 = "\\B!UEBM{MZD@W[!IO!_![T[";
                    expected2 = "@N@N{$[V:AV}$F(LO\\)X/EYJQ?VOA}_{SR:I\\}MWJW";
                    expected3 = "'.RQ@+])'R(N(C+/!RCY?:E.H_.J_G+:IM_}?)#YL(?+R\\:@UF(J((TI:OZSY'RQ";
                    regex = "^[A-Z@%+\\\\\\/'!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Lowercase
                case 0b1010:
                    expected1 = "\\b!uebm{mzd@w[!io!_![t[";
                    expected2 = "@n@n{$[v:av}$f(lo\\)x/eyjq?voa}_{sr:i\\}mwjw";
                    expected3 = "'.rq@+])'r(n(c+/!rcy?:e.h_.j_g+:im_}?)#yl(?+r\\:@uf(j((ti:ozsy'rq";
                    regex = "^[a-z@%+\\\\\\/'!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Letters
                case 0b1011:
                    expected1 = "XPOWuxw/[xB($IM$iUNUyP(";
                    expected2 = "-\\O_-wWF(UT'cXCZuHvdiu-H@]H-cv]qS}!wBLu+)y";
                    expected3 = "fTzIqGhZrzG%y(Y{ePKs\\kA#z\\}FfM[-kK\\FfZ_MZ@BOH)oY[Tm\\s$XM$q%E(ztO";
                    regex = "^[A-Za-z@%+\\\\\\/'!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Numbers
                case 0b1100:
                    expected1 = "#.[!5\\1[3?\\@7[7+(/}/[0!";
                    expected2 = "!8[#5+-)-/84-#[2@2%_@(-2!0\\@!_07$?-7\\_(7%/";
                    expected3 = "8\\'91$4??.$#9!$+10-[2${642?#?7:-+-}_?8){}!23\\)@732:?:{)${1#5-00/";
                    regex = "^[0-9@%+\\\\\\/'!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Numbers + Uppercase
                case 0b1101:
                    expected1 = "_H-WW)A(@J8QM73E1E%E{XA";
                    expected2 = "-X-XW(-?7E2JEXS)O.X?9G+PUV_5$6]{KLC(ZT-:ZK";
                    expected3 = "_T}Q!G)J#TW4KQ(SO6!@X[ATT\\#%X[[-31.4_ZPUBEB-XB(AE2G_@@_M3K?$72]O";
                    regex = "^[A-Z0-9@%+\\\\\\/'!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Numbers + Lowercase
                case 0b1110:
                    expected1 = "_h-ww)a(@j8qm73e1e%e{xa";
                    expected2 = "-x-xw(-?7e2jexs)o.x?9g+puv_5$6]{klc(zt-:zk";
                    expected3 = "_t}q!g)j#tw4kq(so6!@x[att\\#%x[[-31.4_zpubeb-xb(ae2g_@@_m3k?$72]o";
                    regex = "^[a-z0-9@%+\\\\\\/'!#$?:.(),{}\\[\\]\\-\\_]+$";
                    break;

                // Symbols + Numbers + Letters
                default:
                    expected1 = "D'kim0Ume%Fws+mOo[_[QXA";
                    expected2 = "axax{oW%[1RBU%98aFfzMqqR-2d7sd]Sg6{MN0qG\\[";
                    expected3 = "''BWoAt}#0$J!w+Ka6YgHc@Np}r)t@3E[O%8hZ4(lc?c60qOg8eR@sDcue}MU2j$";
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