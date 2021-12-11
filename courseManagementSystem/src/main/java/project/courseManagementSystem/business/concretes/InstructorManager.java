package project.courseManagementSystem.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import project.courseManagementSystem.business.abstracts.InstructorService;
import project.courseManagementSystem.business.abstracts.RoleService;
import project.courseManagementSystem.business.abstracts.UserService;
import project.courseManagementSystem.business.validationRules.InstructorValidatorService;
import project.courseManagementSystem.core.email.EmailCheckService;
import project.courseManagementSystem.core.entities.Role;
import project.courseManagementSystem.core.utilities.results.DataResult;
import project.courseManagementSystem.core.utilities.results.ErrorDataResult;
import project.courseManagementSystem.core.utilities.results.ErrorResult;
import project.courseManagementSystem.core.utilities.results.Result;
import project.courseManagementSystem.core.utilities.results.SuccessDataResult;
import project.courseManagementSystem.core.utilities.results.SuccessResult;
import project.courseManagementSystem.dataAccess.abstracts.InstructorDao;
import project.courseManagementSystem.entities.concretes.Instructor;
import project.courseManagementSystem.entities.dtos.CoursesWithInstructorDto;

@Service
public class InstructorManager implements InstructorService{

	private InstructorDao instructorDao;
	private InstructorValidatorService instructorValidatorService;
	private EmailCheckService emailCheckService;
	private UserService userService;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;
	
	@Autowired
	public InstructorManager(InstructorDao instructorDao, InstructorValidatorService instructorValidatorService,
			EmailCheckService emailCheckService, UserService userService, PasswordEncoder passwordEncoder, RoleService roleService) {
		super();
		this.instructorDao = instructorDao;
		this.instructorValidatorService = instructorValidatorService;
		this.emailCheckService = emailCheckService;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.roleService = roleService;
	}

	@Override
	public Result register(Instructor instructor) {
		if(!instructorValidatorService.checkIfInstructorInfoIsFull(instructor)) {
			return new ErrorResult("enter all your information completely");
		}
		
		if(!emailCheckService.emailCheck(instructor.getEmail())) {
			return new ErrorResult("invalid email");
		}
		
		if(userService.existsByEmail(instructor.getEmail())) {
			return new ErrorResult("instructor is already exist, taken");
		}
		
        if(userService.existByUsername(instructor.getUsername())){
            return new ErrorResult("Username is already taken!");
        }
        
        // create user object
       // User savedUser = new User();
       // savedUser.setLastName(user.getLastName());
       // savedUser.setUsername(user.getUsername());
       // savedUser.setEmail(user.getEmail());
        instructor.setPassword(passwordEncoder.encode(instructor.getPassword()));

        List<Role> roles =roleService.findByName("ROLE_ADMIN").getData();
       // user.setRoles(Collections.singleton(roles));
        instructor.setRoles(roles);
        
		instructorDao.save(instructor);
		return new SuccessResult("Successfully registered");
	}
	
	@Override
	public Result add(Instructor entity) {
		instructorDao.save(entity);
		return new SuccessResult("Instructor added!");
	}

	@Override
	public Result delete(int id) {
		
		if(userService.existById(id)) {
			instructorDao.deleteById(id);
			return new SuccessResult("Instructor deleted!");
		}
		
		return new ErrorResult("instructor is not exist");	
	}

	@Override
	public Result update(Instructor entity) {
		Instructor updatedUser = getById(entity.getId()).getData();
		
		updatedUser.setFirstName(entity.getFirstName());
		updatedUser.setLastName(entity.getLastName());
		updatedUser.setEmail(entity.getEmail());
		updatedUser.setDepartmentName(entity.getDepartmentName());
		updatedUser.setPhoneNumber(entity.getPhoneNumber());
		updatedUser.setNationalityId(entity.getNationalityId());
		
		instructorDao.save(updatedUser);
		return new SuccessResult("Instructor updated!");
	}

	@Override
	public DataResult<Instructor> getById(int id) {
		Instructor instructor = instructorDao.findById(id);
		if(instructor == null) {
			return new ErrorDataResult<Instructor>(null, "Instructor is not exist");
		}
		return new SuccessDataResult<Instructor>(instructorDao.findById(id), "Instructor viewed!");
	}

	@Override
	public DataResult<List<Instructor>> getAll() {
		return new SuccessDataResult<List<Instructor>>(instructorDao.findAll(), "Instructors listed");
	}
	
	// TO DO:
	/*
	 		Instructor instructor = getById(instructorId).getData();
		Course course = courseService.getById(courseId).getData();
		instructor.getEnrolledCourses().add(course);
		instructorDao.save(instructor);
	 
	 */
	
	// --- TO DO ---
	@Override
	public Result addInstructorToCourse(int instructorId, int courseId) {
		return new SuccessResult("succesfull");
	}
	
	@Override
	public DataResult<CoursesWithInstructorDto> getAllCoursesByInstructorId(int instructorId){
		/*	Instructor instructor = getById(instructorId).getData();
		
		CoursesWithInstructorDto coursesWithInstructorDto = new CoursesWithInstructorDto();
		
		coursesWithInstructorDto.setEmail(instructor.getEmail());
		coursesWithInstructorDto.setCourses(instructor.getCourses());
		
		return new SuccessDataResult<CoursesWithInstructorDto>(coursesWithInstructorDto,"bir eğitmene ait kurslar listelendi");*/
		return null;
	} 
	
	public DataResult<List<Instructor>> birKurstakiEgitmenler(int courseId){
		//Course course = courseService.getById(courseId).getData();
		//course
		return null;
	}

}
