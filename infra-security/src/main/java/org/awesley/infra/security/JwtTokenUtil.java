package org.awesley.infra.security;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.ForbiddenException;

import org.awesley.infra.security.model.Authority;
import org.awesley.infra.security.model.JwtAuthority;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.lang.Strings;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "aud";
    static final String CLAIM_KEY_CREATED = "iat";
    static final String CLAIM_KEY_ROLES = "rol";

    //static final String AUDIENCE_UNKNOWN = "unknown";
    static final String AUDIENCE_WEB = "web";
    //static final String AUDIENCE_MOBILE = "mobile";
    //static final String AUDIENCE_TABLET = "tablet";

    private SignatureAlgorithm algorithm;
    //private String secret;
	private String base64EncodedSigningKey;
	private String base64EncodedValidationKey;
	private PrivateKey signingKey = null;
	private PublicKey validationKey = null;
    private Long expiration;

    public JwtTokenUtil(
    		@Value("${jwt.secret:secret}") String secret, 
    		@Value("${jwt.expiration:#{60*60*24*7}}") Long expiration,
    		@Value("${jwt.algorithm:}") String algorithm,
    		@Value("${jwt.base64EncodedSigningKey:}") String base64EncodedSigningKey,
    		@Value("${jwt.base64EncodedValidationKey:}") String base64EncodedValidationKey) {
		super();
		//this.secret = secret;
		this.expiration = expiration;
		algorithm = (algorithm != null && !"".equals(algorithm))? algorithm : "HS512";
		this.algorithm = SignatureAlgorithm.forName(algorithm);
		this.base64EncodedSigningKey = (base64EncodedSigningKey != null && !"".equals(base64EncodedSigningKey))? base64EncodedSigningKey: secret;
		this.base64EncodedValidationKey = (base64EncodedValidationKey != null && !"".equals(base64EncodedValidationKey))? base64EncodedValidationKey: secret;
		if (!this.algorithm.isHmac()) {
		    KeyFactory keyFactory;
			try {
				keyFactory = KeyFactory.getInstance(this.algorithm.getFamilyName());
				if (this.base64EncodedSigningKey != null && !"".equals(this.base64EncodedSigningKey)) {
				    EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(TextCodec.BASE64.decode(this.base64EncodedSigningKey));
					this.signingKey = keyFactory.generatePrivate(privateKeySpec);
				}
				if (this.base64EncodedValidationKey != null && !"".equals(this.base64EncodedValidationKey)) {
				    EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(TextCodec.BASE64.decode(this.base64EncodedValidationKey));
				    this.validationKey = keyFactory.generatePublic(publicKeySpec);
				}
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				throw new RuntimeException("Failed to recreate signingKey or validationKey from base64Encoded", e);
			}
		}
	}

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getAudienceFromToken(String token) {
        return getClaimFromToken(token, Claims::getAudience);
    }
    
    public List<Authority> getAuthoritiesFromToken(String token){
    	Claims claims = getAllClaimsFromToken(token);
    	List<Authority> authoritiesList = 
    			Arrays.stream(Strings.delimitedListToStringArray(claims.get(CLAIM_KEY_ROLES, String.class), ";"))
    			.map(r -> {
    				Authority a = new JwtAuthority(r);
    				return a;
    			}).collect(Collectors.toList());
    	return authoritiesList;
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
    	JwtParser parser = Jwts.parser();
    	if (this.validationKey != null) {
    		parser.setSigningKey(this.validationKey);
    	} else {
    		parser.setSigningKey(base64EncodedValidationKey);
    	}
        return parser.parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(Date.from(Instant.now()));
    }

    public String generateToken(String username, List<? extends Authority> authorities) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username, AUDIENCE_WEB, authorities);
    }

    private String doGenerateToken(Map<String, Object> claims, String username, String audience, List<? extends Authority> authorities) {
        final Date createdDate = Date.from(Instant.now());
        final Date expirationDate = calculateExpirationDate(createdDate);
        String allAuthorities = String.join(":", authorities.stream().map(a -> a.getName()).collect(Collectors.toList()));

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setAudience(audience)
                .claim(CLAIM_KEY_ROLES, allAuthorities)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate);
        
        if (this.signingKey != null) {
        	builder.signWith(this.algorithm, this.signingKey);
        } else {
            builder.signWith(this.algorithm, base64EncodedSigningKey);
        }
        return builder.compact();
    }

	public String refreshToken(String token) {
        final Date createdDate = Date.from(Instant.now());
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        JwtBuilder builder = Jwts.builder().setClaims(claims);
        if (this.signingKey != null) {
        	builder.signWith(this.algorithm, this.signingKey);
        } else {
        	builder.signWith(this.algorithm, base64EncodedSigningKey);
        }
        
        return builder.compact();
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }

	public String getPublicKeyEncodedString() {
		if (algorithm.isHmac() || 
				base64EncodedValidationKey == null || 
				base64EncodedValidationKey.isEmpty() ||
				base64EncodedValidationKey.equals(base64EncodedSigningKey)) {
			throw new ForbiddenException();
		}
		return base64EncodedValidationKey;
	}
}
