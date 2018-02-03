package com.nordea.next.fluentsql;

import static com.nordea.next.fluentsql.TABLE1.TABLE1;
import static com.nordea.next.fluentsql.TABLE2.TABLE2;

import org.junit.Assert;
import org.junit.Test;

import com.nordea.next.fluentsql.Sql.SelectFrom;
import com.nordea.next.fluentsql.Sql.SelectFromWhere;

public class SqlInsertTest {
			
	private static final Column<String> COLA = TABLE1.COLA;
	private static final Column<String> COLB = TABLE1.COLB;
	private static final Column<Integer> ID = TABLE1.ID;
	private static final Column<String> CONTENT = TABLE1.CONTENT;

	private static final Column<String> COLC = TABLE2.COLC;
	private static final Column<String> COLD = TABLE2.COLD;
		
	@Test
	public void insertValuesTest() {
		Sql sql = Sql.InsertInto(TABLE1).columns(COLA,COLB).values("val1", "val2");
		Assert.assertEquals("insert into TABLE1 (COLA,COLB) values (?,?)", sql.toString());		
		Assert.assertEquals(2, sql.parameters().size());	
	}

	@Test
	public void insertSelectTest() {
		
		Sql sql = Sql.InsertInto(TABLE1).columns(COLA,COLB).select(COLA, COLB).from(TABLE2).where(COLA.eq("value1").and(COLB.eq("value2")));
		Assert.assertEquals("insert into TABLE1 (COLA,COLB) select COLA,COLB from TABLE2 where COLA=? and COLB=?", sql.toString());		
		Assert.assertEquals(2, sql.parameters().size());	

		SelectFrom selectFrom = Sql.Select(COLA, COLB).from(TABLE2);		
		Assert.assertEquals("select COLA,COLB from TABLE2", selectFrom.toString());		
		
		sql = Sql.InsertInto(TABLE1).columns(COLA,COLB).select(selectFrom).where(COLA.eq("value1").and(COLB.eq("value2")));
		Assert.assertEquals("insert into TABLE1 (COLA,COLB) select COLA,COLB from TABLE2 where COLA=? and COLB=?", sql.toString());		
		Assert.assertEquals(2, sql.parameters().size());
		
		
		SelectFromWhere selectFromWhere = Sql.Select(COLA, COLB).from(TABLE2).where(COLA.eq("value1").and(COLB.eq("value2")));		
		Assert.assertEquals("select COLA,COLB from TABLE2 where COLA=? and COLB=?", selectFromWhere.toString());		
		
		sql = Sql.InsertInto(TABLE1).columns(COLA,COLB).select(selectFromWhere);
		Assert.assertEquals("insert into TABLE1 (COLA,COLB) select COLA,COLB from TABLE2 where COLA=? and COLB=?", sql.toString());		
		Assert.assertEquals(2, sql.parameters().size());
			
	}


}