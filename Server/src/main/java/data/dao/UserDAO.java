package data.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import data.entity.User;

public class UserDAO extends DAO{
	public User createUser(String login, String email, String password, String firstName, String secondName)
			throws Exception {
        try {
            begin();
            User user = new User(login, email, password, firstName, secondName);
            getSession().save(user);
            commit();
            return user;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Could not create user " + login, e);
        }
    }

    public User retrieveUser(String login) throws Exception {
        try {
            begin();
            Query q = getSession().createQuery("from User where login = :login");
            q.setString("login", login);
            User user = (User) q.uniqueResult();
            commit();
            return user;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Could not get user " + login, e);
        }
    }

    public void deleteUser( User user ) throws Exception {
        try {
            begin();
            getSession().delete(user);
            commit();
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Could not delete user " + user.getLogin(), e);
        }
    }
}
