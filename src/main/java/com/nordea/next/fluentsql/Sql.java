package com.nordea.next.fluentsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Write SQL fluently in Java.   
 * @author g93283
 */
public class Sql {

	SqlStatement sql = new SqlStatement();
	
	private Sql() {		
	}
	
	public String toString() {
		return sql.toString();
	}

	/**
	 * Builds a PreparedStatement with the given sql and binds given parameters.
	 * Caller must close statement (preferably using try-with-resource).
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(toString());
		sql.addParameters(stmt);
		return stmt;
	}
	
	List<SqlStatement.AddParam> parameters() {
		return sql.parameters();
	}
/*
	public static <T> Column<T> column(String name, Class<T> columnType) {
		return new Column<T>(name, columnType);
	}
*/	
	public static Update Update(Table table) {
		return new Update(table);
	}	
	
	public static InsertInto InsertInto(Table table) {
		return new InsertInto(table);
	}	

	public static Select Select(Column... column) {
		return new Select(column);
	}	

	public static DeleteFrom DeleteFrom(Table table) {
		return new DeleteFrom(table);
	}	

	public static final class DeleteFrom extends Sql {
		public DeleteFrom(Table table) {
			sql.append("delete from ").append(table.name());
		}
		
		public DeleteFromWhere where(Expr expr) {
			return new DeleteFromWhere(this, expr);
		}		
	}

	public static final class DeleteFromWhere extends Sql {
		public DeleteFromWhere(DeleteFrom deleteFrom, Expr expr) {
			sql = deleteFrom.sql;
			sql.append(" where ");
			sql.add(expr.sql);
		}

		public boolean execute(Connection con) throws SQLException {
			return sql.execute(con);
		}
	}	
	
	public static final class Update extends Sql {
		public Update(Table table) {
			sql.append("update ").append(table.name());
		}
		
		public <T> UpdateSet set(Column<T> column, T value) {			
			return new UpdateSet(this, column, value);
		}
	}

	public static final class UpdateSet extends Sql {
		public <T> UpdateSet(Update update, Column<T> column, T value) {
			sql = update.sql;
			sql.append(" set ")
				.append(column.name())
				.append("=?");
			sql.addObject(value);
		}
		
		public <T> UpdateSet(UpdateSet updateSet, Column<T> column, T value) {
			sql = updateSet.sql;
			sql.append(", ")
				.append(column.name())
				.append("=?");
			sql.addObject(value);
		}

		public <T> UpdateSet set(Column<T> column, T value) {			
			return new UpdateSet(this, column, value);
		}
		
		public UpdateSetWhere where(Expr expr) {
			return new UpdateSetWhere(this, expr);
		}		
	}
	
	public static final class UpdateSetWhere extends Sql {
		public UpdateSetWhere(UpdateSet updateSet, Expr expr) {
			sql = updateSet.sql;
			sql.append(" where ");
			sql.add(expr.sql);
		}		
		public boolean execute(Connection con) throws SQLException {
			return sql.execute(con);
		}
	}	
	
	public static final class InsertInto extends Sql {
		public InsertInto(Table table) {
			sql.append("insert into ").append(table.name());
		}
		
		public InsertIntoColumns columns(Column... column) {			
			return new InsertIntoColumns(this, column);
		}
		
		public <T> InsertIntoValue value(Column<T> column, T value) {
			return new InsertIntoValue(this, column, value);
		}
	}

	public static final class InsertIntoValue extends Sql {
		SqlStatement sql2 = new SqlStatement();
		public <T> InsertIntoValue(InsertInto insert, Column<T> column, T value) {
			sql = insert.sql;
			sql.append(" (");
			sql.append(column.name());
			
			sql2.append(") values (?");
			sql2.addObject(value);
		}

		public <T> InsertIntoValue(InsertIntoValue insert, Column<T> column, T value) {
			sql = insert.sql;
			sql.append(", ");
			sql.append(column.name());

			
			sql2.append(",?");
			sql2.addObject(value);
		}
		
		public <T> InsertIntoValue value(Column<T> column, T value) {
			return new InsertIntoValue(this, column, value);
		}

		public String toString() {
			sql.add(sql2);
			sql.append(")");
			return sql.toString();
		}

		public boolean execute(Connection con) throws SQLException {
			sql.add(sql2);
			sql.append(")");

			return sql.execute(con);
		}
	}
	
	public static final class InsertIntoColumns extends Sql {
		public InsertIntoColumns(InsertInto insert, Column... column) {
			sql = insert.sql;
			sql.append(" (");
			sql.append(StringUtils.join(column, ","));
			sql.append(")");
		}
		
		public InsertIntoColumnsValues values(Object... params) {			
			return new InsertIntoColumnsValues(this, params);
		}
		
		public InsertIntoColumnsSelect select(Column... column) {
			return new InsertIntoColumnsSelect(this, column);
		}		

		public SelectFrom select(SelectFrom selectFrom) {
			return new SelectFrom(this, selectFrom);
		}		

		public SelectFromWhere select(SelectFromWhere selectFrom) {
			return new SelectFromWhere(this, selectFrom);
		}
	}
	
