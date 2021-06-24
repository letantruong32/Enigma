package enigma;
import static enigma.EnigmaException.*;
import java.util.Map;
import java.util.HashMap;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Truong Le
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated.
     *  Can also use Set/HashSet here. */
    Alphabet(String chars) {
        this.chAr = chars.toCharArray();

        Map<Character, Integer> map = new HashMap<>();
        for (char c: chAr) {
            if (map.containsKey(c)) {
                int count = map.get(c);
                map.put(c, ++count);
            } else {
                map.put(c, 1);
            }
        }
        for (char c: map.keySet()) {
            if (map.get(c) > 1) {
                throw error(c + " Alphabet contains duplicates");
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return this.chAr.length;
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        for (char c: this.chAr) {
            if (c == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index > this.size() - 1) {
            throw error("Character index out of range");
        }
        return chAr[index];
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        for (int i = 0; i < this.size(); i++) {
            if (ch == chAr[i]) {
                return i;
            }
        }
        throw error("Character not in Alphabet");
    }

    /** chAr is instance variable of Alphabet that has all of its characters.*/
    private char[] chAr;
}
