/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.mongodb.builder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.document.InvalidDocumentStoreApiUageException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Criteria implements CriteriaSpec {
	
	private QueryBuilder qb = null;
	
	private LinkedHashMap<String, Object> criteria = new LinkedHashMap<String, Object>();

	private Object isValue = null;
	
	
	public Criteria(QueryBuilder qb) {
		super();
		this.qb = qb;
	}


	public Criteria and(String key) {
		return qb.find(key);
	}


	public Criteria is(Object o) {
		if (isValue != null) {
			throw new InvalidDocumentStoreApiUageException("Multiple 'is' values declared.");
		}
		this.isValue = o;
		return this;
	}

	public Criteria lt(Object o) {
		criteria.put("$lt", o);
		return this;
	}
	
	public Criteria lte(Object o) {
		criteria.put("$lte", o);
		return this;
	}
	
	public Criteria gt(Object o) {
		criteria.put("$gt", o);
		return this;
	}
	
	public Criteria gte(Object o) {
		criteria.put("$gte", o);
		return this;
	}
	
	public Criteria in(Object... o) {
		criteria.put("$in", o);
		return this;
	}

	public Criteria nin(Object... o) {
		criteria.put("$min", o);
		return this;
	}

	public Criteria mod(Number value, Number remainder) {
		List<Object> l = new ArrayList<Object>();
		l.add(value);
		l.add(remainder);
		criteria.put("$mod", l);
		return this;
	}

	public Criteria all(Object o) {
		criteria.put("$is", o);
		return this;
	}

	public Criteria size(Object o) {
		criteria.put("$is", o);
		return this;
	}

	public Criteria exists(boolean b) {
		return this;
	}

	public Criteria type(int t) {
		return this;
	}

	public Criteria not() {
		criteria.put("$not", null);
		return this;
	}
	
	public Criteria regExp(String re) {
		return this;
	}

	public void or(List<Query> queries) {
		criteria.put("$or", queries);		
	}
	
	public Query build() {
		return qb.build(); 
	}

	/* (non-Javadoc)
	 * @see org.springframework.datastore.document.mongodb.query.Criteria#getCriteriaObject(java.lang.String)
	 */
	public DBObject getCriteriaObject(String key) {
		DBObject dbo = new BasicDBObject();
		boolean not = false;
		for (String k : criteria.keySet()) {
			if (not) {
				DBObject notDbo = new BasicDBObject();
				notDbo.put(k, criteria.get(k));
				dbo.put("$not", notDbo);
				not = false;
			}
			else {
				if ("$not".equals(k)) {
					not = true;
				}
				else {
					dbo.put(k, criteria.get(k));
				}
			}
		}
		DBObject queryCriteria = new BasicDBObject();
		if (isValue != null) {
			queryCriteria.put(key, isValue);
			queryCriteria.putAll(dbo);
		}
		else {
			queryCriteria.put(key, dbo);
		}
		return queryCriteria;
	}

}
