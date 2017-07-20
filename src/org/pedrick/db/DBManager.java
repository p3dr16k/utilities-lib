package org.pedrick.db;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.json.JSONArray;
import org.json.JSONObject;

/*=======================================================================

*FILE:         DBManager.java
*
*DESCRIPTION:  A simple and lightweight Manager for DB connections
*REQUIREMENTS: 
*AUTHOR:       Patrick Facco
*VERSION:      1.0
*CREATED:      20-lug-2017
*LICENSE:      GNU/GPLv3
*COPYRIGTH:    Patrick Facco 2017
*This program is free software: you can redistribute it and/or modify
*it under the terms of the GNU General Public License as published by
*the Free Software Foundation, either version 3 of the License, or
*(at your option) any later version.
*
*This program is distributed in the hope that it will be useful,
*but WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*GNU General Public License for more details.
*
*You should have received a copy of the GNU General Public License
*along with this program.  If not, see <http://www.gnu.org/licenses/>.
*========================================================================
* 
*/

public class DBManager

{        
    private static final String ENV = "java:comp/env";
    private static final String DBNAME = "jdbc/test";
    public  static int NUMCONN = 1;
    private Context context;
    private DataSource datasource;
    private Connection connection;
    private Statement statement;
    private ResultSet resultset;
    private boolean disableDebug;
    
    //Singleton instance of DBManager Object
    private static final DBManager dbmanager = new DBManager();    
    
    public static int ROW_DELETED = 1001;
    public static int ROW_UPDATED = 1002;
    public static int ROW_NO_CHANGES = 1003;
    public static int ROW_INSERTED = 1005;
    
