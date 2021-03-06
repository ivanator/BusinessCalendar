package com.ivanparraga.bscal.rest.config;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.ivanparraga.bscal.core.calendar.CalendarGuiceModule;
import com.ivanparraga.bscal.rest.calendar.CalendarRest;

public class GuiceServletConfig extends GuiceServletContextListener {
	public static Injector injector;

	@Override
	protected Injector getInjector() {
		ServletModule servletModule = new ServletModule() {
			@Override
			protected void configureServlets() {
				bind(CalendarRest.class);
				install(getModule());

				bind(JacksonJsonProvider.class).in(Scopes.SINGLETON);
			}
		};

		injector = Guice.createInjector(servletModule);
		return injector;
	}

	protected Module getModule() {
		return new CalendarGuiceModule();
	}
}
