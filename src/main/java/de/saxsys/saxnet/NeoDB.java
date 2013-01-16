package de.saxsys.saxnet;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class NeoDB {

	private static GraphDatabaseService graphDb;

	public static GraphDatabaseService getInstance() {
		if (null == graphDb) {
			graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(
					"target/database/location").newGraphDatabase();
		}
		return graphDb;
	}
}