    /**
     * Private constructor for singleton pattern
     */
    private DBManager() 
    {  
        try
        {
            disableDebug = false;
            context = new InitialContext();             
            datasource =(DataSource)context.lookup(ENV + "/" + DBNAME);
            
            //This is necessary for Glassfish JNDI lookup compatibility
            if(datasource == null)
            {
                datasource = (DataSource)context.lookup(DBNAME);            
            }
            connection = datasource.getConnection();     
            
            System.out.println("CONNECTION OPEN IN COSTRUCTOR, TOTAL CONNECTION = " + NUMCONN);
                    
        }
        catch(NamingException ex)
        {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(SQLException ex)
        {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
    /**
     * Return dbmanager instance
     * @return the instance of DBManager used for database connection
     */
    public static DBManager getInstance()
    {
        return dbmanager;
    }   

    /**
     * Getter for the connection. Useful if you'd like to use a PreparedStatement
     * instead of a simple Statement for the insert/update queries.
     * @return the connection object
     */
    public Connection getConnection()
    {
        return connection;
    }

    /**
     * Getter for current statement. Useful if you need a savepoint or if 
     * you have to analize your queries.
     * @return the current statements
     */
    public Statement getStatement()
    {
        return statement;
    }

    /**
     * Getter for the DataSource object.
     * @return the current DataSource object
     */
    public DataSource getDatasource()
    {
        return datasource;
    }
    
    /**
     * Connect this dbmanager to the database
     * @throws SQLException if a database access error occurs
     */
    private void connectDB() throws SQLException
    {       
        if(connection == null || connection.isClosed())
        {            
            connection = datasource.getConnection();
            //NUMCONN++;
            incrementConn();
            System.out.println("CONNECTION OPEN, TOTAL CONNECTION = " + NUMCONN);
        }    
        System.out.println("CONNECTION REUSE, TOTAL CONNECTION = " + NUMCONN);
    }
    
    public void connectDB(String caller) throws SQLException
    {
        connectDB();
        if(caller != null && !disableDebug)
        {
            System.out.println("connection open by: " + caller);
        }        
    }
    
    public void disconnectDB(String caller)
    {
        disconnectDB();
        if(caller != null && !disableDebug)
        {
            System.out.println("connection closed by: " + caller);
        }
    }
    
    /**
     * Disconnect this dbmanager to the database
     
     */
    private void disconnectDB() 
    {
        try
        {
            if(connection != null && !connection.isClosed())            
            {
                if(statement != null)
                {
                    statement.close();
                }
                if(resultset != null)
                {
                    resultset.close();
                }
                connection.close();
                //NUMCONN--;
                decrementConn();
                System.out.println("CONNECTION CLOSED, TOTAL CONNECTION = " + NUMCONN);
            }        
        }
        catch(SQLException ex)
        {
            System.out.println("CONNECTION CLOSE ERROR, TOTAL CONNECTION = " + NUMCONN);
        }
    }
    
    /**
     * Execute a query on database
     * @param sql a string that rapresent the query to launch
     * @return the ResultSet that wrap the query results
     * @throws SQLException if the query is malformed or a database access error occurs
     */
    public ResultSet executeQueryDB(String sql)
            throws SQLException
    {        
        if(connection == null || connection.isClosed())
        {            
            connection = datasource.getConnection();
        }
        statement = connection.createStatement();
        resultset = statement.executeQuery(sql);
        return resultset;
    }
    
    /**
     * Executes the given SQL statement, which may be an INSERT, UPDATE, or DELETE 
     * statement or an SQL statement that returns nothing, such as an SQL DDL statement.
     * @param sql the query to execute
     * @return either (1) the row count for SQL Data Manipulation Language (DML) 
     * statements or (2) 0 for SQL statements that return nothing
     * @throws SQLException if a database access error occurs, this method is called on a closed Statement, 
     * the given SQL statement produces a ResultSet object, the method is called on a PreparedStatement or CallableStatement     
     */
    public int executeUpdateDB(String sql)
            throws SQLException
    {
        statement = connection.createStatement();
        int result;
        result = statement.executeUpdate(sql);
        return result;
    }
    
    /**
     * Translate a ResultSet object in a JSONArray
     * @param rs The resultset to translate
     * @return a JSONArray with the resultset label as a keys and the resultset values as a values
     * @throws SQLException if an error on the resultset occurs
     */
    public JSONArray jsonize(ResultSet rs) throws SQLException
    {
        resultset = rs;
        
        ResultSetMetaData md = resultset.getMetaData();
        
        int nCol = md.getColumnCount();            
        JSONObject myString = new JSONObject();
        JSONArray queryRis = new JSONArray();
        
        while(resultset.next())
        {
            for(int i = 1; i < nCol+1; i++)
            {             
                myString.put(md.getColumnName(i), resultset.getString(md.getColumnName(i)));            
            }
            queryRis.put(myString);
            myString = new JSONObject();
        }       
        return queryRis;
    }
    
    /**
     * Translate a JSONObject into a query string and insert the new record into the database
     * @param json the JSONObject that rapresents the new record to insert
     * @param table the name of the table where the new record is inserted
     * @return 0 if everything goes right
     * @throws SQLException if and error occurs during the database insertion
     */
    public int dbize(JSONObject json, String table)
            throws SQLException
    {  
        ResultSetMetaData md = metadata(table);
        String columns = getColumnNameForDbize(json);
        String values = getFieldsForDBize(json, md);
        String query = "INSERT INTO " + table + 
                " (" + columns + ") VALUES(" + values + ")";
        
        int result = executeUpdateDB(query);
        return result;
    }
        
    private static int getColumnType(String columnName, ResultSetMetaData md) 
            throws SQLException
    {
        for(int i = 1; i <= md.getColumnCount(); i++)
        {
            if(columnName.equals(md.getColumnName(i)))
            {
                return md.getColumnType(i);
            }
        }
        return -1;
    }
    
    /**
     * Return all fields separate by ','
     * @param o the json object to parse
     * @return all fields separate by ','
     */
    private String getColumnNameForDbize(JSONObject o)
    {
        String res = "";
        String[] fieldsAsArray = JSONObject.getNames(o);
        for(int i = 0; i < fieldsAsArray.length; i++)
        {            
            if(i == fieldsAsArray.length - 1)
            {
                res += fieldsAsArray[i];                
            }
            else
            {
                res += fieldsAsArray[i] + ',';
            }
        }
        return res;
    }
    
    /**
     * Returns all values of json object correctly escaped for insertion into db
     * @param o json object to parse
     * @return all values of json object correctly escaped for insertion into db
     */
    private String getFieldsForDBize(JSONObject o, ResultSetMetaData md) 
            throws SQLException
    {
        String res = "";
        Iterator<String> it = o.keys(); 
        int columnType;
        int size = o.length();
        int i = 0;
        String tmpKey;
        while(it.hasNext())
        {
            tmpKey = it.next();
            columnType = getColumnType(tmpKey, md);
            if(columnType == Types.CHAR
                    || columnType==Types.VARCHAR 
                    || columnType==Types.LONGVARCHAR 
                    || columnType==Types.DATE
                    || columnType==Types.NCHAR
                    || columnType==Types.LONGNVARCHAR
                    || columnType==Types.TIME
                    || (columnType==Types.DATE && columnType==Types.TIME)
                    || columnType==Types.TIMESTAMP)   
            {
                if(i == size - 1)
                {
                    res += "'" + o.getString(tmpKey) + "'";
                }
                else
                {
                    res += "'" + o.getString(tmpKey) + "',";
                }
            }
            else
            {
                if(i == size - 1)
                {
                    res += o.getString(tmpKey);
                }
                else
                {
                    res += o.getString(tmpKey) + ",";
                }
            }
            i++;
        }
        return res;
    }
    
    /**
     * Translate a JSONObject into a query string and update a record on database
     * @param json the JSONObject that rapresents the new record to insert
     * @param table the name of the table where the new record is inserted
     * @param idRow the id of the row 
     * @return the number of rows affected by the update
     * @throws SQLException if something go wrong on update records
     */
     public int dbUpdate(JSONObject json, String table, int idRow)
            throws SQLException 
     {
        Iterator<String> it = json.keys();
        String[] campi;
        campi = JSONObject.getNames(json);
        String[] valori = new String[json.length()];
        int index = 0;
        String primaryKey = "";
        ResultSet rs = connection.getMetaData().getPrimaryKeys(null, null, table);
        while(rs.next()){
            primaryKey = rs.getString("COLUMN_NAME");
            
        }
       
        while (it.hasNext())
        {
            valori[index] = json.getString(it.next());
            index++;
        }

        DatabaseMetaData dbmd = connection.getMetaData();
        String driver_name = dbmd.getDriverName();
        String query_base;

        if (driver_name.toLowerCase().contains("Apache Derby Network Client JDBC Driver".toLowerCase()))
        {
            query_base = "SELECT * FROM " + table + " "
                    + "FETCH FIRST 1 ROWS ONLY";
        }
        else
        {
            query_base = "SELECT * FROM " + table + " "
                    + "LIMIT 1";
        }

        resultset = executeQueryDB(query_base);
        ResultSetMetaData md = resultset.getMetaData();
        Map map = new HashMap();

        //calculate first column ok
        int j = md.getColumnCount() - json.length() + 1;

        for (int i = 0; i < valori.length; i++) 
        {
	    int columnType = DBManager.getColumnType(campi[i], md);
            if(columnType == Types.CHAR
                    || columnType==Types.VARCHAR 
                    || columnType==Types.LONGVARCHAR 
                    || columnType==Types.DATE
                    || columnType==Types.NCHAR
                    || columnType==Types.LONGNVARCHAR
                    || columnType==Types.TIME
                    || (columnType==Types.DATE && columnType==Types.TIME)
                    || columnType==Types.TIMESTAMP)
			{

                valori[i] = "'" + valori[i] + "'";
            }

        }

        if (campi.length == valori.length) 
        {

            for (int i = 0; i < campi.length; i++)
            {

                map.put(campi[i], valori[i]);

            }

            Iterator iterator = map.keySet().iterator();
            String s = "";
            while (iterator.hasNext()) {

                Object key = iterator.next();
                Object value = map.get(key);
                s += key.toString() + " = " + value.toString() + ",";

            }
            s = s.substring(0, s.length() - 1); //tolgo ultima virgola
            
            String query = "UPDATE " + table + " SET "
                    + s + " WHERE "+ primaryKey +" = " + idRow + "";

            int result = executeUpdateDB(query);

            return result;
        }
        else
        {
            return -1;
        }
    } 
    
    /**
     * Gets the metadata of the resultset useful for grab table structure
     * @param table the table to examine
     * @return a ResultSetMetaData for grab the table structure
     * @throws NamingException if there are problems with the context 
     * @throws SQLException if an error on sql syntax occurs
     */
    private ResultSetMetaData metadata(String table)
            throws SQLException
    {
        DatabaseMetaData dbmd = connection.getMetaData();
        String driver_name = dbmd.getDriverName();
        String query_base;
        if(driver_name.toLowerCase().contains("Apache Derby Network Client JDBC Driver".toLowerCase()))
        {
            query_base = "SELECT * "
                    + "FROM " + table + " "
                    + "FETCH FIRST 1 ROWS ONLY";
        }
        else
        {
            query_base = "SELECT * "
                    + "FROM " + table + " "
                    + "LIMIT 1";
        }
        resultset = executeQueryDB(query_base);
        ResultSetMetaData md = resultset.getMetaData();
        return md;
    }  
    
    private synchronized void incrementConn()
    {
        NUMCONN++;
    }
    
    private synchronized void decrementConn()
    {
        NUMCONN--;
    }

    public boolean isDisableDebug()
    {
        return disableDebug;
    }

    public void setDisableDebug(boolean disableDebug)
    {
        this.disableDebug = disableDebug;
    }    
}
