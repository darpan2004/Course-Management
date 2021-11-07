package project.courseManagementSystem.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.courseManagementSystem.business.abstracts.InstructorService;
import project.courseManagementSystem.core.utilities.results.DataResult;
import project.courseManagementSystem.core.utilities.results.Result;
import project.courseManagementSystem.core.utilities.results.SuccessDataResult;
import project.courseManagementSystem.core.utilities.results.SuccessResult;
import project.courseManagementSystem.dataAccess.abstracts.InstructorDao;
import project.courseManagementSystem.entities.concretes.Instructor;

@Service
public class InstructorManager implements InstructorService{

	private InstructorDao instructorDao;
	
	@Autowired
	public InstructorManager(InstructorDao instructorDao) {
		super();
		this.instructorDao = instructorDao;
	}

	@Override
	public Result add(Instructor entity) {
		instructorDao.save(entity);
		return new SuccessResult("Instructor added!");
	}

	@Override
	public Result delete(int id) {
		instructorDao.deleteById(id);
		return new SuccessResult("Instructor deleted!");
	}

	@Override
	public Result update(Instructor entity) {
		// refactor this:
		Instructor updatedUser = getById(entity.getId()).getData();
		updatedUser.setFirstName(entity.getFirstName());
		updatedUser.setLastName(entity.getLastName());
		updatedUser.setEmail(entity.getEmail());
		updatedUser.setDepartmentName(entity.getDepartmentName());
		updatedUser.setPhoneNumber(entity.getPhoneNumber());
		updatedUser.setNationalityId(entity.getNationalityId());
		//instructorDao.delete(entity);
		// instructorDao.save(updatedUser);
		return new SuccessResult("Instructor updated!");
	}

	@Override
	public DataResult<Instructor> getById(int id) {
		;
		return new SuccessDataResult<Instructor>(instructorDao.getById(id), "Instructor viewed!");
	}

	@Override
	public DataResult<List<Instructor>> getAll() {
		return new SuccessDataResult<List<Instructor>>(instructorDao.findAll(), "Instructors listed");
	}

}
