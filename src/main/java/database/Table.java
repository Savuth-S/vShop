package main.java.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Arrays;

import java.util.logging.Logger;

import main.java.database.Database;

public abstract class Table {
        private static final Logger LOGGER = Logger.getLogger(Table.class.getName());

	private String tbName;
	private String[] fields;
        
        public static final String ID = "id";

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

        public ResultSet insert(String[] fields, Object[] data){
	        try(Database db = Database.getInstance()) {
		        String query = String.format("INSERT INTO %s (%s) VALUES (%s)",
		                tbName,
				formatFields(fields),
				formatFieldsQueryCount(fields));

		        try (PreparedStatement insertValues = db.getConnection().prepareStatement(query)){
			        for (int i = 0; i < formatFields(fields).split(",").length; i++){
                                        if (data[i] instanceof byte[] bytes){
                                                insertValues.setBytes(i+1, bytes);
                                        }else if (data[i] instanceof Integer integer){
                                                insertValues.setInt(i+1, integer);
                                        }else if (data[i] instanceof String string){
					        insertValues.setString(i+1, string);
                                        }else{
                                                LOGGER.warning(String.format("INSERT VALUE TYPE NOT FOUND: %s", data[i]));
                                        }
				}

			        return insertValues.executeQuery();
		        }catch (SQLException e){ LOGGER.warning(String.format("FAILED TO INSERT ENTRY: %s %nMESSAGE: %s", query, e.getMessage())); }
                }

		return null;
	}


	protected String getFieldsQuery()
	{
	        StringBuilder sb = new StringBuilder();
	        Arrays.stream(fields).forEach(s -> {
                        switch(s){
                                case ID -> sb.append(String.format("%s int AUTO_INCREMENT", s));
                                case "guid" -> sb.append(String.format(", %s BINARY(16) NOT NULL", s));
                                default -> sb.append(String.format(", %s VARCHAR(30) NOT NULL", s));
                        }
	        });
                
	        return sb.toString();
	}

        protected boolean entryExists(String field, String value)
        {
                try (Database db = Database.getInstance()) {
			return db.execute(String.format("SELECT * FROM %s WHERE %s = %s LIMIT 1", tbName, field, value)).first();
                }catch (SQLException e) {
			LOGGER.warning(String.format("FAILED TO CHECK FOR ENTRY %s", e.getMessage()));
		}

                return false;
        }
	
	protected String getUniques(){ return String.format(", PRIMARY KEY(%s)",ID); }

        private String formatFields(String[] fields)
	{
		StringBuilder sb = new StringBuilder();
		Arrays.stream(fields).forEach(field -> {
			if (!field.equals(Table.ID)){
				if (field.equals(fields[1])){
					sb.append(field);
				}else{
					sb.append(String.format(", %s",field));
				}
			}
		});

		return sb.toString();
	}

	private String formatFieldsQueryCount(String[] fields)
	{
		StringBuilder sb = new StringBuilder();
		Arrays.stream(fields).forEach(field -> {
			if (!field.equals(Table.ID)){
				if (field.equals(fields[1])){
					sb.append('?');
				}else{
					sb.append(", ?");
				}
			}
		});

		return sb.toString();
	}
}
