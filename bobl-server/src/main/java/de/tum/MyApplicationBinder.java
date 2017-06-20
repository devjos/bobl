package de.tum;

import javax.inject.Singleton;

import org.glassfish.jersey.internal.inject.AbstractBinder;

import de.tum.model.DatabaseService;

public class MyApplicationBinder extends AbstractBinder {

  private DatabaseService db;

  public MyApplicationBinder(DatabaseService dbService) {
    this.db = dbService;
  }

  @Override
  protected void configure() {
    bind(db).to(DatabaseService.class).in(Singleton.class);
  }

}
