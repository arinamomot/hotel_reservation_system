package cvut.ear.hotelreservationsystem.security;

import cvut.ear.hotelreservationsystem.environment.Generator;
import cvut.ear.hotelreservationsystem.model.User;
import cvut.ear.hotelreservationsystem.security.model.UserDetails;
import cvut.ear.hotelreservationsystem.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class DefaultAuthenticationProviderTest {

    @Autowired
    private UserService userService;

    @Autowired
    private DefaultAuthenticationProvider provider;

    private final User user = Generator.generateCustomerUser();
    private final String rawPassword = user.getPassword();

    @Before
    public void setUp() throws Exception {
        userService.create(user);
        SecurityContextHolder.setContext(new SecurityContextImpl());
    }

    @After
    public void tearDown() {
        SecurityContextHolder.setContext(new SecurityContextImpl());
    }

    @Test
    public void successfulAuthenticationSetsSecurityContext() {
        final Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), rawPassword);
        final SecurityContext context = SecurityContextHolder.getContext();
        assertNull(context.getAuthentication());
        final Authentication result = provider.authenticate(auth);
        assertNotNull(result);
        assertTrue(result.isAuthenticated());
        assertNotNull(SecurityContextHolder.getContext());
        final UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        assertEquals(user.getEmail(), details.getUsername());
        assertTrue(result.isAuthenticated());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void authenticateThrowsUserNotFoundExceptionForUnknownUsername() {
        final Authentication auth = new UsernamePasswordAuthenticationToken("unknownUsername", rawPassword);
        try {
            provider.authenticate(auth);
        } finally {
            final SecurityContext context = SecurityContextHolder.getContext();
            assertNull(context.getAuthentication());
        }
    }

    @Test(expected = BadCredentialsException.class)
    public void authenticateThrowsBadCredentialsForInvalidPassword() {
        final Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), "unknownPassword");
        try {
            provider.authenticate(auth);
        } finally {
            final SecurityContext context = SecurityContextHolder.getContext();
            assertNull(context.getAuthentication());
        }
    }

    @Test
    public void supportsUsernameAndPasswordAuthentication() {
        assertTrue(provider.supports(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void successfulLoginErasesPasswordInSecurityContextUser() {
        final Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), rawPassword);
        provider.authenticate(auth);
        assertNotNull(SecurityContextHolder.getContext());
        final UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        assertNull(details.getUser().getPassword());
    }
}
