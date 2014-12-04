package no.evote.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtil {

	private static Locale locale = Locale.getDefault();
	private static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", locale);
	private static DateFormat dateIdFormat = new SimpleDateFormat("ddMMyy", locale);

	private DateUtil() {
	}

	public static boolean isEndDateBeforeStartDate(final Date startDate, final Date endDate) {
		Date startDateWithoutTime = createDateWithoutTime(startDate);
		Date endDateWithoutTime = createDateWithoutTime(endDate);

		return endDateWithoutTime.before(startDateWithoutTime);
	}

	private static Date createDateWithoutTime(final Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	public static Date setToStartOfDay(final Date date) {
		return setToStartOrEndOfDay(date, true);
	}

	public static Date setToEndOfDay(final Date date) {
		return setToStartOrEndOfDay(date, false);
	}

	private static Date setToStartOrEndOfDay(final Date date, final boolean startOfDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (startOfDay) {
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
		} else {
			// CSOFF: MagicNumber
			calendar.set(Calendar.HOUR, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			// CSON: MagicNumber
		}
		return calendar.getTime();
	}

	public static boolean isDateEqual(final Date date1, final Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)) {
			return false;
		}

		if (cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)) {
			return false;
		}

		if (cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH)) {
			return false;
		}

		return true;
	}

	public static String getFormatedShortDate(final Date date) {
		if (date == null) {
			return "";
		}
		synchronized (dateFormat) {
			return dateFormat.format(date);
		}
	}

	public static String getFormatedShortIdDate(final Date date) {
		if (date == null) {
			return "";
		}
		synchronized (dateIdFormat) {
			return dateIdFormat.format(date);
		}
	}

	public static String getFormatedTime(final Date date, final DateFormat timeFormat) {
		if (date == null) {
			return "";
		}
		synchronized (timeFormat) {
			return timeFormat.format(date);
		}
	}

	public static String getFormatedShortDate(final Date date, final DateFormat dateFormat) {
		if (date == null) {
			return "";
		}
		synchronized (dateFormat) {
			return dateFormat.format(date);
		}
	}

	public static Date parse(final String date) {

		synchronized (dateFormat) {
			dateFormat.setLenient(false);
			try {
				return dateFormat.parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
	}

	public static Date setHourToEndOfDay(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// CSOFF: MagicNumber
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		// CSON: MagicNumber
		return calendar.getTime();
	}

	public static Date setHourToStartOfDay(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 1);
		calendar.set(Calendar.MINUTE, 0);
		return calendar.getTime();
	}

	public static Date setHourAndMinute(final Date date) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(new Date());
		cal2.set(Calendar.HOUR_OF_DAY, cal1.get(Calendar.HOUR_OF_DAY));
		cal2.set(Calendar.MINUTE, cal1.get(Calendar.MINUTE));

		return cal2.getTime();
	}

	public static Date setHourAndMinuteForDate(final Date date, final Date hourAndMinute) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(hourAndMinute);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date);
		cal2.set(Calendar.HOUR_OF_DAY, cal1.get(Calendar.HOUR_OF_DAY));
		cal2.set(Calendar.MINUTE, cal1.get(Calendar.MINUTE));

		return cal2.getTime();
	}

	public static String getAgeInYears(final Date dateOfBirth) {
		String age = "";
		if (dateOfBirth != null) {
			int ageInYears = 0;
			Calendar now = Calendar.getInstance();
			Calendar dob = Calendar.getInstance();
			dob.setTime(dateOfBirth);
			if (dob.after(now)) {
				return age;
			}
			int year1 = now.get(Calendar.YEAR);
			int year2 = dob.get(Calendar.YEAR);
			ageInYears = year1 - year2;

			if ((dob.get(Calendar.MONTH) > now.get(Calendar.MONTH))
					|| (dob.get(Calendar.MONTH) == now.get(Calendar.MONTH) && dob.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH))) {
				ageInYears--;
			}
			age = Integer.toString(ageInYears);
		}
		return age;
	}

	public static String getBirthYearShort(final Date dateOfBirth) {
		String age = "";
		if (dateOfBirth != null) {
			Calendar dob = Calendar.getInstance();
			dob.setTime(dateOfBirth);
			int year = dob.get(Calendar.YEAR);
			if (year > 0) {
				age = String.valueOf(year).substring(2);
			}
		}
		return age;
	}
}
