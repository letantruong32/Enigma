package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Truong Le
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testInvertChar1() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals('B', p.invert('A'));
        assertEquals('D', p.invert('B'));
        assertEquals('C', p.invert('D'));
        assertEquals('A', p.invert('C'));
    }

    @Test
    public void testInvertChar2() {
        Permutation p = new Permutation("(BAC) (D)", new Alphabet("ABCD"));
        assertEquals('C', p.invert('B'));
        assertEquals('D', p.invert('D'));
        assertEquals('B', p.invert('A'));
        assertEquals('A', p.invert('C'));
    }

    @Test
    public void testInvertChar3() {
        Permutation p = new Permutation("(BA) (D)", new Alphabet("ABCD"));
        assertEquals('A', p.invert('B'));
        assertEquals('D', p.invert('D'));
        assertEquals('B', p.invert('A'));
        assertEquals('C', p.invert('C'));
    }

    @Test
    public void testInvertInt1() {
        Permutation p = new Permutation("(BCD) (A)", new Alphabet("ABCD"));
        assertEquals(0, p.invert(0));
        assertEquals(3, p.invert(1));
        assertEquals(1, p.invert(2));
        assertEquals(2, p.invert(3));
    }

    @Test
    public void testInvertInt2() {
        Permutation p = new Permutation("(BCDA)", new Alphabet("ABCD"));
        assertEquals(3, p.invert(0));
        assertEquals(0, p.invert(1));
        assertEquals(1, p.invert(2));
        assertEquals(2, p.invert(3));
    }

    @Test
    public void testInvertInt3() {
        Permutation p = new Permutation("(BC) (A)", new Alphabet("ABCD"));
        assertEquals(0, p.permute(0));
        assertEquals(2, p.permute(1));
        assertEquals(1, p.permute(2));
        assertEquals(3, p.permute(3));
    }

    @Test
    public void testPermChar1() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals('C', p.permute('A'));
        assertEquals('D', p.permute('C'));
        assertEquals('B', p.permute('D'));
        assertEquals('A', p.permute('B'));
    }

    @Test
    public void testPermChar2() {
        Permutation p = new Permutation("(BAD) (C)", new Alphabet("ABCD"));
        assertEquals('C', p.permute('C'));
        assertEquals('D', p.permute('A'));
        assertEquals('B', p.permute('D'));
        assertEquals('A', p.permute('B'));
    }

    @Test
    public void testPermChar3() {
        Permutation p = new Permutation("(BA) (C)", new Alphabet("ABCD"));
        assertEquals('C', p.permute('C'));
        assertEquals('B', p.permute('A'));
        assertEquals('A', p.permute('B'));
        assertEquals('D', p.permute('D'));
    }


    @Test
    public void testPermInt1() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals(0, p.permute(1));
        assertEquals(2, p.permute(0));
        assertEquals(3, p.permute(2));
        assertEquals(1, p.permute(3));
    }

    @Test
    public void testPermInt2() {
        Permutation p = new Permutation("(BCD) (A)", new Alphabet("ABCD"));
        assertEquals(0, p.permute(0));
        assertEquals(2, p.permute(1));
        assertEquals(3, p.permute(2));
        assertEquals(1, p.permute(3));
    }

    @Test
    public void testPermInt3() {
        Permutation p = new Permutation("(BC) (A)", new Alphabet("ABCD"));
        assertEquals(0, p.permute(0));
        assertEquals(2, p.permute(1));
        assertEquals(1, p.permute(2));
        assertEquals(3, p.permute(3));
    }

    @Test
    public void testPermSize() {
        Permutation p1 = new Permutation("(BAD) (CE)", new Alphabet("ABCDE"));
        assertEquals(5, p1.size());
        Permutation p2 = new Permutation("", new Alphabet(""));
        assertEquals(0, p2.size());
        Permutation p3 = new Permutation("(A)", new Alphabet("A"));
        assertEquals(1, p3.size());
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        p.invert('F');
        p.invert('K');
        p.invert('L');
    }


    @Test
    public void testDerangement() {
        Permutation p1 = new Permutation("(BAD) (CE)", new Alphabet("ABCDE"));
        assertEquals(true, p1.derangement());
        Permutation p2 = new Permutation("(MNK)", new Alphabet("MNK"));
        assertEquals(true, p2.derangement());
        Permutation p3 = new Permutation("(A)", new Alphabet("A"));
        assertEquals(false, p3.derangement());
    }
}
