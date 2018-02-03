package com.nordea.next.fluentsql;

import static com.nordea.next.fluentsql.TABLE1.TABLE1;
import static com.nordea.next.fluentsql.TABLE2.TABLE2;

import org.junit.Assert;
import org.junit.Test;

public class SqlDeleteTest {
			
	public static final Column<String> COLA = TABLE1.COLA;
	public static final Column<String> COLB = TABLE1.COLB;
	public static final Column<Integer> ID = TABLE1.ID;
	public static final Column<String> CONTENT = TABLE1.CONTENT;

	private static final Column<String> COLC = TABLE2.COLC;
	private static final Column<String> COLD = TABLE2.COLD;
	
	
	@Test
	public void deleteTest() {		
		Sql sql = Sql.DeleteFrom(TABLE1).where(COLA.eq("value1"));
		Assert.assertEquals("delete from TABLE1 where COLA=?", sql.toString());		
		Assert.assertEquals(1, sql.parameters().size());	
	}

}