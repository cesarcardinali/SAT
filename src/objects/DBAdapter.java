package objects;


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import core.SharedObjs;


/**
 * DataBase Connection Class It has a constructor to create a connection and auxiliary methods to automatically generate query on database.
 *
 */
public class DBAdapter
{
	private static final String DB_DRIVER		 = "com.mysql.jdbc.Driver";
	private String			  DB_CONNECTION	 = null;
	private String			  DB_USER		   = null;
	private String			  DB_PASSWORD	   = null;
	private Connection		  dbConnection	  = null;
	private PreparedStatement   preparedStatement = null;
	
	/**
	 * Constructor with connection parameter. For special cases when database connections will not be the default. Write the full path to
	 * database including 'jdbc', 'mysql' identifiers and the schema.
	 * <p>
	 * <b>Example:</b> jdbc:mysql://127.0.0.1/sat
	 * </p>
	 * 
	 * @param connection Connection String identifying the database connection
	 * @param user User String from database access
	 * @param password Password String from database access
	 * @throws SQLException If there is a database connection error
	 */
	public DBAdapter(String connection, String user, String password) throws SQLException
	{
		this.DB_CONNECTION = connection;
		this.DB_USER = user;
		this.DB_PASSWORD = password;
		
		this.dbConnection = getDBConnection();
		
	}
	
	/**
	 * Constructor with default connection. User and Password should come as parameters to create a valid Database connection. Default jdbc
	 * address is set in the XML DB file.
	 * 
	 * @param user User String from database access
	 * @param password Password String from database access
	 * @throws SQLException If there is a database connection error
	 */
	public DBAdapter(String user, String password) throws SQLException
	{
		this.DB_CONNECTION = "jdbc:mysql://172.16.20.15/sat_db"; // change hardcoded line to a default config in a xml file
		this.DB_USER = user;
		this.DB_PASSWORD = password;
		
		this.dbConnection = getDBConnection();
		
	}
	
	/**
	 * @throws SQLException
	 */
	public DBAdapter() throws SQLException
	{
		this.DB_CONNECTION = "jdbc:mysql://172.16.20.15/sat_db"; // change hardcoded line to a default config in a xml file
		this.DB_USER = "user";
		this.DB_PASSWORD = "user";
		
		this.dbConnection = getDBConnection();
		
	}
	
	/**
	 * Support method to work with constructor creating the DataBase connection
	 * 
	 * @return DataBase connection
	 * @throws SQLException If there is a database connection error
	 */
	private Connection getDBConnection() throws SQLException
	{
		
		try
		{
			Class.forName(DB_DRIVER);
		}
		catch (ClassNotFoundException e)
		{
			System.out.println(e.getMessage());
		}
		
		dbConnection = DriverManager.getConnection(this.DB_CONNECTION, this.DB_USER, this.DB_PASSWORD);
		
		return dbConnection;
	}
	
	/**
	 * Support method to set all fields in a CustomFilterItem. The ResultSet parameter should be already in the right row to ensure the
	 * CustomFilterItem will have the proper values. Fields that will be set:
	 * <p>
	 * - Filter Name - Header - Regex - Owner - Where to Main - Where to Kernel - Where to System - Where to Bugreport - Where to Report
	 * Output - Is Shared
	 * </p>
	 * 
	 * @param fitem A CustomFilterItem object to be filled
	 * @param rs A ResultSet containing all the values in the right row
	 * @return Object filled with desired values
	 * @throws SQLException When it occurred a problem to access fields in the ResultSet
	 */
	private CustomFilterItem setAllFilterFields(CustomFilterItem fitem, ResultSet rs) throws SQLException
	{
		fitem.setName(rs.getString("name"));
		fitem.setHeader(rs.getString("header"));
		fitem.setRegex(rs.getString("regex"));
		fitem.setOwner(rs.getString("user_key"));
		fitem.setKernel(byteToBool(rs.getByte("w_krnl")));
		fitem.setRadio(byteToBool(rs.getByte("w_radio")));
		fitem.setMain(byteToBool(rs.getByte("w_main")));
		fitem.setBugreport(byteToBool(rs.getByte("w_bugr")));
		fitem.setSystem(byteToBool(rs.getByte("w_syst")));
		fitem.setRoutput(byteToBool(rs.getByte("w_rout")));
		fitem.setShared(byteToBool(rs.getByte("shared")));
		fitem.setActive(byteToBool(rs.getByte("active")));
		fitem.setLastUpdate(rs.getTimestamp("last_modified").toString());
		if (rs.getString("user_key").equals(""))
			fitem.setEditable(true);
		else
			fitem.setEditable(false);
		
		return fitem;
	}
	
