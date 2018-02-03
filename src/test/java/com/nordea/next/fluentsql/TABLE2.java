package com.nordea.next.fluentsql;

public class TABLE2 extends Table {
	public static final String T = "TABLE2";
	public final Column<String> COLC = new Column<String>(T, "COLC", String.class);;
	public final Column<String> COLD = new Column<String>(T, "COLD", String.class);
	public final Column<Integer> ID = new Column<Integer>(T, "ID", Integer.class);
	
	public static final TABLE2 TABLE2 = new TABLE2();	
}
