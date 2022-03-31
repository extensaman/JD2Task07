package by.academy.it.task07;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AppTest
{
    @Test
    public void shouldAnswerWithTrue() throws SQLException {
        ResourceBundle bundle = ResourceBundle.getBundle("database");
        String url = bundle.getString("urlForTest");
        String user = bundle.getString("userForTest");
        String password = bundle.getString("passwordForTest");
        Connection connection = null;

        connection = DriverManager.getConnection(url, user, password);

        assertTrue( connection != null );
    }
}