	public static final class InsertIntoColumnsSelect extends Select {
		public InsertIntoColumnsSelect(InsertIntoColumns insertIntoColumns, Column... column) {
			super(insertIntoColumns, column);
		}
		public InsertIntoColumnsSelectFrom from(Table... tableName) {			
			return new InsertIntoColumnsSelectFrom(this, tableName);
		}		
	}
	
	public static final class InsertIntoColumnsSelectFrom extends SelectFrom {
		public InsertIntoColumnsSelectFrom(InsertIntoColumnsSelect insertIntoColumnsSelect, Table[] tableName) {
			super(insertIntoColumnsSelect, tableName);
		}
		
		public InsertIntoColumnsSelectFromWhere where(Expr expr) {
			return new InsertIntoColumnsSelectFromWhere(this, expr);
		}
	}

	public static final class InsertIntoColumnsSelectFromWhere extends SelectFromWhere {
		public InsertIntoColumnsSelectFromWhere(InsertIntoColumnsSelectFrom insertIntoColumnsSelectFrom, Expr expr) {
			super(insertIntoColumnsSelectFrom, expr);
		}		
		public boolean execute(Connection con) throws SQLException {
			return sql.execute(con);
		}
	}
	
	public static final class InsertIntoColumnsValues extends Sql {
		public InsertIntoColumnsValues(InsertIntoColumns insert, Object... params) {
			sql = insert.sql;
			sql.append(" values (");
			int ix = 0;
			for (Object param: params) {
				if (ix>0) {
					sql.append(",");
				}
				sql.addObject(param);
				sql.append("?");
				ix++;
			}
			sql.append(")");
		}
	}
	
	public static class Select extends Sql {
		public Select(Sql first, String... columnName) {
			if (first!=null) {
				sql = first.sql;
				sql.append(" ");
			}
			sql.append("select ");
			sql.append(StringUtils.join(columnName, ","));			
		}

		public Select(Sql first, Column... column) {
			if (first!=null) {
				sql = first.sql;
				sql.append(" ");
			}
			sql.append("select ");
			sql.append(StringUtils.join(column, ","));			
		}

		public Select(Column... column) {
			this(null, column);
		}

		public SelectFrom from(Table... tableName) {			
			return new SelectFrom(this, tableName);
		}
	}
	
	public static class SelectFrom extends Sql {
		public SelectFrom(Select select, String... tableName) {
			sql = select.sql;
			sql.append(" from ");
			sql.append(StringUtils.join(tableName, ","));
		}

		public SelectFrom(Select select, Table... tableName) {
			sql = select.sql;
			sql.append(" from ");
			sql.append(StringUtils.join(tableName, ","));
		}
		
		public SelectFrom(InsertIntoColumns first, SelectFrom second) {
			first.sql.append(" ");
			first.sql.add(second.sql);
			sql = first.sql;
		}

		public SelectFrom(SelectFromLeftOuterJoin first, Expr expr) {
			first.sql.append(" on ");
			first.sql.add(expr.sql);
			sql = first.sql;
		}

		public SelectFromWhere where(Expr expr) {
			return new SelectFromWhere(this, expr);
		}
		
		public SelectFromWhereOrderBy orderBy(Direction... direction) {
			return new SelectFromWhereOrderBy(this, direction);
		}		
		
		public SelectFromLeftOuterJoin leftOuterJoin(Table tableName) {
			return new SelectFromLeftOuterJoin(this, tableName);
		}
/*		
		public <T> T exactQuery(Connection con, JdbcUtil.RowMapper<T> rowMapper) throws SQLException {
			return sql.exactQuery(con, rowMapper);
		}
*/		
	}

	public static class SelectFromLeftOuterJoin extends Sql {
		public SelectFromLeftOuterJoin(SelectFrom selectFrom, Table tableName) {
			sql = selectFrom.sql;
			sql.append(" left outer join ");
			sql.append(tableName.name());
		}
		
		public SelectFrom on(Expr expr) {
			return new SelectFrom(this, expr);
		}
	}
	
	public static class SelectFromWhere extends Sql {
		public SelectFromWhere(SelectFrom selectFrom, Expr expr) {
			sql = selectFrom.sql;
			sql.append(" where ");
			sql.add(expr.sql);
		}		

		public SelectFromWhere(InsertIntoColumns first, SelectFromWhere second) {
			first.sql.append(" ");
			first.sql.add(second.sql);
			sql = first.sql;
		}
		public SelectFromWhere and(Expr expr) {
			sql.append(" and ");
			sql.add(expr.sql);
			return this;
		}
		public SelectFromWhere or(Expr expr) {
			sql.append(" or ");
			sql.add(expr.sql);
			return this;
		}
		public SelectFromWhereOrderBy orderBy(Direction... direction) {
			return new SelectFromWhereOrderBy(this, direction);
		}				
/*		
		public <T> T exactQuery(Connection con, JdbcUtil.RowMapper<T> rowMapper) throws SQLException {
			return sql.exactQuery(con, rowMapper);
		}
*/		
	}

