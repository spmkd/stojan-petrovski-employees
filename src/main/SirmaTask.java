package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import model.CollaboratingEmployees;
import model.FromDateToDate;

public class SirmaTask
{
	private static Map<Integer, HashMap<Integer, Set<FromDateToDate>>> employees = new TreeMap<>();
	private static TreeSet<CollaboratingEmployees> collaboratingEmployees = new TreeSet<>();

	public static String SPLITTER = ",";

	public static void main(String[] args)
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("sample.csv");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		try
		{
			for (String line; (line = bufferedReader.readLine()) != null;)
			{
				processLine(line);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		printEmployees();
		fillCollaboratingTreeSet();

		if (collaboratingEmployees.size() > 0)
		{
			CollaboratingEmployees first = collaboratingEmployees.first();
			System.out.printf(
					"%n%nEmployees that have worked most time together are with empId[%d] and empId[%d] with sum of [%d] working days together",
					first.getFirstEmpId(), first.getSecondEmpId(), first.getDaysWorkedTogether());
			;
		} else
		{
			System.out.println("No colleges worked together on any projects!");
		}
	}

	private static void fillCollaboratingTreeSet()
	{
		List<Integer> employeeIds = new LinkedList<>(employees.keySet());

		for (int i = 0; i < employeeIds.size() - 1; i++)
		{
			HashMap<Integer, Set<FromDateToDate>> employeeToCompare = employees.get(employeeIds.get(i));

			for (int y = i + 1; y < employeeIds.size(); y++)
			{

				int commonWorkdays = 0;

				HashMap<Integer, Set<FromDateToDate>> employeeComparator = employees.get(employeeIds.get(y));

				Set<Integer> comparatorProjects = employeeComparator.keySet();
				comparatorProjects.retainAll(employeeToCompare.keySet());

				for (Integer commonProjectId : comparatorProjects)
				{
					commonWorkdays += commonWorkDaysOnProject(employeeToCompare.get(commonProjectId),
							employeeComparator.get(commonProjectId));
				}

				if (commonWorkdays > 0)
				{
					System.out.printf(
							"Common work days between empID[%d] and empID[%d] on common projects is %d days!%n",
							employeeIds.get(i), employeeIds.get(y), commonWorkdays);
					collaboratingEmployees
							.add(new CollaboratingEmployees(employeeIds.get(i), employeeIds.get(y), commonWorkdays));
				}

			}
		}
	}

	private static int commonWorkDaysOnProject(Set<FromDateToDate> datesFirst, Set<FromDateToDate> datesSecond)
	{
		int commonWorkDays = 0;

		if (datesFirst.size() < datesSecond.size())
		{
			return commonWorkDaysOnProject(datesSecond, datesFirst);
		}

		Iterator<FromDateToDate> iteratorFirst = datesFirst.iterator();

		while (iteratorFirst.hasNext())
		{
			FromDateToDate firstFromDateToDate = iteratorFirst.next();

			Iterator<FromDateToDate> iteratorSecond = datesSecond.iterator();

			while (iteratorSecond.hasNext())
			{
				FromDateToDate secondFromDateToDate = iteratorSecond.next();

				if (firstFromDateToDate.compareTo(secondFromDateToDate) > 1)
				{
					FromDateToDate temp = firstFromDateToDate;
					firstFromDateToDate = secondFromDateToDate;
					secondFromDateToDate = temp;
				}

				if (firstFromDateToDate.getTo().compareTo(secondFromDateToDate.getFrom()) > 1)
				{

					if (firstFromDateToDate.getTo().compareTo(secondFromDateToDate.getTo()) > 1)
					{
						commonWorkDays += Math.toIntExact(
								ChronoUnit.DAYS.between(secondFromDateToDate.getFrom(), secondFromDateToDate.getTo()));
					} else
					{
						commonWorkDays += Math.toIntExact(
								ChronoUnit.DAYS.between(secondFromDateToDate.getFrom(), firstFromDateToDate.getTo()));
					}
				}

			}

		}

		return commonWorkDays;
	}

	private static void processLine(String readLine)
	{
		String[] singleLine = readLine.split(SPLITTER);

		int empId = Integer.parseInt(singleLine[0].trim());
		int projectId = Integer.parseInt(singleLine[1].trim());

		LocalDate dateFrom = getDateFromString(singleLine[2].trim());
		LocalDate dateTo = getDateFromString(singleLine[3].trim());

		if (dateFrom == null || dateTo == null)
		{
			System.out.printf("ERROR! Date format could not be determined - [%s]%n", dateFrom);
		} else
		{
			processEmployee(empId, projectId, dateFrom, dateTo);
		}
	}

	private static void processEmployee(int empId, int projectId, LocalDate dateFrom, LocalDate dateTo)
	{
		FromDateToDate fromDateToDate = new FromDateToDate(dateFrom, dateTo);

		// projectId , set of From - To dates
		HashMap<Integer, Set<FromDateToDate>> foundEmployee = employees.get(empId);

		if (foundEmployee == null)
		{
			foundEmployee = new HashMap<Integer, Set<FromDateToDate>>();

			Set<FromDateToDate> listOfDatesOnProject = new HashSet<>();
			listOfDatesOnProject.add(fromDateToDate);

			foundEmployee.put(projectId, listOfDatesOnProject);
			employees.put(empId, foundEmployee);
		} else
		{
			Set<FromDateToDate> datesWorkedOnProject = foundEmployee.get(projectId);

			if (datesWorkedOnProject == null)
			{
				datesWorkedOnProject = new HashSet<>();
			}

			datesWorkedOnProject.add(fromDateToDate);
			foundEmployee.put(projectId, datesWorkedOnProject);
		}

	}

	private static LocalDate getDateFromString(String date)
	{
		if (date.trim().equals("NULL"))
		{
			return LocalDate.now();
		}

		String dateFormatPattern = BorrowedDateUtil.determineDateFormat(date);

		if (dateFormatPattern == null)
		{
			System.out.printf("ERROR! Date format could not be determined - [%s]%n", date);
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatPattern);

		try
		{
			return simpleDateFormat.parse(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		} catch (ParseException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private static void printEmployees()
	{
		for (Integer employeId : employees.keySet())
		{
			System.out.printf("EmployeeID: %d%n", employeId);
			HashMap<Integer, Set<FromDateToDate>> employeeMap = employees.get(employeId);
			for (Integer projectdId : employeeMap.keySet())
			{
				System.out.printf("\sProjectID: %d%n", projectdId);
				for (FromDateToDate fromToDate : employeeMap.get(projectdId))
				{
					System.out.printf("\s\sFrom date %s to date %s %n", fromToDate.getFrom(), fromToDate.getTo());
				}
			}
			System.out.println("------------------------------------");
		}
	}
}
