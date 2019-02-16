package org.awesley.infra.security;

import static org.junit.Assert.*;

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

@RunWith(SpringRunner.class)
@SpringBootTest(
	classes = { JwtTokenUtilPropertiesTest.JwtTokenUtilPropertiesTestConfiguration.class },
	properties = {
		"jwt.algorithm=RS512",
		"jwt.base64EncodedSigningKey=MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCgXDA8mDsweO8QC25bl/uHNtkk/3NskbsdcoYPPx7c28L45IULyhR8nKdrGiU93M5XOoddPg3YMmXgcUaF0zqTuYQxcvf18jHECc99/SXRNdXRGRqT0AGAnkL5jeTSFII20tXMpOKr/embJ4R/xlMG0E2kwze1wYK+iqSeLE3XvjjTNLDYJdciohTlxPliDAJqCIqsZlWgfLUuIBKOGP5Xrb2bWYoaRyXKY575JkKxBnVem2iFHlB+FwLDqiuNCN3do+eadKbsuNWtHN6EIKImNYW3s9TpKjW+KT9UFuuxHNXkdqsROwpIFC3oMfqNINUlnF/4821e26dChqHyFzKXAgMBAAECggEAehLz3Yz9sf697nblQfDG174XO6UDVyjxSo9+Imkos3TQTeyYo8roIZyXjl7O7vMCWUEC/yZYH7bt+xgAWavdMcvOKF6fCKmg3N2jCQQHH+ZdqNn1c4SrQCA6WAN+p7ZZEicZ/rmenNOkXCqPIT32wn8cPEsoQ8LWJvsgKhEMCSleY9OnRiUZncDyDrljTDWTmeq+wWrevWEM5LNeua8IvkbhIYBmcgYBugL5GMcXYXvzTo0bJtIRgn0F7YBUu3NOUC+1vTQKmvOZo15RwO9RfeiDBD/tp7+1HEtoMPyN0apX7GOBbGMsCEF01wfqiiNZpTQyEyS1Cl2H2xqqHuDCoQKBgQDbOXFljC9brQd32I7yYyBGPm9HRWWi71JB9hT3xt2jklnG3dFN6ffjTyfBxp9BfljDEbv2jHblSCWGBIRz8ZTCNjnh4VDUQaaLHq+UQPuvMyD07nVhEpGhaw9SU46aDxKhLgO/VozItoMHhewu+JN5zfrvmxJs34krbFEdiOL/GQKBgQC7QtQOumA/5rm4Zy+Xs0AYKqm7NPQ9CmPGguMSv1yXxm2SbpWmCOYkMkiqgwQLCW0mrOMsCt/CNsg4Jh7m05RSzsLAw6ZiaTxn5GBP+3xKqFFDk6sfmCwJt93rAlW7Aguib7jfG1dCEw10dYV3aqaQFC+PDiJ56y3QfU/a4CHlLwKBgQCUeE3tSUjVnNT+/MKrNCkqiaRRr5VLCSw7kg7XVG7NFhQJVDcpvnhtETHK0dsPvyMcUclsC+uGExuerTaLU6S8936yFSfp6OCDPfAJxwzttgHvAAStDqdknB5PUlm0ytqCIKjXECXe1YTeNnwjv6QZjUASwIJ1C9tZdk1lZEzgGQKBgDrqVKNpmdyg76+Hr16QA8tWqN4AVBNcUHXlOiHs2OnbgR+/8Q8y341l0Jzs6Xm7dn7jh/mUVj/ZUnbv72r6CEFhUb6qD5AIwfuBy5d+bOVkPWUAcadYDgrksUo+CtDyXMy3T7fJ91MPgX0xeFNQ16Se5TOh49Dt4BUnR9d6Cs/7AoGAIpHd12xADaBb9n/uXmbG5urCigdvraebQgiB32VYJ12epH3BNNLHZ5lt4+9rP6CZJmSbb1LLRgmy8KzLLzySNxIdqro4nWivuT2dmgIuCxejEj3rj195a5+SdhLmtDa/aDZWF20eEkwtxmeVIbgpRlrTCHJBHb4XSgBc+fk0InY=",
		"jwt.base64EncodedValidationKey=MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoFwwPJg7MHjvEAtuW5f7hzbZJP9zbJG7HXKGDz8e3NvC+OSFC8oUfJynaxolPdzOVzqHXT4N2DJl4HFGhdM6k7mEMXL39fIxxAnPff0l0TXV0Rkak9ABgJ5C+Y3k0hSCNtLVzKTiq/3pmyeEf8ZTBtBNpMM3tcGCvoqknixN17440zSw2CXXIqIU5cT5YgwCagiKrGZVoHy1LiASjhj+V629m1mKGkclymOe+SZCsQZ1XptohR5QfhcCw6orjQjd3aPnmnSm7LjVrRzehCCiJjWFt7PU6So1vik/VBbrsRzV5HarETsKSBQt6DH6jSDVJZxf+PNtXtunQoah8hcylwIDAQAB"
	}
)
public class JwtTokenUtilPropertiesTest {

	private List<? extends Authority> authorities = 
			Arrays.asList(new JwtAuthority("Role1"), new JwtAuthority("Role2"));

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Test
	public void givenInjectedJwtTokenUtil_withInjectedKeyValues_shouldCreateTokenAndValidate() {
		String token = jwtTokenUtil.generateToken("TestUser",  authorities);

		assertNotNull(token);
		assertTrue(Pattern.matches("[^.]+.[^.]+.[^.]+", token));
		assertTrue(jwtTokenUtil.validateToken(token));
	}

	@SpringBootApplication
	static class JwtTokenUtilPropertiesTestConfiguration {
		
	}
}
