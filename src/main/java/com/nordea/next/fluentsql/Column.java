package com.nordea.next.fluentsql;

import com.nordea.next.fluentsql.Sql.Compare;
import com.nordea.next.fluentsql.Sql.Direction;

public class Column<T> {
	private final String tableName;
	private final String name;
	private final Class<T> columnType;
	
	public static final Column<Integer> ROWNUM = new Column<Integer>("", "ROWNUM", Integer.class);
/*	
	public Column(Class<T> columnType) {
		this.name = getClass().getSimpleName();
		this.columnType = columnType;
	}
*/	
	public Column(Column c) {
		this(c.tableName, c.name, c.columnType);
	}
	
	public Column(String tableName, String name, Class<T> columnType) {
		this.tableName = tableName;
		//name = getClass().getSimpleName();
		this.name = name;
		this.columnType = columnType;
	}
	
	public String name() {
		return name;
	}
	
	public String tableName() {
		return tableName;
	}
	
	public Compare eq(T value) {
		return new Sql.Compare(this, Compare.EQ, value);			
	}
	
	public Compare lt(T value) {
		return new Sql.Compare(this, Compare.LT, value);
	}

	public Compare le(T value) {
		return new Sql.Compare(this, Compare.LE, value);
	}

	public Compare gt(T value) {
		return new Sql.Compare(this, Compare.GT, value);
	}

	public Compare ge(T value) {
		return new Sql.Compare(this, Compare.GE, value);
	}
	
	public Compare like(T value) {
		return new Sql.Compare(this, Compare.LIKE, value);
	}

	public Compare eq(Column<T> column2) {
		return new Sql.Compare(this, Compare.EQ, column2);			
	}
	
	public Compare lt(Column<T> column2) {
		return new Sql.Compare(this, Compare.LT, column2);
	}

	public Compare le(Column<T> column2) {
		return new Sql.Compare(this, Compare.LE, column2);
	}

	public Compare gt(Column<T> column2) {
		return new Sql.Compare(this, Compare.GT, column2);
	}

	public Compare ge(Column<T> column2) {
		return new Sql.Compare(this, Compare.GE, column2);
	}
	
	public Compare like(Column<T> column2) {
		return new Sql.Compare(this, Compare.LIKE, column2);
	}
	
	public Compare isNull() {
		return new Sql.Compare(this, Compare.IS_NULL);
	}

	public Compare isNotNull() {
		return new Sql.Compare(this, Compare.IS_NOT_NULL);
	}

	public Direction asc() {
		return new Direction(this, "asc");
	}

	public Direction desc() {
		return new Direction(this, "desc");
	}

	public String toString() {
		return name();
	}

	public Class<T> getColumnType() {
		return columnType;
	}
}

