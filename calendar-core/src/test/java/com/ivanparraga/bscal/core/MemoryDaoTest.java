package com.ivanparraga.bscal.core;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ivanparraga.bscal.core.domain.Entity;

public class MemoryDaoTest {
	private Dao<Entity<?>> dao;
	private Entity<?> domainObject;

	@BeforeMethod
	public void init() {
		dao = new MemoryDao<>();
		domainObject = mock(Entity.class);
	}

	@Test
	public void createRead() {
		String id = "1234";
		when(domainObject.getId()).thenReturn(id);

		dao.create(domainObject);
		Entity<?> actualObject = dao.read(id);

		assertEquals(actualObject, domainObject);
	}

	@Test
	public void readAllExisting() {
		Entity<?> foo = getMockEntityWithId("foo");
		dao.create(foo);
		Entity<?> boo = getMockEntityWithId("boo");
		dao.create(boo);

		Set<Entity<?>> actualEntities = dao.read();

		Set<Entity<?>> entities = new HashSet<>();
		entities.add(foo);
		entities.add(boo);
		assertEquals(actualEntities, entities);
	}

	private Entity<?> getMockEntityWithId(String id) {
		Entity<?> entity = mock(Entity.class);
		when(entity.getId()).thenReturn(id);
		return entity;
	}

	@Test(expectedExceptions = PersistenceException.class,
		expectedExceptionsMessageRegExp = "Id cannot be null")
	public void createNoId() {
		dao.create(domainObject);
	}

	@Test(expectedExceptions = DuplicateIdException.class,
			expectedExceptionsMessageRegExp =
			"Entity with id \"whatever\" already existed")
	public void createDuplicatedId() {
		when(domainObject.getId()).thenReturn("whatever");
		dao.create(domainObject);
		dao.create(domainObject);
	}

	@Test(expectedExceptions = NoSuchObjectException.class,
		expectedExceptionsMessageRegExp =
		"Object with id \"whatever\" cannot be found")
	public void readNotExisting() {
		dao.read("whatever");
	}

	@Test
	public void deleteNotExisting() {
		dao.delete("whatever");
	}

	@Test(expectedExceptions = NoSuchObjectException.class,
			expectedExceptionsMessageRegExp =
			"Object with id \"foo\" cannot be found")
	public void createDelete() {
		String id = "foo";
		when(domainObject.getId()).thenReturn(id);

		dao.create(domainObject);
		dao.delete(id);
		dao.read(id);
	}
}
