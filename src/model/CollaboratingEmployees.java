package model;

public class CollaboratingEmployees implements Comparable<CollaboratingEmployees>
{
	int firstEmpId;
	int secondEmpId;
	int daysWorkedTogether;

	public CollaboratingEmployees(int firstEmpId, int secondEmpId, int daysWorkedTogether)
	{
		super();
		this.firstEmpId = firstEmpId;
		this.secondEmpId = secondEmpId;
		this.daysWorkedTogether = daysWorkedTogether;
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
		CollaboratingEmployees other = (CollaboratingEmployees) obj;
		if (daysWorkedTogether != other.daysWorkedTogether)
			return false;
		if (firstEmpId != other.firstEmpId)
			return false;
		if (secondEmpId != other.secondEmpId)
			return false;
		return true;
	}

	@Override
	public int compareTo(CollaboratingEmployees o)
	{
		return o.getDaysWorkedTogether() - this.getDaysWorkedTogether();
	}

	public int getFirstEmpId()
	{
		return firstEmpId;
	}

	public void setFirstEmpId(int firstEmpId)
	{
		this.firstEmpId = firstEmpId;
	}

	public int getSecondEmpId()
	{
		return secondEmpId;
	}

	public void setSecondEmpId(int secondEmpId)
	{
		this.secondEmpId = secondEmpId;
	}

	public int getDaysWorkedTogether()
	{
		return daysWorkedTogether;
	}

	public void setDaysWorkedTogether(int daysWorkedTogether)
	{
		this.daysWorkedTogether = daysWorkedTogether;
	}
}
