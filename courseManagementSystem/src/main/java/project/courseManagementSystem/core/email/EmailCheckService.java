package project.courseManagementSystem.core.email;

import org.springframework.stereotype.Service;

@Service
public interface EmailCheckService {
	public boolean emailCheck(String email);
}
