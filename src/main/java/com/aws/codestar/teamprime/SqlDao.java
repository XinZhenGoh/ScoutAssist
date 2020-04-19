package com.aws.codestar.teamprime;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class SqlDao {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://scoutassistdb.cwr7stpufwbn.us-east-2.rds.amazonaws.com:3306/TeamPrime?user=TeamPrime&password=kingsallday";

    //  Database credentials
    static final String USER = "TeamPrime";
    static final String PASS = "kingsallday";

    public static String GetName(int id){
        String name = "com.mysql.jdbc.Driver";
        String sql = "SELECT name FROM PlayerInfo WHERE playerID = '"+id+"'";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while(rs.next()) {
                name = rs.getString("name");
            }

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("SQl statement executed");

        System.out.println("sql statement = "+sql);
        System.out.println(id+" name is "+name);
        return name;
    }

    public static int GetID(String name){
        int id = 0;
        String sql = "SELECT playerID FROM PlayerInfo WHERE name = '"+name+"'";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while(rs.next()) {
                id = rs.getInt("playerID");
            }

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("SQl statement executed");

        System.out.println("sql statement = "+sql);
        System.out.println(name+" id is "+id);
        return id;
    }

    //unimplemented
    public static void AddPlayer(){
    }

    //unimplemented
    public static void DeleteRecordByID(){
    }

    //unimplemented
    public static void DeleteRecordByName(){
    }

    //obsolete function, will be removed
    public static ResultSet ExecuteSql(String sql){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("SQl statement executed");
        return rs;
    }

//Method to display all player data
    public static void DisplayPlayers(){
        Connection conn = null;
        Statement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT playerID, name FROM PlayerInfo";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt("playerID");
                String name = rs.getString("name");

                //Display values
                System.out.print("\nID: " + id);
                System.out.print(", Name: " + name);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");



    }

}//end
