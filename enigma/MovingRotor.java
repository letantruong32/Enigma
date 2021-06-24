package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Truong Le
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _perm = perm;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        return _notches.indexOf(alphabet().toChar(this.setting())) != -1;
    }

    @Override
    void advance() {
        this.set(_perm.wrap(this.setting() + 1));

    }

    /** All the notches of the Rotor. */
    private String _notches;
    /** Permutation of rotor. */
    private Permutation _perm;
}
