# springboot-with-mongoDB

Spring Boot - Accessing data with MongoDB :: Learn how to set up and manage user details on MongoDB and how to configure Spring Boot to connect to it at run time.

## **Setup Application:**

> **Note:** Find the MongoDB connection details in "/src/main/resources/application.properties"

### Create the application.properties file

>- In the sources folder, you create a resource file src/main/resources/application.properties

```
spring.data.mongodb.database=test1
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
```
> **Note:** Here, spring.jpa.hibernate.ddl-auto can be none, update, create, create-drop, refer to the Hibernate documentation for details.
> [Database initialization](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html)

### Create the @Entity model

/src/main/java/com/user/app/entity/User.java

```
package com.user.app.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="user") // This tells Repository to make a collection out of this class
public class User {
    @Id
    private Long id;

    private String name;

    private String email;
    
    private String profession;

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
    
}
```
 
This is the entity class which translate into a DB Collection.

### Create the repository

/src/main/java/com/user/app/repository/UserRepository.java

```
package com.user.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.user.app.entity.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends MongoRepository<User, Long> {

}
```

This is the repository interface, this will be automatically implemented by Spring in a bean with the same name with changing case The bean name will be userRepository

### Create a new controller for your Spring application

src/main/java/hello/MainController.java

```
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

```

The above example does specify GET, PUT, POST, and DELETE. @RequestMapping maps all HTTP operations according to the http method type. If you need same method multiple time within controller then you need to define unique path. Example:  ``` @RequestMapping(path="/testPath") ```

### Request and Response Value Object

/src/main/java/com/user/app/model/ResponseVO.java


```
package com.user.app.model;

public class ResponseVO {
	
	private boolean success;
	private String message;
	
	public ResponseVO(){}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
```

/src/main/java/com/user/app/model/UserVO.java
```
package com.user.app.model;

public class UserVO {
    private Long id;

    private String name;

    private String email;
    
    private String profession;

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
    
}

```
Above model is use to create/map the request and response.

### Make the application executable

Although it is possible to package this service as a traditional WAR file for deployment to an external application server, the simpler approach demonstrated below creates a standalone application. You package everything in a single, executable JAR file, driven by a good old Java main() method. Along the way, you use Spring’s support for embedding the Tomcat servlet container as the HTTP runtime, instead of deploying to an external instance.

src/main/java/com/user/app/UserDataManagementApplication.java

```
package com.user.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserDataManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserDataManagementApplication.class, args);
	}
}

```
### Build an executable JAR

You can run the application from the command line with Maven. Or you can build a single executable JAR file that contains all the necessary dependencies, classes, and resources, and run that. This makes it easy to ship, version, and deploy the service as an application throughout the development lifecycle, across different environments, and so forth.

If you are using Maven, you can run the application using ./mvnw spring-boot:run. Or you can build the JAR file with ./mvnw clean package. Then you can run the JAR file:

java -jar target/usermanagement-0.0.1-SNAPSHOT.jar
The procedure above will create a runnable JAR. You can also opt to build a classic WAR file instead.
Logging output is displayed. The service should be up and running within a few seconds.

### Test the application

Now that the application is running, you can test it.

Install Postman. [Installing the Postman Chrome App](https://www.getpostman.com/docs/introduction)

> **Note:** Install Postman. Postman is available as a native app (recommended) for Mac / Windows / Linux, and as a Chrome App. The Postman Chrome app can only run on the Chrome browser. To use the Postman Chrome app, you will first need to install Google Chrome. 


 Import the collection file "[SpringBootUserDataManagement.postman_collection.json](https://github.com/suku19/springboot-with-mongoDB/blob/master/SpringBootUserDataManagement.postman_collection.json)". Click on the ‘Import’ button on the top bar, and paste a URL to the collection, or the collection JSON itself, and click ‘Import’. Find the more details in [Getting started with postman Collections](https://www.getpostman.com/docs/collections)
 
 **Test Results:**

Add User

----------

![Add User](https://github.com/suku19/springboot-with-mongoDB/blob/master/img/addUser.JPG)

----------
Get User By ID

----------

![getUserByID](https://github.com/suku19/springboot-with-mongoDB/blob/master/img/getUserByID.JPG)

----------
Get All User

----------
![getAllUser](https://github.com/suku19/springboot-with-mongoDB/blob/master/img/getAllUser.JPG)

----------
Update User

----------

![updateUser](https://github.com/suku19/springboot-with-mongoDB/blob/master/img/updateUser.JPG)

----------
Delete user by Id

----------

![deleteById](https://github.com/suku19/springboot-with-mongoDB/blob/master/img/deleteById.JPG)

![deleteById](https://github.com/suku19/springboot-with-mongoDB/blob/master/img/deleteById.JPG)

