package com.texasjake95.core.tile;

public class Suction implements Comparable<Suction> {

	public enum SuctionType
	{
		DENSE,
		NORMAL,
		VACUUM;
	}

	private SuctionType type;
	private int value;
	private boolean source;

	public Suction(SuctionType type, int value)
	{
		this(type, value, false);
	}

	public Suction(SuctionType type, int value, boolean source)
	{
		this.type = type;
		this.value = value;
		this.source = source;
	}

	@Override
	public int compareTo(Suction other)
	{
		int compare = Boolean.compare(this.source, other.source);
		if (compare != 0)
			return compare;
		compare = this.type.compareTo(other.type);
		if (compare != 0)
			return compare;
		compare = Integer.compare(this.value, other.value);
		return compare;
	}
}
