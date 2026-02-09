package com.hrms.hrms_backend.mapper;

import com.hrms.hrms_backend.dto.user.UpdateUser;
import com.hrms.hrms_backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UpdateUser toDTO(User user){
        if(user == null ) return null;

        UpdateUser dto = new UpdateUser();

        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setProfilePhotoUrl(user.getProfilePhotoUrl());


        return dto;
    }

    public User toEntity(UpdateUser dto){
        if(dto == null) return null;

        User user = new User();

        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setLastName(dto.getLastName());
        user.setFirstName(dto.getFirstName());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setProfilePhotoUrl(dto.getProfilePhotoUrl());

        return user;
    }

    public void updateEntity(User user,UpdateUser dto){
        if(dto == null) return;

        user.setFirstName(dto.getFirstName());
        user.setPhone(dto.getPhone());
        user.setLastName(dto.getLastName());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setEmail(dto.getEmail());
        user.setProfilePhotoUrl(dto.getProfilePhotoUrl());
    }
}
