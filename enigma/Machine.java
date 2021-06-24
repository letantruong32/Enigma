package enigma;

import java.util.HashMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Truong Le
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        if (numRotors <= 1) {
            throw error("Num Rotors must be greater than 1");
        }
        if (pawls < 0 || pawls >= numRotors) {
            throw error("Incorrect value of pawls");
        }
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length != _numRotors) {
            throw error("Num of rotors inserted must be equal to numRotor");
        }
        if (_pawls < 0) {
            throw error("Num of moving rotors must be greater/equal to 0");
        }

        rotorsArr = new Rotor[_numRotors];
        HashMap<String, Rotor> rotorsHashMap = new HashMap<>();
        for (Rotor r: _allRotors) {
            rotorsHashMap.put(r.name(), r);
        }

        for (int i = 0; i < rotors.length; i++) {
            if (rotorsHashMap.containsKey(rotors[i])) {
                rotorsArr[i] = rotorsHashMap.get(rotors[i]);
            } else {
                throw error("Rotor's name not in list of all rotors");
            }
        }

        if (!rotorsArr[0].reflecting()) {
            throw error("Leftmost rotor must be a reflector");
        }
        for (int i = (_numRotors - _pawls); i < _numRotors; i++) {
            if (!rotorsArr[i].rotates()) {
                throw error("All rotors have pawl must rotate");
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != _numRotors - 1) {
            throw error("Setting's length must equal _numRotors - 1");
        }
        for (int i = 1; i < rotorsArr.length; i++) {
            if (!_alphabet.contains(setting.charAt(i - 1))) {
                throw error("Initial position not in alphabet");
            }
            rotorsArr[i].set(setting.charAt(i - 1));
        }
    }

    /** Set my rotors according to RINGSETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRingRotors(String ringSetting) {
        if (ringSetting.length() != _numRotors - 1) {
            throw error("Setting's length must equal _numRotors - 1");
        }
        for (int i = 1; i < rotorsArr.length; i++) {
            if (!_alphabet.contains(ringSetting.charAt(i - 1))) {
                throw error("Initial position not in alphabet");
            }
            rotorsArr[i].setRing(ringSetting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        if (plugboard.derangement()) {
            throw error("Plugboard must contain no derangement");
        }
        String[] cycles = plugboard.cyclesArr();
        for (String cycle: cycles) {
            if (cycle.length() != 2) {
                throw error("Each cycle must have max of 2 chars");
            }
        }
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advRotors();
        int ch = c % _alphabet.size();

        if (_plugboard != null) {
            ch = _plugboard.permute(ch);
        }

        for (int i = rotorsArr.length - 1; i >= 0; i--) {
            ch = rotorsArr[i].convertForward(ch);
        }
        for (int j = 1; j < rotorsArr.length; j++) {
            ch = rotorsArr[j].convertBackward(ch);
        }

        if (_plugboard != null) {
            ch = _plugboard.permute(ch);
        }

        return ch;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String convert = "";
        for (int i = 0; i < msg.length(); i++) {
            int c = convert(_alphabet.toInt(msg.charAt(i)));
            convert += _alphabet.toChar(c);
        }
        return convert;
    }

    /** Advance all selected Rotors at Notch. */
    void advRotors() {
        if (_pawls == 0) {
            return;
        }
        Set<Rotor> advanceRotors = new HashSet<>();
        for (int i = _numRotors - _pawls; i < _numRotors; i++) {
            if (i == _numRotors - 1) {
                advanceRotors.add(rotorsArr[i]);
            } else {
                if (rotorsArr[i + 1].atNotch()) {
                    advanceRotors.add(rotorsArr[i]);
                }
                if (rotorsArr[i].atNotch()
                        && !rotorsArr[i - 1].reflecting()) {
                    advanceRotors.add(rotorsArr[i]);
                    advanceRotors.add(rotorsArr[i - 1]);
                }
            }
        }
        for (Rotor r: advanceRotors) {
            r.advance();
        }
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** Number of rotors of the machine. */
    private int _numRotors;
    /** Number of pawls of the machine. */
    private int _pawls;
    /** A collection of all Rotors in the machine. */
    private Collection<Rotor> _allRotors;
    /** The plugboard of the machine. */
    private Permutation _plugboard;
    /** Array of rotors of the machine. */
    private Rotor[] rotorsArr;
}
