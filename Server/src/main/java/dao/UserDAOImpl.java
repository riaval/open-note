package dao;

import org.hibernate.Query;

import domain.User;

public class UserDAOImpl extends DAOImpl<User>{
	
	public User findByLogin(String login){
		Query query = getSession().createQuery("from User where login = :login");
		query.setString("login", login);
		return findOne(query);
	}
	
}
	
//	public void createUser(User user) throws Exception {
//        try {
//            begin();
//            getSession().save(user);
//            commit();
//        } catch (HibernateException e) {
//            rollback();
//            throw new Exception("Could not create user " + user.getLogin(), e);
//        }
//    }
//
//    public User retrieveUser(String login) throws Exception {
//        try {
//            begin();
//            Query q = getSession().createQuery("from User where login = :login");
//            q.setString("login", login);
//            User user = (User) q.uniqueResult();
//            commit();
//            return user;
//        } catch (HibernateException e) {
//            rollback();
//            throw new Exception("Could not get user " + login, e);
//        }
//    }
//
//    public void deleteUserByLogin(String login) throws Exception {
//        try {
//            begin();
//            Query q = getSession().createQuery("delete from User where login in (:login) ");
//            q.setString("login", login);
//            q.executeUpdate();
//            commit();
//        } catch (HibernateException e) {
//            rollback();
//            throw new Exception("Could not delete user " + login, e);
//        }
//    }
//}
