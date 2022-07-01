package sav.manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Arrays;
import java.util.NoSuchElementException;

import java.util.logging.Logger;

import sav.vshop.database.DbHelper;

public abstract class Table 
{
	

	private static final Logger LOGGER = Logger.getLogger(Table.class.getName());

	private DbHelper db;
	private String tbName;
	private String[] fields;
        
	public static final String ID = "id";

	protected Table(DbHelper db, String tbName, String[] fields)
	{
		this.db = db;
		this.tbName = tbName;	
		this.fields = fields;
	}

	public boolean create()
	{
		String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s %s)", tbName, getFieldsQuery(), getUniques());
			
		try{
			return db.execute(sql) != null;
		}catch (SQLException e){
			LOGGER.severe(String.format("FAILED TO MAKE TABLE: %s %nERROR: %s", tbName, e.getMessage()));
		}

		return false;
	}
	
	public ResultSet insert(String[] fields, Object[] data)
	{
	    try(DbHelper db = DbHelper.getInstance()) {
			String query = String.format("INSERT INTO %s (%s) VALUES (%s)", tbName, formatFields(fields), formatFieldsQueryCount(fields));

			try (PreparedStatement insertValues = db.getConnection().prepareStatement(query)){
				//gets type of variable and prepares that type for insert on database
				for (int i = 0; i < formatFields(fields).split(",").length; i++){
					// switch(true){
					// 	case data[i] instanceof byte[] bytes:
					// 		insertValues.setBytes(i+1, bytes);
					// 		break;
					// 	case data[i] instanceof Integer integer:
					// 		insertValues.setInt(i+1, integer);
					// 		break;
					// 	case data[i] instanceof String string:
					// 		insertValues.setString(i+1, string);
					// 		break;
					// 	default:
					// 		LOGGER.warning(String.format("INSERT VALUE TYPE NOT FOUND: %s", data[i]));
					// }

                	if (data[i] instanceof byte[]){
                    	insertValues.setBytes(i+1, (byte[]) data[i]);
                	}else if (data[i] instanceof Integer){
                    	insertValues.setInt(i+1, (Integer) data[i]);
                    }else if (data[i] instanceof String){
						insertValues.setString(i+1, (String) data[i]);
                	}else{
                    	LOGGER.warning(String.format("INSERT VALUE TYPE NOT FOUND: %s", data[i]));
                    }
				}

			    return insertValues.executeQuery();
		    }catch (SQLException e){ 
				LOGGER.warning(String.format("FAILED TO INSERT ENTRY: %s %nMESSAGE: %s", query, e.getMessage())); 
            }catch(NoSuchElementException e){
				LOGGER.warning(String.format("CONNECTION DOES NOT EXISTS: %s", e.getMessage())); 
			}
		}

		return null;
	}


	protected abstract String getFieldsQuery();
	
	protected boolean entryExists(String field, String value)
	{
		try{
			return db.execute(String.format("SELECT * FROM %s WHERE %s = %s LIMIT 1", tbName, field, value)).first();
        }catch (SQLException e) {
			LOGGER.warning(String.format("FAILED TO CHECK FOR ENTRY: %s %nMESSAGE: %s", value, e.getMessage()));
		}

    	return false;
    }

	protected String getUniques(){ return String.format(", PRIMARY KEY(%s)", ID); }


	/**
	 * FORMATS FIELDS LIST FOR PROPER SQL QUERY INTERPRETATION
	 * @param fields Field list to format for the SQL query
	 * @returns String with expected format for field list
	 * */
    private String formatFields(String[] fields)
	{
		StringBuilder sb = new StringBuilder();
		
		Arrays.stream(fields).forEach(field -> {
			if (!field.equals(ID)){
				if (field.equals(fields[1])){
					sb.append(field);
				}else{
					sb.append(String.format(", %s",field));
				}
			}
		});

		return sb.toString();
	}

	/**
	 * GETS FIELDS COUNT AND REPLACES THEM WITH EXPECTED "TO REPLACE" CHARACTER FOR PROPER PreparedStatement SQL QUERY EXECUTION 
	 * @param fields Field list to format for the SQL query
	 * @returns String with expected amount of "to replace" characters
	 * */
	private String formatFieldsQueryCount(String[] fields)
	{
		StringBuilder sb = new StringBuilder();

		Arrays.stream(fields).forEach(field -> {
			if (!field.equals(ID)){
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
