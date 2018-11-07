package com.apap.tutorial8.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apap.tutorial8.model.PasswordModel;
import com.apap.tutorial8.model.PilotModel;
import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;
	
	String pattern = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,}";
	
	@RequestMapping(value= "/addUser", method = RequestMethod.POST)
	private String addUserSubmit(@ModelAttribute UserRoleModel user) {
		
		userService.addUser(user);
		return "home";
	}
	
	@RequestMapping(value= "/updatePassword", method = RequestMethod.GET)
	private String updateUserPassword() {
		return "updatePassword";
	}
	
	@RequestMapping(value= "/updatePassword", method = RequestMethod.POST)
	private String updateUserPasswordSubmit(@ModelAttribute PasswordModel password, Model model ) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		UserRoleModel user = userService.getUserDetailByUserName(SecurityContextHolder.getContext().getAuthentication().getName());	
		
		
		if(passwordEncoder.matches(password.getCurrentPassword(), user.getPassword())) {
			if(password.getConfirmPassword().equals(password.getNewPassword())) {
				if(password.getNewPassword().matches(pattern)) {
					userService.changePassword(user, password.getNewPassword());
					model.addAttribute("message2", "password berhasil diubah" );
				}
				else {
					model.addAttribute("message", "Password anda harus mengandung angka, huruf dan minimal memiliki 8 karakter");
				}
			}
			else {
				model.addAttribute("message", "password baru tidak cocok" );
			}
		}
		else {
			model.addAttribute("message", "password lama salah" );
		}
		System.out.println(user.getPassword());
		return "updatePassword";
	}
	
	
}
