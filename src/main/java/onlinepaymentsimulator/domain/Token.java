package onlinepaymentsimulator.domain;

import java.util.Date;
import java.util.UUID;

public class Token {
    private final String token;
    private final Date expires;

    public Token(Date expires) {
        this.expires = expires;
        this.token = createToken();
    }
    
    private String createToken() {
        return UUID.randomUUID().toString();
    }

    public String getToken() {
        return token;
    }

    public Date getExpires() {
        return expires;
    }
    
    public boolean isValid() {
        return new Date().before(expires);
    }
    
    public long timeLeft(Date date) {
        long time = expires.getTime() - date.getTime();
        
        if(time < 0) {
            time = 0;
        }
        
        return time;
    }
}
