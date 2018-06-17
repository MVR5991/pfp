import java.util.List;

/**
 * An interface for 'map' computations.
 *
 * @param InKey    the type of the input key argument
 * @param InValue  the type of the input value argument
 * @param TmpKey   the type of the resulting key
 * @param TmpValue the type of the resulting value
 */
@FunctionalInterface
public interface Mapper<InKey, InValue, TmpKey, TmpValue> {

	/**
	 * Performs the 'map' computation on the specified key-value pair.
	 *
	 * @param key   the key passed to the computation
	 * @param value the value passed to the computation
	 */
	List<Pair<TmpKey, TmpValue>> map(InKey key, InValue value);
}

