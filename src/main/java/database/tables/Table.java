package main.java.database.tables;

import java.sql.*;

public interface Table {
	void create(Connection connection);
	
	String getFieldsQuery();
}
