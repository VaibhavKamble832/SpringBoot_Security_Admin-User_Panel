package com.web.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.web.dto.MyUser;
import com.web.repository.MyUserRepo;
import com.web.service.MyUserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MyUserController {

	@Autowired
	MyUserService myUserService;

	@Autowired
	MyUserRepo myUserRepo;

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	
	@ModelAttribute
	public void commonUser(Principal p, Model model) {
	    if (p != null) {
	        String email = p.getName();
	        MyUser user = myUserRepo.findByEmail(email);
	        if (user != null) {
	            model.addAttribute("user", user);
	        }
	    }
	}


	
	@PostMapping("/register")
	public String registerUser(@ModelAttribute MyUser myUser, HttpSession session) {

		myUser.setPassword(encoder.encode(myUser.getPassword()));
		MyUser u = myUserService.addUser(myUser);

		if (u != null) {
			session.setAttribute("msg", "Register Successfully");
		} else {
			session.setAttribute("msg", "Please fill all Details...");
		}

		return "redirect:/register";
	}
	
	@GetMapping("/admin/userList")
	public String userListPage(Model model ) {
		List<MyUser> userList = myUserService.getOnlyUserList("ROLE_USER");
		model.addAttribute("userList", userList);
		
		return "user_list" ;
	}
	
	@GetMapping("/admin/deleteUser/{id}")
	public String deleteUser(@PathVariable(value = "id") Integer id, HttpSession session) {
		myUserService.deleteUserById(id);
		session.setAttribute("msg", "User is Deleted Successfully..!");
		return "redirect:/admin/userList";
	}
	
	
	
	

}
