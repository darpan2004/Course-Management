package project.courseManagementSystem.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.courseManagementSystem.business.abstracts.UserService;
import project.courseManagementSystem.dataAccess.abstracts.UserDao;
import project.courseManagementSystem.entities.concretes.User;

@Service
public class UserManager implements UserService{

	//you need to access to dataAccess layer. So:
	private UserDao userDao; 
	
	//UserDao is a interface
	@Autowired
	public UserManager(UserDao userDao) {
		super();
		this.userDao = userDao;
	}
	@Override
	public List<User> getAll() {
		return userDao.findAll();
	}

}
