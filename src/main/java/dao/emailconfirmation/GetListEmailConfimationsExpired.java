package dao.emailconfirmation;

import constants.Constants;
import resource.EmailConfirmation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GetListEmailConfimationsExpired
{

    private static final String STATEMENT = "select * from emailconfirmation where expirationdate <= ?";
    private final Connection connection;
    private Timestamp date;

    public GetListEmailConfimationsExpired(final Connection connection, Timestamp date)
    {
        this.connection = connection;
        this.date = date;
    }

    public List<EmailConfirmation> execute() throws SQLException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<EmailConfirmation> result = null;
        try {
            ps = connection.prepareStatement(STATEMENT);
            ps.setTimestamp(1,date);
            rs = ps.executeQuery();

            result = new ArrayList<>();
            while (rs.next())
                result.add(new EmailConfirmation(rs.getString(Constants.EMAILCONFIRMATION_PERSON),rs.getString(Constants.EMAILCONFIRMATION_TOKEN),rs.getTimestamp(Constants.EMAILCONFIRMATION_EXPIRATIONDATE)));

        } finally
        {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
            connection.close();
        }
        return result;
    }

}