	/**
	 * Support method to convert "boolean" to "byte"
	 * 
	 * @param boolVar Boolean variable to convert
	 * @return Desired byte
	 */
	private byte boolToByte(boolean boolVar)
	{
		byte bVar = (byte) ((boolVar) ? 1 : 0);
		
		return bVar;
	}
	
	/**
	 * Support method to convert "byte" to "boolean"
	 * 
	 * @param bVar Byte variable to convert
	 * @return Desired boolean
	 */
	private boolean byteToBool(byte bVar)
	{
		boolean boolVar = false;
		if (bVar == 1)
			boolVar = true;
		
		return boolVar;
	}
	
	/**
	 * Support method only to print all filter names on a SELECT *
	 * 
	 */
	public void printNamesSelectAll()
	{
		String selectSQL = "SELECT * FROM Filters";
		
		try
		{
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next())
			{
				
				String userid = rs.getString("name");
				
				System.out.println("userid : " + userid);
				
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
		
	}
	
	/**
	 * Get all filters and return them on a CustomFiltersList
	 * 
	 * @return A CustomFiltersList (ArrayList) with CustomFilterItem objects containing all filters
	 */
	public CustomFiltersList selectAllFilters()
	{
		String selectSQL = "SELECT * FROM Filters";
		CustomFilterItem aux = new CustomFilterItem();
		CustomFiltersList flist = new CustomFiltersList();
		
		try
		{
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next())
			{
				aux = new CustomFilterItem();
				flist.add(setAllFilterFields(aux, rs));
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
		
		return flist;
		
	}
	
	/**
	 * Search and returns an extended ArrayList customized for Filters containing all Filter Items that user is the owner. <b>Uses
	 * SharedObjs.getUser() to determine username</b>.
	 * 
	 * @return An ArrayList containing all Filter Items under the user name.
	 */
	public CustomFiltersList myFilters()
	{
		String selectSQL = "SELECT * FROM Filters WHERE user_key = '" + SharedObjs.getUser() + "';";
		CustomFilterItem aux = new CustomFilterItem();
		CustomFiltersList flist = new CustomFiltersList();
		
		try
		{
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next())
			{
				aux = new CustomFilterItem();
				flist.add(setAllFilterFields(aux, rs));
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
		
		return flist;
	}
	
	/**
	 * Search and returns an extended ArrayList customized for Filters containing all Filter Items that shared flag is enabled.
	 * 
	 * @return An ArrayList containing all Filter Items with shared flag enabled.
	 */
	public CustomFiltersList sharedFilters()
	{
		String selectSQL = "SELECT * FROM Filters WHERE shared = " + 1 + ";";
		CustomFilterItem aux = new CustomFilterItem();
		CustomFiltersList flist = new CustomFiltersList();
		
		try
		{
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next())
			{
				aux = new CustomFilterItem();
				flist.add(setAllFilterFields(aux, rs));
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
		
		return flist;
	}
	
	public CustomFiltersList publicFilters()
	{
		String selectSQL = "SELECT * FROM Filters WHERE user_key = '';";
		CustomFilterItem aux = new CustomFilterItem();
		CustomFiltersList flist = new CustomFiltersList();
		
		try
		{
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next())
			{
				aux = new CustomFilterItem();
				flist.add(setAllFilterFields(aux, rs));
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
		
		return flist;
	}
	
	/**
	 * Check if the Filter exists under the owner name
	 * 
	 * @param filterName The name of desired filter
	 * @param userName The name of owner of the filter
	 * @return [True] if found a result [False] if not
	 */
	public boolean existsFilterWithOwner(String filterName, String userName)
	{
		String selectSQL = "SELECT name FROM Filters WHERE name = '" + filterName + "' AND user_key = '"
						   + userName + "';";
		boolean found = false;
		
		try
		{
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			
			// if a line exists (found a result) then found receives true
			found = rs.next();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
		
		return found;
	}
	
	/**
	 * Inserts Filter on Data Base determining every parameter.
	 * 
	 * @param filterName Name of the filter
	 * @param header The header for the result found
	 * @param regex Regex filter clause
	 * @param wMain Main log "where to" checkbox
	 * @param wSyst System log "where to" checkbox
	 * @param wKrnl Kernel log "where to" checkbox
	 * @param wBugr Bugreport log "where to" checkbox
	 * @param wRout Report Output log "where to" checkbox
	 * @param shared If it should be shared to other users
	 * @param userName Owner of the filter
	 * @return [0] if insert failed (Invalid fields) [1] if insert succeeded.
	 */
	public int insertFilter(String filterName, String header, String regex, boolean wMain, boolean wSyst,
							boolean wKrnl, boolean wRadio, boolean wBugr, boolean wRout, boolean shared,
							boolean active, String userName)
	{
		
		// Visual query example for reference:
		// INSERT INTO Filters VALUES (0, 'Test_Filter', '[A-z]', 1, 1, 0, 0, 1, 1, 'testuser');
		
		String insertSQL = "INSERT INTO Filters VALUES ( null, '" + filterName + "', '" + header + "', '"
						   + regex + "', " + boolToByte(wMain) + ", " + boolToByte(wSyst) + ", "
						   + boolToByte(wKrnl) + ", " + boolToByte(wRadio) + ", " + boolToByte(wBugr) + ", "
						   + boolToByte(wRout) + ", " + boolToByte(shared) + ", " + boolToByte(active)
						   + ", '" + userName + "', null);";
		int insertDone = 0;
		
		try
		{
			preparedStatement = dbConnection.prepareStatement(insertSQL);
			
			// Execute insert SQL statement
			insertDone = preparedStatement.executeUpdate();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return insertDone;
		
	}
	
	/**
	 * Inserts Filter Item on Data Base.
	 * 
	 * @param filter Filter Item containing all fields that should be inserted.
	 * @return [0] if insert failed (Invalid fields) [1] if insert succeeded.
	 */
	public int insertFilter(CustomFilterItem filter)
	{
		// Visual query example for reference:
		// INSERT INTO Filters VALUES (null, 'Test_Filter', '- TestHeader' , '[A-z]', 1, 1, 0, 0, 1, 1, 1, 0, 'testuser', null);
		
		String insertSQL = "INSERT INTO Filters VALUES ( null, '" + filter.getName() + "', '"
						   + filter.getHeader() + "', '" + filter.getRegex() + "', "
						   + boolToByte(filter.isMain()) + ", " + boolToByte(filter.isSystem()) + ", "
						   + boolToByte(filter.isKernel()) + ", " + boolToByte(filter.isRadio()) + ", "
						   + boolToByte(filter.isBugreport()) + ", " + boolToByte(filter.isRoutput()) + ", "
						   + boolToByte(filter.isShared()) + ", '" + boolToByte(filter.isActive()) + ", '"
						   + filter.getOwner() + "', null);";
		int insertDone = 0;
		
		try
		{
			preparedStatement = dbConnection.prepareStatement(insertSQL);
			
			// Execute insert SQL statement
			insertDone = preparedStatement.executeUpdate();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return insertDone;
		
	}
	
	/**
	 * @param filtersList
	 * @return Filters inserted {@link int}
	 */
	public int insertFilters(CustomFiltersList filtersList)
	{
		// Visual query example for reference:
		// INSERT INTO Filters VALUES (null, 'Test_Filter', '- TestHeader' , '[A-z]', 1, 1, 0, 0, 1, 1, 1, 0, 'testuser', null);
		int inserted = 0;
		
		for (CustomFilterItem filter : filtersList)
		{
			inserted = inserted + insertFilter(filter);
		}
		
		return inserted;
	}
	
	/**
	 * This method will update filters only under the name of the user. If the user is not the owner he won't be able to edit it. <b>Uses
	 * SharedObjs.getUser() to determine username.</b> Also, this method attempts to update every field on Filters table whether it has
	 * changed or not.
	 * 
	 * @param oldFilterName The name of the filter that should be updated
	 * @param editedFilter New Filter item containing all fields with updated values
	 * @return [0] if update failed (User is not the owner or filter name does not exist or a field has an invalid value) [1] if update
	 *         succeeded.
	 */
	public int updateFilter(String oldFilterName, CustomFilterItem editedFilter)
	{
		// Visual query example for reference:
		// UPDATE Filters SET name = 'Test_Filter', header = '- TestHeader', regex = '[A-z]', w_main = 1, w_syst = 1, w_krnl = 0, w_radio =
		// 1, w_bugr = 0, w_rout = 1, shared = 1, active = 0, user_key = 'testuser' WHERE name = 'Test_Adapter' AND user_key = 'testuser';
		
		String updateSQL = "UPDATE Filters SET name = '" + editedFilter.getName() + "', header = '"
						   + editedFilter.getHeader() + "', regex = '" + editedFilter.getRegex()
						   + "', w_main = " + boolToByte(editedFilter.isMain()) + ", w_syst = "
						   + boolToByte(editedFilter.isSystem()) + ", w_krnl = "
						   + boolToByte(editedFilter.isKernel()) + ", w_radio = "
						   + boolToByte(editedFilter.isRadio()) + ", w_bugr = "
						   + boolToByte(editedFilter.isBugreport()) + ", w_rout = "
						   + boolToByte(editedFilter.isRoutput()) + ", shared = "
						   + boolToByte(editedFilter.isShared()) + ", active= "
						   + boolToByte(editedFilter.isActive()) + ", user_key = '" + editedFilter.getOwner()
						   + "' WHERE name = '" + oldFilterName + "' AND user_key = '" + SharedObjs.getUser()
						   + "';";
		int updateDone = 0;
		
		try
		{
			preparedStatement = dbConnection.prepareStatement(updateSQL);
			
			// Execute insert SQL statement
			updateDone = preparedStatement.executeUpdate();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return updateDone;
		
	}
	
	/**
	 * Delete filter query adapter. Only will work if the owner of the filter is the same user trying to delete it. <b>Uses
	 * SharedObjs.getUser() to determine username.</b>
	 * 
	 * @param filterName The filter name that will be deleted
	 * @return [0] if delete failed (User is not the owner or filter name does not exist) [1] if delete succeeded.
	 */
	public int deleteFilter(String filterName)
	{
		// Visual query example for reference:
		// DELETE from Filters where name = 'Test_Filter';
		String deleteSQL = "DELETE from Filters where name = '" + filterName + "' AND user_key = '"
						   + SharedObjs.getUser() + "';";
		
		int deleteDone = 0;
		
		try
		{
			preparedStatement = dbConnection.prepareStatement(deleteSQL);
			
			// Execute delete SQL statement
			deleteDone = preparedStatement.executeUpdate();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
		
		return deleteDone;
	}
	
	public int deleteAllMyFilters()
	{
		// Visual query example for reference:
		// DELETE from Filters where name = 'Test_Filter';
		String deleteSQL = "DELETE from Filters where user_key = '" + SharedObjs.getUser() + "';";
		
		int deleteDone = 0;
		
		try
		{
			preparedStatement = dbConnection.prepareStatement(deleteSQL);
			
			// Execute delete SQL statement
			deleteDone = preparedStatement.executeUpdate();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
		
		return deleteDone;
	}
	
	/**
	 * Close Data Base connection
	 * 
	 * @throws SQLException If occurs a failure during connection closure.
	 */
	public void close() throws SQLException
	{
		if (preparedStatement != null)
		{
			preparedStatement.close();
		}
		
		if (dbConnection != null)
		{
			dbConnection.close();
		}
	}
	
}
