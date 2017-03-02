package lyric.utils;

public class Pair<F, S> {
	public final F first;
    public final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair))
            return false;
        if (o == this)
            return true;
        if (o.hashCode() == this.hashCode())
        	return true;
        Pair t = (Pair) o;
        return first == t.first
                && second == t.second;
    }

    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode())
                ^ (second == null ? 0 : second.hashCode());
    }
}
