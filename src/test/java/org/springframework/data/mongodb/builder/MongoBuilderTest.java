package org.springframework.data.mongodb.builder;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.mongodb.builder.SortSpec.SortOrder;

public class MongoBuilderTest {

	@Test
	public void u1() {
		UpdateSpec u = new UpdateSpec();
		u.set("name",52);
		u.sort().on("name", SortOrder.DESCENDING);

		String expected = "{ \"$set\" : { \"name\" : 52}}";
		Assert.assertEquals(expected, u.build().getUpdateObject().toString());
		
		String expectedSort = "{ \"name\" : -1}";
		Assert.assertEquals(expectedSort, u.build().getSortObject().toString());
	}

	@Test
	public void q1() {

		QuerySpec q = new QuerySpec();
		q.find("name").gte("M").lte("T").and("age").not().gt(22);		
		q.fields().exclude("address").slice("orders", 10);
		q.limit(50);

		String expected = "{ \"name\" : { \"$gte\" : \"M\" , \"$lte\" : \"T\"} , \"age\" : { \"$not\" : { \"$gt\" : 22}}}";
		Assert.assertEquals(expected, q.build().getQueryObject().toString());

		String expectedFields = "{ \"address\" : 0 , \"orders\" : { \"$slice\" : 10}}";
		Assert.assertEquals(expectedFields, q.build().getFieldsObject().toString());
		
		Assert.assertEquals(50, q.build().getLimit());
	}
	
	@Test
	public void q2() {

		QuerySpec q = new QuerySpec();
		q.find("name").is("Thomas");
		q.find("age").not().mod(22, 2);
		String expected = "{ \"name\" : \"Thomas\" , \"age\" : { \"$not\" : { \"$mod\" : [ 22 , 2]}}}";
		Assert.assertEquals(expected, q.build().getQueryObject().toString());
	}

	@Test
	public void q3() {

		QuerySpec q = new QuerySpec();;
		q.or(
				new QuerySpec().find("name").is("Sven").and("age").lt(50).build(), 
				new QuerySpec().find("age").lt(50).build(),
				new BasicQuery("{'name' : 'Thomas'}")
		);
		String expected = "{ \"$or\" : [ { \"name\" : \"Sven\" , \"age\" : { \"$lt\" : 50}} , { \"age\" : { \"$lt\" : 50}} , { \"name\" : \"Thomas\"}]}";
		Assert.assertEquals(expected, q.build().getQueryObject().toString());
	}
	
}
