package org.springframework.data.mongodb.builder;

import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.mongodb.builder.BasicQuery;
import org.springframework.data.mongodb.builder.QueryBuilder;
import org.springframework.data.mongodb.builder.SortSpecification.SortOrder;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class QueryBuilderTest {

	@Test
	public void q1() {

		QueryBuilder q = new QueryBuilder();
		q.find("name").gte("M").lte("T").and("age").not().gt(22);
		q.fields().exclude("address");
		q.slice().on("orders", 10);
		q.sort().on("name", SortOrder.ASCENDING);
		q.limit(50);
		String expected = "{ \"name\" : { \"$gte\" : \"M\" , \"$lte\" : \"T\"} , \"age\" : { \"$not\" : { \"$gt\" : 22}}}";
		Assert.assertEquals(expected, q.build().getQueryObject().toString());
	}
	
	@Test
	public void q2() {

		QueryBuilder q = new QueryBuilder();
		q.find("name").is("Thomas");
		q.find("age").not().mod(22, 2);
		String expected = "{ \"name\" : \"Thomas\" , \"age\" : { \"$not\" : { \"$mod\" : [ 22 , 2]}}}";
		Assert.assertEquals(expected, q.build().getQueryObject().toString());
	}

	@Test
	public void q3() {

		QueryBuilder q = new QueryBuilder();
		q.or(
				new QueryBuilder().find("name").is("Sven").and("age").lt(50).build(), 
				new QueryBuilder().find("age").lt(50).build(),
				new BasicQuery("{'name' : 'Thomas'}")
		);
		q.sort().on("name", SortOrder.ASCENDING);
		String expected = "{ \"$or\" : [ { \"name\" : \"Sven\" , \"age\" : { \"$lt\" : 50}} , { \"age\" : { \"$lt\" : 50}} , { \"name\" : \"Thomas\"}]}";
		Assert.assertEquals(expected, q.build().getQueryObject().toString());
	}
	
	//@Test
	public void m1() throws UnknownHostException, MongoException {
		Mongo mongo = new Mongo();
		DB db = mongo.getDB("test");
		System.out.println(db.getCollectionNames());
	}
	
}
