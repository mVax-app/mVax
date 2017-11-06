package mhealth.mvax.search;

/**
 * @author Robert Steilberg
 *         <p>
 *         Representation of a generic tuple
 */

public class Tuple<F, S> {

    private F mFirst;

    private S mSecond;

    public Tuple(F first, S second) {
        this.mFirst = first;
        this.mSecond = second;
    }

    public F getFirst() {
        return mFirst;
    }

    public S getSecond() {
        return mSecond;
    }

    public void setFirst(F first) {
        this.mFirst = first;
    }

    public void setSecond(S second) {
        this.mSecond = second;
    }

}
