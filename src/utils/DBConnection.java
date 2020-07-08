package utils;

import java.lang.reflect.Field;
import java.security.interfaces.RSAKey;
import java.sql.*;
import java.util.ArrayList;

public class DBConnection<T> {
    private String urlConnection = "jdbc:sqlserver://localhost:1433;databaseName=QLBT;user=sa;password=123456";
    /// lay so dong
    public int getRowCount(String table) {
        try (Connection con = DriverManager.getConnection(urlConnection)) {
            int row = 0;
            String query = "Select count(*) from " + table;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                row = rs.getInt(1);
            }
            return row;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public String getProductInformation(){
        try (Connection con = DriverManager.getConnection(urlConnection))
        {
            String query = "Select productname,productcontent from product";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String strReturn = "";
            while (rs.next())
            {
                strReturn +="<html> Name: "+rs.getString("productname")+"<br>"+" Content :"+rs.getString("productcontent")+"</html>";
                strReturn += ",";
            }
            strReturn = strReturn.substring(0,strReturn.length()-1);
            return strReturn;

        }catch (SQLException e)
        {
            e.printStackTrace();
        }return "";
    }
    public String getStoreAddress()
    {
        try(Connection con = DriverManager.getConnection(urlConnection))
        {
            String query = "Select storename,storeaddress from store";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String strReturn = "";
            while(rs.next())
            {
                strReturn +="<html> Store: "+rs.getString("storename") +"<br>"+"Address: "+rs.getString("storeaddress")+"</html>";
                strReturn += ",";
            }
            strReturn = strReturn.substring(0,strReturn.length()-1);
            return strReturn;

        }catch (SQLException e)
        {
            e.printStackTrace();
        }return "";
    }
    public String getComboboxString (String query)
    {
        try(Connection con = DriverManager.getConnection(urlConnection))
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String strReturn = "";
            while (rs.next()) {
                strReturn += rs.getString(1) + ",";
            }
            strReturn = strReturn.substring(0, strReturn.length() - 1); //loai bo dau phay cuoi
            return strReturn;

        }catch (SQLException e)
        {
            e.printStackTrace();
        }return "";
    }
    public String getName(String query)
    {
        try(Connection con = DriverManager.getConnection(urlConnection))
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next())
            {
                return rs.getString(1);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }return "";
    }
    public int getID(String query)
    {
        try (Connection con = DriverManager.getConnection(urlConnection))
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next())
            {
                return rs.getInt(1);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }return 0;
    }
    public String getUnit(){
        try (Connection con = DriverManager.getConnection(urlConnection))
        {
            String query = "Select value,unitname,unitconvertvalue,unitconvertname from unit";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String strReturn = "";
            while(rs.next())
            {
                strReturn += rs.getInt("value")+ " "+rs.getString("unitname")+" = "+rs.getInt("unitconvertvalue")+" "+rs.getString("unitconvertname")+",";
            }
            strReturn = strReturn.substring(0,strReturn.length());
            return strReturn;
        }catch (SQLException e)
        {
            e.printStackTrace();
        }return "";
    }
    public Timestamp getDate(String query)
    {
        try(Connection con = DriverManager.getConnection(urlConnection))
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Timestamp timeReturn = null;
            while(rs.next())
            {
                timeReturn = rs.getTimestamp(1);
            }
            return timeReturn;
        }catch (SQLException e)
        {
            e.printStackTrace();
        }return null;
    }
    public boolean check(String query){
        try (Connection con = DriverManager.getConnection(urlConnection)){ ;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
            {
                return true;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updatePassword(String username, String password)
    { try(Connection con = DriverManager.getConnection(urlConnection))
    {
        String query = "update employee set password = '"+password+"' where username ='"+username+"'";
        Statement stmt = con.createStatement();
        int check = stmt.executeUpdate(query);
        if ( check!= 0)
        {
            return  true;
        }
        else
        {
            return false;
        }
    }catch (SQLException e )
    {
        e.printStackTrace();
    }return false;
    }
    public boolean Create(T item) {
        try (Connection con = DriverManager.getConnection(urlConnection)) {
            Class<?> classInfo = item.getClass();
            String className = classInfo.getName();
            int lastIndxDot = className.lastIndexOf(".");
            String tableName = className.substring(lastIndxDot + 1);
            String query = "INSERT INTO " + tableName + "(";
            Field[] fields = classInfo.getFields();
            for (Field columnName : fields
            ) {
                query += columnName.getName() + ",";
            }
            query = query.substring(0, query.length() - 1);
            query += ") VALUES(";
            for (Field fieldItem : fields
            ) {
                if (fieldItem.getType().equals(String.class)) {
                    query += "N'" + fieldItem.get(item) + "',";

                } else if (fieldItem.getType().equals(Timestamp.class)) {
                    query += "'" + fieldItem.get(item) + "',";
                } else if (fieldItem.getType().equals(int.class)||fieldItem.getType().equals(float.class)) {
                    query += fieldItem.get(item) + ",";
                }

            }
            query = query.substring(0, query.length() - 1);
            query += ")";

            PreparedStatement pstmt = con.prepareStatement(query);
            int check = pstmt.executeUpdate();
            if (check == 1) {
                return true;
            }


        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
    // lay du lieu
    public ArrayList<Object> getAllData(String query) {
        try (Connection con = DriverManager.getConnection(urlConnection)) {
            ArrayList<Object> arr = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    arr.add(rs.getObject(i));
                }
            }
            return arr;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
