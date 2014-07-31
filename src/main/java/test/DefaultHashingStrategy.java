package test;

import gnu.trove.strategy.HashingStrategy;

public class DefaultHashingStrategy<T> implements HashingStrategy<T> {

	private static final long serialVersionUID = 1905511294598977651L;

	@Override
	public int computeHashCode(T object)
	{
		return object.hashCode();
	}

	@Override
	public boolean equals(T o1, T o2)
	{
		return o1.equals(o2);
	}
}
