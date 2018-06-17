import java.util.Objects;



/**
 * A pair of two values. The pair is immutable: Once it is created, it is not
 * possible to modify its elements.
 *
 * @param K the type of the first element of the pair
 * @param V the type of the second element of the pair
 */
public class Pair<K, V> {

	private final K key;
	private final V value;



	/**
	 * Instantiates a new pair composed of the specified elements.
	 *
	 * @param key the first element of the pair
	 * @param value the second element of the pair
	 */
	public Pair(final K key, final V value) {
		this.key = key;
		this.value = value;
	}



	/**
	 * Returns the key (the first element) of the pair.
	 *
	 * @return the first element
	 */
	public K getKey() {
		return this.key;
	}



	/**
	 * Returns the first element of the pair.
	 *
	 * @return the first element
	 */
	public K getFirst() {
		return this.getKey();
	}



	/**
	 * Returns the value (the second element) of the pair.
	 *
	 * @return the second element
	 */
	public V getValue() {
		return this.value;
	}



	/**
	 * Returns the second element of the pair.
	 *
	 * @return the second element
	 */
	public V getSecond() {
		return this.getValue();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	public boolean equals(final Object obj) {
		if (obj instanceof Pair) {
			final Pair<?, ?> other = (Pair<?, ?>) obj;
			return Objects.equals(this.getKey(), other.getKey())
					&& Objects.equals(this.getValue(), other.getValue());
		}
		return false;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.getKey(), this.getValue());
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "(" + this.getKey() + ", " + this.getValue() + ")";
	}
}

