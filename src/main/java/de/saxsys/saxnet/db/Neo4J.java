package de.saxsys.saxnet.db;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

@Singleton
public class Neo4J {

	private GraphDatabaseService graphDb;

	public GraphDatabaseService db() {
		return graphDb;
	}

	@PostConstruct
	public void init() {
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(
				"target/database/location").newGraphDatabase();
	}

	@PreDestroy
	public void destroy() {
		graphDb.shutdown();
	}
}
