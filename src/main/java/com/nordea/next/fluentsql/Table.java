package com.nordea.next.fluentsql;



/**
 * Define a table by extending Table and include static Column members.
 * Also define a singleton with the name of your table.
 * You may also define singletons for each of the columns to avoid prefixing table name.
 * <pre>
 *	static final class TABLE1 extends Table {
 *		public TABLE1() {
 *			super(TABLE1.class.getSimpleName());
 *		}
 *		public static final Column<String> COLA = new Column("COLA", String.class);
 *		public static final Column<Integer> ID = new Column("ID", Integer.class);
 *		public static final Column<String> CONTENT = new Column("CONTENT", String.class);
 *	}
 *	private static final TABLE1 TABLE1 = new TABLE1();
 *	private static final Column<String> COLA = TABLE1.COLA;
 *	private static final Column<Integer> ID = TABLE1.ID;
 *	private static final Column<String> CONTENT = TABLE1.CONTENT;
 * </pre>
 * @author G93283
 *
 */
public abstract class Table {
	private final String name;

	public Table() {
		name = getClass().getSimpleName();
	}
	public String name() {
		return name;
	}

	public String toString() {
		return name();
	}
}

