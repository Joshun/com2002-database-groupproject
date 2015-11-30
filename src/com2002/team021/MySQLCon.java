package com2002.team021;

import java.sql.*;
import com2002.team021.config.*;

class MysqlCon{
    public static void main(String args[]){
        try{
            Class.forName("com.mysql.jdbc.Driver");

            Connection con=DriverManager.getConnection(config.SQL.DB_CONNECTION_STRING);
//here sonoo is the database name, root is the username and root is the password
            Statement stmt=con.createStatement();

            ResultSet rs=stmt.executeQuery("select * from emp");

            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));

            con.close();

        }catch(Exception e){ System.out.println(e);}

    }
}
