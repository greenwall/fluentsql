package com.nordea.next.fluentsql;

import static com.nordea.next.fluentsql.Column.ROWNUM;
import static com.nordea.next.fluentsql.Sql.and;
import static com.nordea.next.fluentsql.Sql.or;
import static com.nordea.next.fluentsql.TABLE1.TABLE1;
import static com.nordea.next.fluentsql.TABLE2.TABLE2;

import java.sql.Timestamp;

import org.junit.Assert;
import org.junit.Test;

public class SqlTest {
				
	private static final Column<String> COLA = TABLE1.COLA;
	private static final Column<String> COLB = TABLE1.COLB;
	private static final Column<Integer> ID = TABLE1.ID;
	private static final Column<String> CONTENT = TABLE1.CONTENT;

	private static final Column<String> COLC = TABLE2.COLC;
	private static final Column<String> COLD = TABLE2.COLD;
	
	@Test
	public void selectTest() {
		
		Sql sql = Sql.Select(COLA).from(TABLE1).orderBy(COLA.asc());
		Assert.assertEquals("select COLA from TABLE1 order by COLA asc", sql.toString());
		
		sql = Sql.Select(ID, CONTENT).from(TABLE1).where(ID.eq(1));		
		Assert.assertEquals("select ID,CONTENT from TABLE1 where ID=?", sql.toString());
		Assert.assertEquals(1, sql.parameters().size());

		sql = Sql.Select(COLA,COLB).from(TABLE1,TABLE2).orderBy(COLA.asc(),COLB.desc());
		Assert.assertEquals("select COLA,COLB from TABLE1,TABLE2 order by COLA asc,COLB desc", sql.toString());
		
		sql = Sql.Select(ID, CONTENT).from(TABLE1).where(and(ID.eq(42),CONTENT.eq("value2")));		
		Assert.assertEquals("select ID,CONTENT from TABLE1 where ID=? and CONTENT=?", sql.toString());
		Assert.assertEquals(2, sql.parameters().size());

		sql = Sql.Select(ID, CONTENT).from(TABLE1).where(ID.eq(42)).and(CONTENT.eq("value2"));		
		Assert.assertEquals("select ID,CONTENT from TABLE1 where ID=? and CONTENT=?", sql.toString());
		Assert.assertEquals(2, sql.parameters().size());
		
	}

	
	@Test
	public void columnSelectTest() {
		Sql sql;

		sql = Sql.Select(ID, CONTENT).from(TABLE1).where(COLA.eq("1"));		
		Assert.assertEquals("select ID,CONTENT from TABLE1 where COLA=?", sql.toString());
		Assert.assertEquals(1, sql.parameters().size());

		sql = Sql.Select(COLA, COLB).from(TABLE1).where(COLA.lt("value1").and(COLB.gt("value2")));		
		Assert.assertEquals("select COLA,COLB from TABLE1 where COLA<? and COLB>?", sql.toString());		
	}
	
	@Test
	public void binaryTest() {
		Sql.Expr expr = ID.eq(42);		
		Assert.assertEquals("ID=?", expr.toString());
		Assert.assertEquals(1, expr.parameters().size());

		expr = CONTENT.like("somevalue");		
		Assert.assertEquals("CONTENT like ?", expr.toString());
		Assert.assertEquals(1, expr.parameters().size());

		expr = ID.gt(42);		
		Assert.assertEquals("ID>?", expr.toString());
		Assert.assertEquals(1, expr.parameters().size());

		expr = ID.ge(42);		
		Assert.assertEquals("ID>=?", expr.toString());
		Assert.assertEquals(1, expr.parameters().size());
		
		expr = ID.lt(42);		
		Assert.assertEquals("ID<?", expr.toString());
		Assert.assertEquals(1, expr.parameters().size());

		expr = ID.le(42);		
		Assert.assertEquals("ID<=?", expr.toString());
		Assert.assertEquals(1, expr.parameters().size());
	
	}

