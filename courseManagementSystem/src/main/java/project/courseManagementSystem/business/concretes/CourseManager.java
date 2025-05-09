package project.courseManagementSystem.business.concretes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import project.courseManagementSystem.business.abstracts.CourseService;
import project.courseManagementSystem.business.abstracts.InstructorService;
import project.courseManagementSystem.business.abstracts.LessonService;
import project.courseManagementSystem.business.abstracts.StudentService;
import project.courseManagementSystem.business.constants.Messages;
import project.courseManagementSystem.business.validationRules.CourseValidatorService;
import project.courseManagementSystem.core.utilities.results.DataResult;
import project.courseManagementSystem.core.utilities.results.ErrorDataResult;
import project.courseManagementSystem.core.utilities.results.ErrorResult;
import project.courseManagementSystem.core.utilities.results.Result;
import project.courseManagementSystem.core.utilities.results.SuccessDataResult;
import project.courseManagementSystem.core.utilities.results.SuccessResult;
import project.courseManagementSystem.dataAccess.abstracts.CourseDao;
import project.courseManagementSystem.entities.concretes.Course;
import project.courseManagementSystem.entities.concretes.Instructor;
import project.courseManagementSystem.entities.concretes.Lesson;
import project.courseManagementSystem.entities.concretes.Student;
import project.courseManagementSystem.entities.dtos.CourseDto;

@Service
public class CourseManager implements CourseService {

	private CourseDao courseDao;
	private CourseValidatorService courseValidatorService;
	private InstructorService instructorService;
	private LessonService lessonService;
	private StudentService studentService;

	@Autowired
	public CourseManager(CourseDao courseDao, CourseValidatorService courseValidatorService,
			InstructorService instructorService, LessonService lessonService, StudentService studentService) {
		this.courseDao = courseDao;
		this.courseValidatorService = courseValidatorService;
		this.instructorService = instructorService;
		this.lessonService = lessonService;
		this.studentService = studentService;
	}

	@Override
	public Result save(CourseDto courseDto) {
		try {
			if (!courseValidatorService.checkIfCourseInfoIsFull(courseDto)) {
				return new ErrorResult(Messages.enterAllInfo);
			}
			Course course = new Course();
			course.setStartDate(courseDto.getStartDate());
			course.setEndDate(courseDto.getEndDate());
			course.setName(courseDto.getName());
			
			List<Student> students = new ArrayList<Student>();
			for (Integer id : courseDto.getStudentIds()) {
				Student student = studentService.getById(id).getData();
				student.setCourse(course);
				students.add(student);
			}
			course.setStudents(students);
			
			
			courseDao.save(course);
			return new SuccessResult(Messages.added);
		} 
		catch (NullPointerException ex) {
			return new ErrorResult(ex.toString());
		}
		catch (HttpMessageNotReadableException ex) {
			return new ErrorResult(ex.toString());
		}
		catch (Exception e) {
			return new ErrorResult(e.toString());
		}
	}
	
	@Override
	public Result add(Course entity) {
		return null;
	}

	@Override
	public Result delete(int id) {
		try {
			if(getById(id).getData() == null) {
				return new ErrorResult(Messages.isNotExist);
			}
			courseDao.deleteById(id);
			return new SuccessResult(Messages.deleted);
		} 
		//Veri Bütünlüğü İhlali İstisnası:
		//eğer silmek istediğim kurs, 
		//kendisi ile ilişkili olan tablolarda kayıtlı ise ve ben de onu silmeye çalışıyorsam:
		catch (DataIntegrityViolationException ex) {
			return new ErrorResult(ex.getMessage());
			//"message": "could not execute statement; SQL [n/a]; constraint [null];
			// nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"
		}
		
	}

	@Override
	public Result update(Course entity) {
		courseDao.save(entity);
		return new SuccessResult(Messages.updated);
	}

	@Override
	public DataResult<Course> getById(int id) {
		Course course = courseDao.findById(id);
		if(course == null) {
			return new ErrorDataResult<Course>(null, Messages.isNotExist);
		}
		return new SuccessDataResult<Course>(course, Messages.viewed);
	}

	@Override
	public DataResult<List<Course>> getAll() {
		return new SuccessDataResult<List<Course>>(courseDao.findAll(), Messages.listed);
	}
	
	
	@Override
	public Result addInstructorToCourse(int instructorId, int courseId) {
		Instructor instructor = instructorService.getById(instructorId).getData();
		Course course = getById(courseId).getData();
		course.getEnrolledInstructors().add(instructor);
		courseDao.save(course);
		return new SuccessResult(Messages.instructorAddedToCourse);
	}
	
	@Override
	public Result addLessonToCourse(int lessonId, int courseId) {
		Lesson lesson = lessonService.getById(lessonId).getData();
		Course course = getById(courseId).getData();
		course.getEnrolledLessons().add(lesson);
		courseDao.save(course);
		return new SuccessResult(Messages.lessonAddedToCourse);
	}

	@Override
	public DataResult<List<Course>> findByName(String name) {
		return new SuccessDataResult<List<Course>>(courseDao.findByName(name), Messages.listed);
	}

	


}
