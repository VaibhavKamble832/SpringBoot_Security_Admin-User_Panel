package com.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.dto.MyUser;

@Repository
public interface MyUserRepo extends JpaRepository<MyUser, Integer>{

	public MyUser findByEmail(String email);
	
	
	public List<MyUser> findAllByRole(String Role);
}
