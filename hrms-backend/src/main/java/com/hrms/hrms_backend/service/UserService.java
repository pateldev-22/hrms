package com.hrms.hrms_backend.service;
import com.hrms.hrms_backend.constants.Role;
import com.hrms.hrms_backend.dto.user.UpdateUser;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.exception.CustomException;
import com.hrms.hrms_backend.mapper.UserMapper;
import com.hrms.hrms_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No user found"));
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("No User found with email:" + email));
    }

    public UpdateUser update(Long id,UpdateUser userDto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found with id:"+id));
        userMapper.updateEntity(user,userDto);
        User updated = userRepository.save(user);

        return userMapper.toDTO(updated);
    }

    public List<User> getAllEmployees(){
        return userRepository.findUserByRoleIsLike(Role.EMPLOYEE);
    }

}
