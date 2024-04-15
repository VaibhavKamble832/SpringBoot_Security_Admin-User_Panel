package com.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.web.dto.MyUser;
import com.web.repository.MyUserRepo;

import jakarta.servlet.http.HttpSession;

@Service
public class MyUserService {
	
	@Autowired
	MyUserRepo myUserRepo;
	
	public MyUser addUser(MyUser myUser) {
		return myUserRepo.save(myUser);
	}	
	
	public void removeSessionMsg() {
		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest().getSession();
		
		session.removeAttribute("msg");
	}
	public void removeSessionEMsg() {
		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest().getSession();
		
		session.removeAttribute("emsg");
	}
	
	public List<MyUser> getUserList(){
		return myUserRepo.findAll();
	}
	
	public List<MyUser> getOnlyUserList(String role){
		return myUserRepo.findAllByRole(role);
	}
	
	public boolean deleteUserById(int id) {
		myUserRepo.deleteById(id);
		return true;
	}
	
	
}