	public static final class SelectFromWhereOrderBy extends Sql {
		public SelectFromWhereOrderBy(SelectFromWhere selectFromWhere, Direction... direction) {
			sql = selectFromWhere.sql;
			sql.append(" order by ");
			sql.append(StringUtils.join(direction, ","));
		}
		public SelectFromWhereOrderBy(SelectFrom selectFrom, Direction... direction) {
			sql = selectFrom.sql;
			sql.append(" order by ");
			sql.append(StringUtils.join(direction, ","));
		}
/*		
		public <T> T exactQuery(Connection con, JdbcUtil.RowMapper<T> rowMapper) throws SQLException {
			return sql.exactQuery(con, rowMapper);
		}
*/		
	}

	public static Binary and(Expr a, Expr b) {
		return new Binary(a, " and ", b);
	}

	public static Binary and(Expr a, Binary b) {
		return new Binary(a, " and ", b);
	}

	public static Binary and(Binary a, Expr b) {
		return new Binary(a, " and ", b);
	}

	public static Binary and(Binary a, Binary b) {
		return new Binary(a, " and ", b);
	}
	
	public static Binary or(Expr a, Expr b) {
		return new Binary(a, " or ", b);
	}
	
	public static Binary or(Expr a, Binary b) {
		return new Binary(a, " or ", b);
	}

	public static Binary or(Binary a, Expr b) {
		return new Binary(a, " or ", b);
	}

	public static Binary or(Binary a, Binary b) {
		return new Binary(a, " or ", b);
	}

	static class Expr {
		SqlStatement sql;
		public String toString() {
			return sql.toString();
		}
		List<SqlStatement.AddParam> parameters() {
			return sql.parameters();
		}
	}
/*
	static final class And extends Binary {
		public And(Expr a, Expr b) {
			super(a, "and", b);
		}
	}
*/	
	public static class Binary extends Expr {
		public Binary(Expr a, String binaryOperator, Expr b) {
			sql = a.sql;			
			sql.append(binaryOperator);
			sql.add(b.sql);
		}
		public Binary(Binary a, String binaryOperator, Expr b) {
			sql = new SqlStatement("(");
			sql.add(a.sql);			
			sql.append(")");
			sql.append(binaryOperator);
			sql.add(b.sql);
		}
		public Binary(Expr a, String binaryOperator, Binary b) {
			sql = a.sql;			
			sql.append(binaryOperator);
			sql.append("(");
			sql.add(b.sql);
			sql.append(")");
		}
		public Binary(Binary a, String binaryOperator, Binary b) {
			sql = new SqlStatement("(");
			sql.add(a.sql);			
			sql.append(")");
			sql.append(binaryOperator);
			sql.append("(");
			sql.add(b.sql);
			sql.append(")");
		}
		public String toString() {
			return sql.toString();
		}
	}
	
	public static <T> Compare eq(Column<T> column, T value) {
		return new Compare(column, Compare.EQ, value);
	}

	public static class Compare extends Expr {
		public static final String EQ = "=";
		public static final String LT = "<";
		public static final String LE = "<=";
		public static final String GT = ">";
		public static final String GE = ">=";
		public static final String LIKE = " like ";
		public static final String IS_NULL = " is null";
		public static final String IS_NOT_NULL = " is not null";

		public <T> Compare(Column<T> column, String operator) {
			sql = new SqlStatement();
			sql.append(column.name());
			sql.append(operator);
		}
		
		public <T> Compare(Column<T> column, String operator, T value) {
			sql = new SqlStatement();
			sql.append(column.name());
			sql.append(operator);
			sql.append("?");
			sql.addObject(value);
		}

		public <T> Compare(Column<T> column, String operator, Column<T> column2) {
			sql = new SqlStatement();
			if (column.name().equals(column2.name())) {
				sql.append(column.tableName()).append(".").append(column.name());
				sql.append(operator);
				sql.append(column2.tableName()).append(".").append(column2.name());
			} else {
				sql.append(column.name());
				sql.append(operator);
				sql.append(column2.name());
			}
		}
		
		public Binary and(Expr b) {
			return new Binary(this, " and ", b);
		}

		public Binary and(Binary b) {
			return new Binary(this, " and ", b);
		}

		public Binary or(Expr b) {
			return new Binary(this, " or ", b);
		}
		
		public Binary or(Binary b) {
			return new Binary(this, " or ", b);
		}

		public String toString() {
			return sql.toString();
		}
	}
/*
	static final class Eq extends Compare {
		public Eq(String columnName, String value) {
			super(columnName, Compare.EQ, value);
		}
	}
*/
/*
	static final class Like extends Expr {
		public Like(String columnName, String value) {
			sql = new SqlStatement();
			sql.append(columnName);
			sql.append(" like ?");
			sql.addString(value);
		}
		public String toString() {
			return sql.toString();
		}
	}
*/	
	
	
	public static final class Direction extends Column {
		final String sql;
		public Direction(Column column, String dir) {
			super(column);
			this.sql = column.name()+" "+dir;
		}
		public String toString() {
			return sql;
		}
	}
}
