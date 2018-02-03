package com.nordea.next.fluentsql;

import static com.nordea.next.fluentsql.Column.ROWNUM;
import static com.nordea.next.fluentsql.Sql.and;
import static com.nordea.next.fluentsql.Sql.or;
import static com.nordea.next.fluentsql.TABLE1.TABLE1;
import static com.nordea.next.fluentsql.TABLE2.TABLE2;

import java.sql.Timestamp;

import org.junit.Assert;
import org.junit.Test;

public class SqlJoinTest {
				
	private static final Column<String> COLA = TABLE1.COLA;
	private static final Column<String> COLB = TABLE1.COLB;
	private static final Column<Integer> ID = TABLE1.ID;
	private static final Column<String> CONTENT = TABLE1.CONTENT;

	private static final Column<String> COLC = TABLE2.COLC;
	private static final Column<String> COLD = TABLE2.COLD;
	
	@Test
	public void selectLeftOuterJoinTest() {
		
		Sql sql = Sql.Select(COLA).from(TABLE1).leftOuterJoin(TABLE2).on(TABLE1.ID.eq(TABLE2.ID));
		Assert.assertEquals("select COLA from TABLE1 left outer join TABLE2 on TABLE1.ID=TABLE2.ID", sql.toString());		
		Assert.assertEquals(0, sql.parameters().size());

		sql = Sql.Select(COLA).from(TABLE1).leftOuterJoin(TABLE2).on(TABLE1.ID.eq(TABLE2.ID).and(COLA.gt("abc")));
		Assert.assertEquals("select COLA from TABLE1 left outer join TABLE2 on TABLE1.ID=TABLE2.ID and COLA>?", sql.toString());		
		Assert.assertEquals(1, sql.parameters().size());
	
	}

	
}