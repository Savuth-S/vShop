package main.java.database.tables;

import main.java.database.Database;

import java.sql.SQLException;
import java.util.Arrays;

public abstract class Table {
	private String tbName;
	public static final String ID = "id";
	private String[] fields;

	protected void setTableName(String tbName) {this.tbName = tbName;}
	protected void setFields(String[] fields) { this.fields = fields; }

	public boolean create()
	{
		try (Database db = Database.getInstance()){
			if (db.execute(String.format(
					"CREATE TABLE IF NOT EXISTS %s (%s %s)",
					tbName, getFieldsQuery(), getUniques())) != null){
				return true;
			}
		} catch (SQLException e) {e.printStackTrace();}

		return false;
	}

	private String getFieldsQuery()
	{
		StringBuilder sb = new StringBuilder();
		Arrays.stream(fields).forEach(s -> {
			if (s.equals(ID)){
				sb.append(String.format("%s int AUTO_INCREMENT", s));
			}else{
				sb.append(String.format(", %s VARCHAR(100) NOT NULL", s));
			}
		});

		return sb.toString();
	}
	
	String getUniques(){ return String.format(", PRIMARY KEY(%s)",ID); }
}
