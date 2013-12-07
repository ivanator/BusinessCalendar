package com.ivanparraga.bscal.rest.calendar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.NotFoundException;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ivanparraga.bscal.core.NoSuchObjectException;
import com.ivanparraga.bscal.core.calendar.BasicCalendar;
import com.ivanparraga.bscal.core.calendar.CalendarLao;
import com.ivanparraga.bscal.core.domain.Calendar;

public class CalendarRestTest {
	private CalendarLao lao;
	private CalendarRest rest;

	@BeforeMethod
	public void init() {
		lao = mock(CalendarLao.class);
		rest = new CalendarRest(lao);
	}

	@Test
	public void readExisting() throws JSONException {
		String id = "foo";
		Calendar calendar = new BasicCalendar(id, "fooName", 2004);
		when(lao.read(id)).thenReturn(calendar);

		String actualCalendar = rest.read(id);

		String expected = "{id:\"foo\",name:\"fooName\",year:2004}";
		JSONAssert.assertEquals(expected, actualCalendar, false);
	}

	@Test(expectedExceptions = NotFoundException.class)
	public void readNotExisting() throws JSONException {
		String id = "foo";
		when(lao.read(id)).thenThrow(new NoSuchObjectException(""));

		rest.read(id);
	}

	@Test
	public void create() throws JSONException {
		String id = "foo";
		String calendarName = "fooName";
		int year = 2004;
		Calendar passedCalendar = new BasicCalendar(calendarName, year);
		Calendar expectedCalendar = new BasicCalendar(id, calendarName, year);
		when(lao.create(passedCalendar)).thenReturn(expectedCalendar);

		String calendarToCreate =
				"{\"name\":\"" + calendarName + "\",\"year\":" + year + "}";
		String actualCalendar = rest.create(calendarToCreate);

		verify(lao).create(passedCalendar);
		String expected = "{id:\"" + id + "\",name:\"" + calendarName + "\",year:" + year + "}";
		JSONAssert.assertEquals(expected, actualCalendar, false);
	}
}