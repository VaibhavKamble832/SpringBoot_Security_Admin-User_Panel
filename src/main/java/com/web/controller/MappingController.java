package com.web.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.dto.MyUser;
import com.web.repository.MyUserRepo;
import com.web.service.MyUserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MappingController {

	@Autowired
	MyUserRepo myUserRepo;

	@Autowired
	MyUserService myUserService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
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
	

	@GetMapping("/")
	public String indexPage() {
		return "index";
	}

	@GetMapping("/register")
	public String registerPage() {
		return "register";
	}

	@GetMapping("/signing")
	public String loginPage() {
		return "customlogin";
	}

	@GetMapping("/user/home")
	public String userHome() {
		return "user_home";
	}

	@GetMapping("/admin/home")
	public String adminHome() {
		return "admin_home";
	}

	
	@GetMapping("/user/changePass")
	public String loadUserChangePassword() {
		return "change_password";
	}

	@GetMapping("/admin/changePass")
	public String loadAdminChangePassword() {
		return "change_password";
	}

	
	
	@PostMapping("/changePass")
	public String changePassword(Principal p, @RequestParam("oldPass") String oldPass, 
			@RequestParam("newPass") String newPass, HttpSession session) {
		
		String email = p.getName();
		MyUser myUser = myUserRepo.findByEmail(email);
		
		boolean f = bCryptPasswordEncoder.matches(oldPass, myUser.getPassword());
		
		if(f) {
			myUser.setPassword(bCryptPasswordEncoder.encode(newPass));
			MyUser updatePassUser = myUserService.addUser(myUser);
			
			if (updatePassUser != null) {
				session.setAttribute("msg", "Password Changed Successfully");
			}
			else {
				session.setAttribute("emsg", "Something went wrong");
			}
		}
		else {
			session.setAttribute("emsg", "Your old password is not matching");
		}
		
		if(myUser.getRole() == "ROLE_ADMIN") {
			return "redirect:/admin/changePass";
		}
		else {
			return "redirect:/user/changePass";
		}
	}
	
	
	
	
	@GetMapping("/user/editProfile")
	public String editUserPage() {
		return "edit_profile";
	}
	@GetMapping("/admin/editProfile")
	public String editAdminPage() {
		return "edit_profile";
	}

	@PostMapping("/editProfile")
	public String editUser(Principal p, @RequestParam("name") String name,
			@RequestParam("email") String email, HttpSession session){
		
		String pEmail = p.getName();
		MyUser myUser = myUserRepo.findByEmail(pEmail);
		
		myUser.setName(name);
		myUser.setEmail(email);
		
		MyUser updatedUser = myUserService.addUser(myUser);
		
		if (updatedUser != null) {
			session.setAttribute("msg", "Profile Details Updated Successfully...!");
		}
		else {
			session.setAttribute("emsg", "Something went wrong");
		}
		
		if(myUser.getRole() == "ROLE_ADMIN") {
			return "redirect:/admin/editProfile";
		}
		else {
			return "redirect:/user/editProfile";
		}

	}
	
	
}
