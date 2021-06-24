package enigma;
import static enigma.EnigmaException.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Truong Le
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        boolean invalidCy1, invalidCy2, invalidCy3;
        for (int i = 0; i < cycles.length() - 1; i++) {
            invalidCy1 = cycles.charAt(i) == '(' && cycles.charAt(i + 1) == ')';
            invalidCy2 = cycles.charAt(i) == '(' && cycles.charAt(i + 1) == '(';
            invalidCy3 = cycles.charAt(i) == ')' && cycles.charAt(i + 1) == ')';
            if (invalidCy1 || invalidCy2 || invalidCy3) {
                throw error("Cycle cannot be empty. Or invalid syntax");
            }
        }
        boolean invalidAlpha = alphabet.contains('(') || alphabet.contains(')')
                || alphabet.contains(' ');
        if (invalidAlpha) {
            throw error("Alphabet cannot contain ), ( or space");
        }

        _cycles = cycles;
        _alphabet = alphabet;
        _cyclesArr = cyclesArr();
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        Pattern pat = Pattern.compile("\\s");
        Matcher mat = pat.matcher(cycle);
        boolean found = mat.find();
        if (found) {
            throw error("Cycle cannot contain whitespace");
        } else {
            this._cycles += cycle;
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /**
     * Put all cycles into String Array in order to
     * facilitate cycles manipulation (permute/invert).
     * @return all cycles as String Array
     */
    public String[] cyclesArr() {
        String cycNoSpace = _cycles.replaceAll(" ", "");
        String[] cyclesArr = cycNoSpace.split("\\)");
        for (int i = 0; i < cyclesArr.length; i++) {
            if (cyclesArr[i].length() > 0) {
                cyclesArr[i] = cyclesArr[i].substring(1);
            }
        }
        return cyclesArr;
    }


    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        if (p < 0 || p > this.size() - 1) {
            throw error("Character index out of range");
        }
        char chFr = _alphabet.toChar(wrap(p));
        char chTo = '\0';
        for (String cycle: _cyclesArr) {
            if (cycle.indexOf(chFr) != -1) {
                if (cycle.length() == 1) {
                    chTo = chFr;
                } else if (cycle.indexOf(chFr) == cycle.length() - 1) {
                    chTo = cycle.charAt(0);
                } else {
                    chTo = cycle.charAt(cycle.indexOf(chFr) + 1);
                }
            }
        }
        if (chTo == '\0') {
            return _alphabet.toInt(chFr);
        } else {
            return _alphabet.toInt(chTo);
        }
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        if (c < 0 || c > this.size() - 1) {
            throw error("Character index out of range");
        }
        char chFr = _alphabet.toChar(wrap(c));
        char chTo = '\0';
        for (String cycle: _cyclesArr) {
            if (cycle.indexOf(chFr) != -1) {
                if (cycle.length() == 1) {
                    chTo = chFr;
                } else if (cycle.indexOf(chFr) == 0) {
                    chTo = cycle.charAt(cycle.length() - 1);
                } else {
                    chTo = cycle.charAt(cycle.indexOf(chFr) - 1);
                }
            }
        }
        if (chTo == '\0') {
            return _alphabet.toInt(chFr);
        } else {
            return _alphabet.toInt(chTo);
        }
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (!_alphabet.contains(p)) {
            throw error("Character not in Alphabet");
        }
        int indexFr = _alphabet.toInt(p);
        int indexTo = permute(indexFr);
        return _alphabet.toChar(indexTo);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!_alphabet.contains(c)) {
            throw error("Character not in Alphabet");
        }
        int indexFr = _alphabet.toInt(c);
        int indexTo = invert(indexFr);
        return _alphabet.toChar(indexTo);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        if (this.size() == 1) {
            return false;
        }
        int cyclesLength = 0;
        for (String cycle: _cyclesArr) {
            cyclesLength += cycle.length();
        }
        return cyclesLength == this.size();
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** Cycles represented as string of this permutation. */
    private String _cycles;
    /** Array of cycles of this permutation. */
    private String[] _cyclesArr;
}
