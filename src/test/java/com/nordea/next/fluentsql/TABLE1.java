package com.nordea.next.fluentsql;

import static com.nordea.next.fluentsql.TABLE1.TABLE1;

import java.sql.Timestamp;

public class TABLE1 extends Table {
	public static final String T = "TABLE1";
/*		
	static final class Column<T> extends com.nordea.branchchannel.jdbc.Column<T> {
		public Column(String name, Class<T> columnType) {
			super("TABLE1", name, columnType);
		}			
	}
*/		
//	static final class COLA extends Column<String> { }

//	public static final Column<String> COLA = new COLA(TABLE1);//new Column<String>("COLA", String.class);;
	public final Column<String> COLA = new Column<String>(T, "COLA", String.class);;
	public final Column<String> COLB = new Column<String>(T, "COLB", String.class);
	public final Column<Integer> ID = new Column<Integer>(T, "ID", Integer.class);
	public final Column<String> CONTENT = new Column<String>(T, "CONTENT", String.class);
	public final Column<Timestamp> TIME1 = new Column<Timestamp>(T, "TIME1", Timestamp.class);
	public final Column<Timestamp> TIME2 = new Column<Timestamp>(T, "TIME2", Timestamp.class);
	
	public static final TABLE1 TABLE1 = new TABLE1();
		
}

