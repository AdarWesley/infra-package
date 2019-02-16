package org.awesley.infra.security;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.awesley.infra.security.model.Authority;
import org.awesley.infra.security.model.JwtAuthority;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.jsonwebtoken.impl.TextCodec;

@RunWith(SpringRunner.class)
@SpringBootTest(
	classes = { JwtTokenUtilTest.JwtTokenUtilTestConfiguration.class },
	properties = {
		"jwt.secret=injectedSecretValue"
	}
)
public class JwtTokenUtilTest {

	private List<? extends Authority> authorities = 
			Arrays.asList(new JwtAuthority("Role1"), new JwtAuthority("Role2"));

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Test
	public void givenSymetricKey_shouldCreateValidTokeAndValidate() {
		JwtTokenUtil jtu = new JwtTokenUtil("secret", 60L*60*24*7, null, null, null);
		String token = jtu.generateToken("TestUser", authorities);
		
		assertNotNull(token);
		assertTrue(Pattern.matches("[^.]+.[^.]+.[^.]+", token));
		
		assertTrue(jtu.validateToken(token));
	}

	@Test
	public void givenInjectedJwtTokenUtil_withSymetricKey_shouldCreateValidTokenAndValidate() {
		String token = jwtTokenUtil.generateToken("TestUser",  authorities);

		assertNotNull(token);
		assertTrue(Pattern.matches("[^.]+.[^.]+.[^.]+", token));
		assertTrue(jwtTokenUtil.validateToken(token));

		JwtTokenUtil jtu = new JwtTokenUtil("injectedSecretValue", 60L*60*24*7, null, null, null);
		assertTrue(jtu.validateToken(token));
	}
	
	@Test
	public void testAsymmetric() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		KeyPair kp = generator.genKeyPair();
		String base64encodedSigningKey = TextCodec.BASE64.encode(kp.getPrivate().getEncoded());
		String base64encodedValidationKey = TextCodec.BASE64.encode(kp.getPublic().getEncoded());
		System.out.println("SigningKey: " + base64encodedSigningKey);
		System.out.println("ValidationKey: " + base64encodedValidationKey);
		
		JwtTokenUtil jtu = new JwtTokenUtil(null, 60L*60*24*7, "RS512", base64encodedSigningKey, base64encodedValidationKey);
		String token = jtu.generateToken("TestUser", authorities);

		assertNotNull(token);
		assertTrue(Pattern.matches("[^.]+.[^.]+.[^.]+", token));
		assertTrue(jtu.validateToken(token));
	}
	
	@SpringBootApplication
	static class JwtTokenUtilTestConfiguration {
		
	}
}
