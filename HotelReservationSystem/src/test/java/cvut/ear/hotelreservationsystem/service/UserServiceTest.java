//package cvut.ear.hotelreservationsystem.service;
//
//import cvut.ear.hotelreservationsystem.dao.UserDao;
//import cvut.ear.hotelreservationsystem.environment.Generator;
//import cvut.ear.hotelreservationsystem.model.User;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.verify;
//
///**
// * This test does not use Spring.
// * <p>
// * It showcases how services can be unit tested without being dependent on the application framework or database.
// */
//public class UserServiceTest {
//
//    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    @Mock
//    private UserDao userDaoMock;
//
//    private UserService sut;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        this.sut = new UserService(userDaoMock, passwordEncoder);
//    }
//
//    @Test
//    public void persistEncodesUserPassword() {
//        final User user = Generator.generateUser();
//        final String rawPassword = user.getPassword();
//        sut.createNewUser(user);
//
//        final ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
//        verify(userDaoMock).persist(captor.capture());
//        assertTrue(passwordEncoder.matches(rawPassword, captor.getValue().getPassword()));
//    }
//}