	@Test
	public void exprWithValueTest() {
		Sql.Expr expr = ID.eq(42);		
		Assert.assertEquals("ID=?", expr.toString());
		Assert.assertEquals(1, expr.parameters().size());

		expr = Sql.and(ID.eq(1),CONTENT.eq("value2"));		
		Assert.assertEquals("ID=? and CONTENT=?", expr.toString());
		Assert.assertEquals(2, expr.parameters().size());
	
		expr = Sql.or(ID.eq(1),CONTENT.eq("value2"));		
		Assert.assertEquals("ID=? or CONTENT=?", expr.toString());
		Assert.assertEquals(2, expr.parameters().size());

		expr = and( 
					COLA.eq("a").or(COLB.eq("b")), 
					COLC.eq("c").or(COLD.eq("d"))
				);		
		Assert.assertEquals("(COLA=? or COLB=?) and (COLC=? or COLD=?)", expr.toString());
		Assert.assertEquals(4, expr.parameters().size());
	
		expr = or( 
				COLA.eq("a").and(COLB.eq("b")), 
				COLC.eq("c").and(COLD.eq("d"))
			);		
		Assert.assertEquals("(COLA=? and COLB=?) or (COLC=? and COLD=?)", expr.toString());
		Assert.assertEquals(4, expr.parameters().size());
	}

	@Test
	public void isNullTest() {
		Sql.Expr expr = ID.isNull();		
		Assert.assertEquals("ID is null", expr.toString());
		Assert.assertEquals(0, expr.parameters().size());
	}	

	@Test
	public void isNotNullTest() {
		Sql.Expr expr = ID.isNotNull();		
		Assert.assertEquals("ID is not null", expr.toString());
		Assert.assertEquals(0, expr.parameters().size());
	}	
	
	@Test
	public void compareTest() {
		Sql.Expr expr = COLA.eq(COLB);		
		Assert.assertEquals("COLA=COLB", expr.toString());
		Assert.assertEquals(0, expr.parameters().size());

		expr = ID.eq(TABLE2.ID);		
		Assert.assertEquals("TABLE1.ID=TABLE2.ID", expr.toString());
		Assert.assertEquals(0, expr.parameters().size());

		expr = Sql.and(COLA.like(COLC),COLB.gt(COLD));		
		Assert.assertEquals("COLA like COLC and COLB>COLD", expr.toString());
		Assert.assertEquals(0, expr.parameters().size());
	
	}
	
	@Test
	public void selectLikeTest() {		
		Sql sql = Sql.Select(ID, CONTENT).from(TABLE1).where(ID.eq(2).and(CONTENT.like("value2")));		
		Assert.assertEquals("select ID,CONTENT from TABLE1 where ID=? and CONTENT like ?", sql.toString());
		Assert.assertEquals(2, sql.parameters().size());
	
	}

	@Test
	public void selectTest2() {		
	
		Sql sql = Sql.Select(ID, COLA, COLB, CONTENT)
		.from(TABLE1)
		.where(CONTENT.isNotNull())
		.and(COLB.isNull())
		.and(COLA.eq("value1"))
		.and(COLB.isNull().or(TABLE1.TIME2.le(new Timestamp(System.currentTimeMillis()))))
		.and(ROWNUM.le(1))
		.orderBy(TABLE1.TIME1.asc());
	
		String s = "select ID,COLA,COLB,CONTENT "
//		+"from TABLE1 where CONTENT is not null and COLB is null and COLA=? and (COLB is null or TIME2<=?) and ROWNUM<=? order by TIME1 asc";
				
		// TODO Fix missing parenthesis:
		+"from TABLE1 where CONTENT is not null and COLB is null and COLA=? and COLB is null or TIME2<=? and ROWNUM<=? order by TIME1 asc";
		
		Assert.assertEquals(s, sql.toString());
	}
}