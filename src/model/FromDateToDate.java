package model;

import java.time.LocalDate;

public class FromDateToDate implements Comparable<FromDateToDate>
{
	LocalDate from;
	LocalDate to;

	public FromDateToDate(LocalDate from, LocalDate to)
	{
		super();
		this.from = from;
		this.to = to;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FromDateToDate other = (FromDateToDate) obj;
		if (from == null)
		{
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null)
		{
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}

	@Override
	public int compareTo(FromDateToDate o)
	{
		return this.getFrom().compareTo(o.getFrom());
	}

	@Override
	public String toString()
	{
		return "FromDateToDate [from=" + from + ", to=" + to + "]";
	}

	public LocalDate getFrom()
	{
		return from;
	}

	public void setFrom(LocalDate from)
	{
		this.from = from;
	}

	public LocalDate getTo()
	{
		return to;
	}

	public void setTo(LocalDate to)
	{
		this.to = to;
	}

}
