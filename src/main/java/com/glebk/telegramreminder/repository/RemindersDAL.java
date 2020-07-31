package com.glebk.telegramreminder.repository;

import com.glebk.telegramreminder.repository.model.Reminder;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by glebk on 7/29/20
 */
@Repository
public class RemindersDAL {

	private static final String CHAT_ID = "chatId";
	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String DAY = "day";
	private static final String HOUR = "hour";
	private static final String MINUTE = "minute";

	private final MongoTemplate mongoTemplate;

	public RemindersDAL(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public List<Reminder> getCurrentTimeReminders(Reminder reminder) {
		Query query = new Query(
				new Criteria().andOperator(
						new Criteria().orOperator(
								Criteria.where(YEAR).is(-1),
								Criteria.where(YEAR).is(reminder.getYear())),
						new Criteria().orOperator(
								Criteria.where(MONTH).is(-1),
								Criteria.where(MONTH).is(reminder.getMonth())),
						new Criteria().orOperator(
								Criteria.where(DAY).is(-1),
								Criteria.where(DAY).is(reminder.getDay())),
						new Criteria().orOperator(
								Criteria.where(HOUR).is(-1),
								Criteria.where(HOUR).is(reminder.getHour())),
						new Criteria().orOperator(
								Criteria.where(MINUTE).is(-1),
								Criteria.where(MINUTE).is(reminder.getMinute()))));
		return mongoTemplate.find(query, Reminder.class);
	}

	public List<Reminder> getChatActualReminders(Reminder reminder) {
		Query query = new Query(
				new Criteria().andOperator(
						Criteria.where(CHAT_ID).is(reminder.getChatId()),
						new Criteria().orOperator(
								Criteria.where(YEAR).is(-1),
								Criteria.where(YEAR).gte(reminder.getYear())),
						new Criteria().orOperator(
								Criteria.where(YEAR).is(-1),
								Criteria.where(YEAR).gt(reminder.getYear()),
								Criteria.where(MONTH).is(-1),
								Criteria.where(MONTH).gte(reminder.getMonth())),
						new Criteria().orOperator(
								Criteria.where(YEAR).is(-1),
								Criteria.where(YEAR).gt(reminder.getYear()),
								Criteria.where(MONTH).is(-1),
								Criteria.where(MONTH).gt(reminder.getMonth()),
								Criteria.where(DAY).is(-1),
								Criteria.where(DAY).gte(reminder.getDay())),
						new Criteria().orOperator(
								Criteria.where(YEAR).is(-1),
								Criteria.where(YEAR).gt(reminder.getYear()),
								Criteria.where(MONTH).is(-1),
								Criteria.where(MONTH).gt(reminder.getMonth()),
								Criteria.where(DAY).is(-1),
								Criteria.where(DAY).gt(reminder.getDay()),
								Criteria.where(HOUR).is(-1),
								Criteria.where(HOUR).gte(reminder.getHour())),
						new Criteria().orOperator(
								Criteria.where(YEAR).is(-1),
								Criteria.where(YEAR).gt(reminder.getYear()),
								Criteria.where(MONTH).is(-1),
								Criteria.where(MONTH).gt(reminder.getMonth()),
								Criteria.where(DAY).is(-1),
								Criteria.where(DAY).gt(reminder.getDay()),
								Criteria.where(HOUR).gt(reminder.getHour()),
								Criteria.where(HOUR).is(-1),
								Criteria.where(MINUTE).is(-1),
								Criteria.where(MINUTE).gt(reminder.getMinute()))))
				.with(Sort.by(YEAR, MONTH, DAY, HOUR, MINUTE));
		return mongoTemplate.find(query, Reminder.class);
	}

	public Reminder get(Reminder reminder) {
		Query query = new Query(
				new Criteria().andOperator(
						Criteria.where(CHAT_ID).is(reminder.getChatId()),
						Criteria.where(YEAR).is(reminder.getYear()),
						Criteria.where(MONTH).is(reminder.getMonth()),
						Criteria.where(DAY).is(reminder.getDay()),
						Criteria.where(HOUR).is(reminder.getHour()),
						Criteria.where(MINUTE).is(reminder.getMinute())));
		return mongoTemplate.findOne(query, Reminder.class);
	}

	public void save(Reminder reminder) {
		mongoTemplate.save(reminder);
	}

	public void delete(Reminder reminder) {
		Query query = new Query(
				new Criteria().andOperator(
						Criteria.where(CHAT_ID).is(reminder.getChatId()),
						Criteria.where(YEAR).is(reminder.getYear()),
						Criteria.where(MONTH).is(reminder.getMonth()),
						Criteria.where(DAY).is(reminder.getDay()),
						Criteria.where(HOUR).is(reminder.getHour()),
						Criteria.where(MINUTE).is(reminder.getMinute())));
		mongoTemplate.remove(query, Reminder.class);
	}

	public void deleteOutdatedReminders(Reminder reminder) {
		Query query = new Query(
				new Criteria().orOperator(
						new Criteria().andOperator(
								Criteria.where(YEAR).ne(-1),
								Criteria.where(YEAR).lt(reminder.getYear())),
						new Criteria().andOperator(
								Criteria.where(MONTH).ne(-1),
								Criteria.where(YEAR).is(reminder.getYear()),
								Criteria.where(MONTH).lt(reminder.getMonth())),
						new Criteria().andOperator(
								Criteria.where(DAY).ne(-1),
								Criteria.where(YEAR).is(reminder.getYear()),
								Criteria.where(MONTH).is(reminder.getMonth()),
								Criteria.where(DAY).lt(reminder.getDay())),
						new Criteria().andOperator(
								Criteria.where(HOUR).ne(-1),
								Criteria.where(YEAR).is(reminder.getYear()),
								Criteria.where(MONTH).is(reminder.getMonth()),
								Criteria.where(DAY).is(reminder.getDay()),
								Criteria.where(HOUR).lt(reminder.getHour())),
						new Criteria().andOperator(
								Criteria.where(MINUTE).ne(-1),
								Criteria.where(YEAR).is(reminder.getYear()),
								Criteria.where(MONTH).is(reminder.getMonth()),
								Criteria.where(DAY).is(reminder.getDay()),
								Criteria.where(HOUR).is(reminder.getHour()),
								Criteria.where(MINUTE).lt(reminder.getMinute()))));
		mongoTemplate.remove(query, Reminder.class);
	}
}
