package onlinepaymentsimulator.domain;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import org.apache.commons.codec.digest.*;

public class Token {
    private final String token;
    private final Date expires;
    
    private final HashAlgorithm algorithm = HashAlgorithm.SHA512;

    public Token(Date expires) {
        this.expires = expires;
        this.token = createToken();
    }
    
    private String createToken() {
        Random random = new Random();
        String tokenValue = String.valueOf(random.nextInt(99999) + 11111);
        
        if (algorithm == HashAlgorithm.SHA512) {
            return new String(calculateTokenSHA512(tokenValue));
        }
        
        return null;
    }
    
    private byte[] calculateTokenSHA512(String stringToHash) {
        return DigestUtils.sha512(stringToHash);
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
    
    private enum HashAlgorithm {
        SHA512
    }
}
