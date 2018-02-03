package com.nordea.next.fluentsql;

import static com.nordea.next.fluentsql.TABLE1.TABLE1;
import static com.nordea.next.fluentsql.TABLE2.TABLE2;

import org.junit.Assert;
import org.junit.Test;

public class SqlUpdateTest {
	
	private static final Column<String> COLA = TABLE1.COLA;
	private static final Column<String> COLB = TABLE1.COLB;
	private static final Column<Integer> ID = TABLE1.ID;
	private static final Column<String> CONTENT = TABLE1.CONTENT;

	private static final Column<String> COLC = TABLE2.COLC;
	private static final Column<String> COLD = TABLE2.COLD;
	
	
	@Test
	public void updateTest() {
		
		Sql sql = Sql.Update(TABLE1).set(COLA,"value1").set(COLB,"2").where(COLC.eq("value1"));
		Assert.assertEquals("update TABLE1 set COLA=?, COLB=? where COLC=?", sql.toString());		
		Assert.assertEquals(3, sql.parameters().size());	

	}

}