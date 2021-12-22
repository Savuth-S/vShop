package main.java.database;

public class Helper
{
        public static void main(String[] args)
        {
                Connection connection = null;
                Srting url = "jdbc:mariadb://localhost:3306/test";
                String user = "root";
                String pwd = "root";
                
                try{
                        connection = DriverManager.getConnection(url, user, pwd);
                
                }catch(SQLException e){
                        e.printStackTrace();
                }

                        System.out.println(
                               "Succes" );
        }
}
