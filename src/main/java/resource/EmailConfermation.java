package resource;

import java.sql.Timestamp;

public class EmailConfermation {

    private final String person;
    private final String token;
    private Timestamp expirationDate;

    public EmailConfermation(final String person, final String token, final Timestamp expirationDate) {
        this.person = person;
        this.token = token;
        this.expirationDate = expirationDate;
    }

    public String getPerson() {
        return person;
    }

    public String getToken() {
        return token;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

}
