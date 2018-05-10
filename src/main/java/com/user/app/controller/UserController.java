package com.user.app.controller;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.user.app.entity.User;
import com.user.app.exception.UserException;
import com.user.app.model.ResponseVO;
import com.user.app.model.UserVO;
import com.user.app.service.UserService;
import com.user.app.util.AppLogger;
import com.user.app.util.IdGenerator;
import com.user.app.util.InputValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Sukanta
 * 
 * UserController exposed service for user data CRUD operation
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(path = "/users")
@Api(value = "usermanagement", description = "Operations pertaining to users data management")
public class UserController {
	@Autowired
	private UserService userService;

	@ApiOperation(value = "View a list of available Users", response = Iterable.class)
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Iterable<User> getAllUsers() {
		AppLogger.getLogger().info("Get all users..");
		return userService.getAlluser();
	}

	@ApiOperation(value = "View of available User based on Id", response = User.class)
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public @ResponseBody User getUser(@ApiParam @PathVariable long id) {
		AppLogger.getLogger().info("Get user by Id:" + id);
		return userService.getUserById(id);
	}

	@ApiOperation(value = "Create a new user", response = ResponseVO.class)
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> addNewUser(
			@ApiParam @RequestBody UserVO userVo) throws UserException {
		AppLogger.getLogger().info("input data: " + toStringInput(userVo));
		// input validation
		if (!InputValidator.isValidateInput(userVo)) {
			AppLogger.getLogger().info("Input data not valid.. ");
			throw new UserException("INPUT.DATA.INVALID",
					"Input data not valid..");
		}
		User user = userService.saveUser(setUserData(userVo));
		AppLogger.getLogger().info(
				"User Created successfully" + toStringInput(user));
		return new ResponseEntity<ResponseVO>(new ResponseVO(true,
				"New user Created successfully"), HttpStatus.CREATED);
	}

	@ApiOperation(value = "Update user", response = ResponseVO.class)
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<?> updateUser(
			@ApiParam @RequestBody UserVO userVo) throws UserException {
		AppLogger.getLogger().info("input data: " + toStringInput(userVo));
		// input validation
		if (!isValidId(userVo.getId())
				|| !InputValidator.isValidateInput(userVo)) {
			AppLogger.getLogger().info("Input data not valid.. ");
			throw new UserException("INPUT.DATA.INVALID",
					"Input data not valid..");
		}
		User user = userService.saveUser(setUserData(userVo));
		AppLogger.getLogger().info(
				"User data updated successfully.." + toStringInput(user));
		return new ResponseEntity<ResponseVO>(new ResponseVO(true,
				"User data updated successfully"), HttpStatus.OK);
	}

	@ApiOperation(value = "Delete user", response = ResponseVO.class)
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<?> deleteUser(
			@ApiParam @PathVariable Long id) throws UserException {
		// input validation
		if (isValidId(id)) {
			AppLogger.getLogger().info("User Id Invalid..");
			throw new UserException("USER.ID.INVALID",
					"User to delete doesn´t exist");
		}
		User n = new User();
		n.setId(id);
		userService.deleteUser(n);
		AppLogger.getLogger()
				.info("User{id:" + id + "} deleted successfully..");
		return new ResponseEntity<ResponseVO>(new ResponseVO(true,
				"User has been deleted"), HttpStatus.OK);
	}

	@RequestMapping(value = "*", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> handle() {
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	private boolean isValidId(Long id) {
		boolean isValidUserId = (id == null ? true : null == userService
				.getUserById(id));
		AppLogger.getLogger().info("isValidUser: " + isValidUserId);
		return isValidUserId;
	}

	private User setUserData(UserVO userVo) {
		User n = new User();
		n.setId(IdGenerator.createID());
		n.setName(userVo.getName());
		n.setEmail(userVo.getEmail());
		n.setProfession(userVo.getProfession());
		return n;
	}

	private String toStringInput(Object obj) {
		return new ReflectionToStringBuilder(obj, new RecursiveToStringStyle())
				.toString();
	}
}
