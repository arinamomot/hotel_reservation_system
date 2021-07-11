package cvut.ear.hotelreservationsystem.dao;

import cvut.ear.hotelreservationsystem.model.Hotel;
import cvut.ear.hotelreservationsystem.model.Room;
import cvut.ear.hotelreservationsystem.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Objects;

@Repository
public class UserDao extends BaseDao<User> {

    public UserDao() {
        super(User.class);
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("Select u From User u Where not u.disabled", User.class).getResultList();
    }

    @Override
    public void remove(User entity) {
        Objects.requireNonNull(entity);
        try {
            entity.setDisabled(true);
            em.merge(entity);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    public User findByEmail(String email) {
        try {
            return em.createNamedQuery("User.findByEmail", User.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
