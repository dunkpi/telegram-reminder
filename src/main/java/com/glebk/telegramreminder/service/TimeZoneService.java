package com.glebk.telegramreminder.service;

import com.glebk.telegramreminder.repository.model.Reminder;
import org.jvnet.hk2.annotations.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by glebk on 7/26/20
 */
@Service
public class TimeZoneService {

	private static final List<TimeZoneMapping> ZONEMAPPINGS = new ArrayList<>();
	private static final String DEFAULT_TIME_ZONE = "UTC";

	static {
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Kabul", "(GMT +04:30) Kabul"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Anchorage", "(GMT -09:00) Alaska"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Riyadh", "(GMT +03:00) Kuwait, Riyadh"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Dubai", "(GMT +04:00) Abu Dhabi, Muscat"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Baghdad", "(GMT +03:00) Baghdad"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Buenos_Aires", "(GMT -03:00) Buenos Aires"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Halifax", "(GMT -04:00) Atlantic Time (Canada)"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Australia/Darwin", "(GMT +09:30) Darwin"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Australia/Sydney", "(GMT +10:00) Canberra, Melbourne, Sydney"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Baku", "(GMT +04:00) Baku"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Atlantic/Azores", "(GMT -01:00) Azores"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Dhaka", "(GMT +06:00) Dhaka"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Regina", "(GMT -06:00) Saskatchewan"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Atlantic/Cape_Verde", "(GMT -01:00) Cape Verde Is."));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Yerevan", "(GMT +04:00) Yerevan"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Australia/Adelaide", "(GMT +09:30) Adelaide"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Guatemala", "(GMT -06:00) Central America"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Almaty", "(GMT +06:00) Astana"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Cuiaba", "(GMT -04:00) Cuiaba"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Europe/Budapest", "(GMT +01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Europe/Warsaw", "(GMT +01:00) Sarajevo, Skopje, Warsaw, Zagreb"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Pacific/Guadalcanal", "(GMT +11:00) Solomon Is., New Caledonia"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Mexico_City", "(GMT -06:00) Guadalajara, Mexico City, Monterrey"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Chicago", "(GMT -06:00) Central Time (US & Canada)"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Shanghai", "(GMT +08:00) Beijing, Chongqing, Hong Kong, Urumqi"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Etc/GMT+12", "(GMT -12:00) International Date Line West"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Africa/Nairobi", "(GMT +03:00) Nairobi"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Australia/Brisbane", "(GMT +10:00) Brisbane"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Europe/Minsk", "(GMT +02:00) Minsk"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Sao_Paulo", "(GMT -03:00) Brasilia"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/New_York", "(GMT -05:00) Eastern Time (US & Canada)"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Africa/Cairo", "(GMT +02:00) Cairo"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Yekaterinburg", "(GMT +05:00) Ekaterinburg"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Pacific/Fiji", "(GMT +12:00) Fiji, Marshall Is."));
		ZONEMAPPINGS.add(new TimeZoneMapping("Europe/Kiev", "(GMT +02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Tbilisi", "(GMT +04:00) Tbilisi"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Europe/London", "(GMT) Dublin, Edinburgh, Lisbon, London"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Godthab", "(GMT -03:00) Greenland"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Atlantic/Reykjavik", "(GMT) Monrovia, Reykjavik"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Europe/Istanbul", "(GMT +02:00) Athens, Bucharest, Istanbul"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Pacific/Honolulu", "(GMT -10:00) Hawaii"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Calcutta", "(GMT +05:30) Chennai, Kolkata, Mumbai, New Delhi"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Tehran", "(GMT +03:30) Tehran"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Jerusalem", "(GMT +02:00) Jerusalem"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Amman", "(GMT +02:00) Amman"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Kamchatka", "(GMT +12:00) Petropavlovsk-Kamchatsky - Old"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Seoul", "(GMT +09:00) Seoul"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Magadan", "(GMT +11:00) Magadan"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Indian/Mauritius", "(GMT +04:00) Port Louis"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Etc/GMT+2", "(GMT -02:00) Mid-Atlantic"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Beirut", "(GMT +02:00) Beirut"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Montevideo", "(GMT -03:00) Montevideo"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Africa/Casablanca", "(GMT) Casablanca"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Chihuahua", "(GMT -07:00) Chihuahua, La Paz, Mazatlan"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Denver", "(GMT -07:00) Mountain Time (US & Canada)"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Rangoon", "(GMT +06:30) Yangon (Rangoon)"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Novosibirsk", "(GMT +06:00) Novosibirsk"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Africa/Windhoek", "(GMT +02:00) Windhoek"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Katmandu", "(GMT +05:45) Kathmandu"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Pacific/Auckland", "(GMT +12:00) Auckland, Wellington"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/St_Johns", "(GMT -03:30) Newfoundland"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Irkutsk", "(GMT +08:00) Irkutsk"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Krasnoyarsk", "(GMT +07:00) Krasnoyarsk"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Santiago", "(GMT -04:00) Santiago"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Tijuana", "(GMT -08:00) Baja California"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Los_Angeles", "(GMT -08:00) Pacific Time (US & Canada)"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Karachi", "(GMT +05:00) Islamabad, Karachi"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Asuncion", "(GMT -04:00) Asuncion"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Europe/Paris", "(GMT +01:00) Brussels, Copenhagen, Madrid, Paris"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Europe/Moscow", "(GMT +03:00) Moscow, St. Petersburg, Volgograd"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Cayenne", "(GMT -03:00) Cayenne, Fortaleza"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Bogota", "(GMT -05:00) Bogota, Lima, Quito"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/La_Paz", "(GMT -04:00) Georgetown, La Paz, Manaus, San Juan"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Pacific/Samoa", "(GMT -11:00) Samoa"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Bangkok", "(GMT +07:00) Bangkok, Hanoi, Jakarta"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Singapore", "(GMT +08:00) Kuala Lumpur, Singapore"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Africa/Johannesburg", "(GMT +02:00) Harare, Pretoria"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Colombo", "(GMT +05:30) Sri Jayawardenepura"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Damascus", "(GMT +02:00) Damascus"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Taipei", "(GMT +08:00) Taipei"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Australia/Hobart", "(GMT +10:00) Hobart"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Tokyo", "(GMT +09:00) Osaka, Sapporo, Tokyo"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Pacific/Tongatapu", "(GMT +13:00) Nuku'alofa"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Ulaanbaatar", "(GMT +08:00) Ulaanbaatar"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Indianapolis", "(GMT -05:00) Indiana (East)"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Phoenix", "(GMT -07:00) Arizona"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Etc/GMT", "(GMT) Coordinated Universal Time"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Etc/GMT-12", "(GMT +12:00) Coordinated Universal Time+12"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Etc/GMT+2", "(GMT -02:00) Coordinated Universal Time-02"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Etc/GMT+11", "(GMT -11:00) Coordinated Universal Time-11"));
		ZONEMAPPINGS.add(new TimeZoneMapping("America/Caracas", "(GMT -04:30) Caracas"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Vladivostok", "(GMT +10:00) Vladivostok"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Australia/Perth", "(GMT +08:00) Perth"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Africa/Lagos", "(GMT +01:00) West Central Africa"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Europe/Berlin", "(GMT +01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Tashkent", "(GMT +05:00) Tashkent"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Pacific/Port_Moresby", "(GMT +10:00) Guam, Port Moresby"));
		ZONEMAPPINGS.add(new TimeZoneMapping("Asia/Yakutsk", "(GMT +09:00) Yakutsk"));
	}

	private static final TimeZoneService INSTANCE = new TimeZoneService();

	public static TimeZoneService getInstance() {
		return INSTANCE;
	}

	private final List<TimeZoneWithDisplayNames> timeZones = new ArrayList<>();

	private TimeZoneService() {
		HashSet<String> availableIdsSet = new HashSet<>(Arrays.asList(TimeZone.getAvailableIDs()));
		for (TimeZoneMapping zoneMapping : ZONEMAPPINGS) {
			String id = zoneMapping.getOlsonName();
			if (!availableIdsSet.contains(id)) {
				throw new IllegalStateException("Unknown ID [" + id + "]");
			}
			TimeZone timeZone = TimeZone.getTimeZone(id);
			timeZones.add(new TimeZoneWithDisplayNames(timeZone, zoneMapping.getWindowsDisplayName()
			));
		}
		timeZones.sort((a, b) -> {
			int diff = a.getTimeZone().getRawOffset() - b.getTimeZone().getRawOffset();
			if (diff < 0) {
				return -1;
			} else if (diff > 0) {
				return 1;
			} else {
				return a.getDisplayName().compareTo(b.getDisplayName());
			}
		});
	}

	public List<TimeZoneWithDisplayNames> getTimeZones() {
		return timeZones;
	}

	public static final class TimeZoneWithDisplayNames {
		private final TimeZone timeZone;

		private final String displayName;

		public TimeZoneWithDisplayNames(TimeZone timeZone, String displayName) {
			this.timeZone = timeZone;
			this.displayName = displayName;
		}

		public TimeZone getTimeZone() {
			return timeZone;
		}

		public String getDisplayName() {
			return displayName;
		}

	}

	private static final class TimeZoneMapping {
		private final String olsonName;
		private final String windowsDisplayName;

		public TimeZoneMapping(String olsonName,
							   String windowsDisplayName) {
			this.olsonName = olsonName;
			this.windowsDisplayName = windowsDisplayName;
		}
		public String getOlsonName() {
			return olsonName;
		}

		public String getWindowsDisplayName() {
			return windowsDisplayName;
		}

	}

	public static List<Reminder> fromDefaultTime(List<Reminder> reminders) {
		if (reminders == null) {
			return Collections.emptyList();
		}
		return reminders.stream().map(TimeZoneService::fromDefaultTime).collect(Collectors.toList());
	}

	public static Reminder fromDefaultTime(Reminder reminder) {
		return convertReminder(reminder, ZoneId.of(DEFAULT_TIME_ZONE), ZoneId.of(reminder.getTimeZoneId()));
	}

	public static Reminder toDefaultTime(Reminder reminder) {
		return convertReminder(reminder, ZoneId.of(reminder.getTimeZoneId()), ZoneId.of(DEFAULT_TIME_ZONE));
	}

	public static LocalDateTime getDefaultTime() {
		return LocalDateTime.now(ZoneId.of(DEFAULT_TIME_ZONE));
	}

	private static Reminder convertReminder(Reminder reminder, ZoneId from, ZoneId to) {
		final LocalDateTime now = LocalDateTime.now(from);
		final LocalDateTime fromDateTime = LocalDateTime.of(
				reminder.getYear() == -1 ? now.getYear() : reminder.getYear(),
				reminder.getMonth() == -1 ? now.getMonthValue() : reminder.getMonth(),
				reminder.getDay() == -1 ? now.getDayOfMonth() : reminder.getDay(),
				reminder.getHour() == -1 ? now.getHour() : reminder.getHour(),
				reminder.getMinute() == -1 ? now.getMinute() : reminder.getMinute(),
				0);
		LocalDateTime toDateTime = fromDateTime.atZone(from).withZoneSameInstant(to).toLocalDateTime();
		final Reminder ret = new Reminder();
		ret.setChatId(reminder.getChatId());
		ret.setLocale(reminder.getLocale());
		ret.setTimeZoneId(reminder.getTimeZoneId());
		ret.setText(reminder.getText());
		ret.setYear(reminder.getYear() == -1 ? reminder.getYear() : toDateTime.getYear());
		ret.setMonth(reminder.getMonth() == -1 ? reminder.getMonth() : toDateTime.getMonthValue());
		ret.setDay(reminder.getDay() == -1 ? reminder.getDay() : toDateTime.getDayOfMonth());
		ret.setHour(reminder.getHour() == -1 ? reminder.getHour() : toDateTime.getHour());
		ret.setMinute(reminder.getMinute() == -1 ? reminder.getMinute() : toDateTime.getMinute());
		return ret;
	}

	public static boolean isLeapYear(int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
	}
}
