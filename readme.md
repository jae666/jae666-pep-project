# Social Media Blog API

# Project Description
The Social Media Blog API is a backend application designed for a micro-blogging platform. It allows users to register, log in, create, view, update, and delete messages. The API offers endpoints to manage user accounts and interactions with messages, making it a powerful tool for building a social media platform. This project implements a 3-layer architecture with controllers, services, and DAOs to ensure maintainable, scalable, and secure code.

## Technologies Used
	Java - version 11
	Javalin - version 5.0
	MySQL - version 8.0
	JDBC - version 5.0
	JUnit - version 5.7 (for testing)
	Mockito - version 3.7 (for mocking database and service interactions)

## Features
	User Registration: Allows users to create new accounts by providing a unique username and password.
	User Login: Users can log in with their credentials and access their account.
	Message Creation: Users can post new messages to the platform.
	Message Retrieval: Retrieve all messages or a specific message by its ID.
	Message Deletion: Users can delete their messages by providing a valid message ID.
	Message Update: Users can edit their messages with new content.
	User-specific Messages: Retrieve all messages posted by a particular user.

## Getting Started
To get started with this project, follow the steps below:

	Clone the repository:
	
	bash
	Copy code
	git clone (https://github.com/jae666/jae666-pep-project)
	Set up MySQL Database:
	
	Install MySQL on your machine if you don't have it yet. Download MySQL
	Create a new database and run the SQL script located in src/main/resources/SocialMedia.sql to set up the required tables.
	Example commands:
	
	bash
	Copy code
	mysql -u root -p
	CREATE DATABASE social_media_blog;
	USE social_media_blog;
	source /path/to/SocialMedia.sql;
	Configure Database Connection:
	
	In the ConnectionUtil.java file, update the connection settings with your MySQL database credentials.
	java
	Copy code
	public static final String URL = "jdbc:mysql://localhost:3306/social_media_blog";
	public static final String USER = "root";
	public static final String PASSWORD = "yourpassword";
	Running the Application:
	
	Run the Main.java file to start the API server.
	bash
	Copy code
	javac Main.java
	java Main
	Accessing the API:
	
	The API will run on localhost:8080.

## Usage
After setting up the project, you can interact with the following endpoints:

	POST /register: Create a new account.
	
	Example body:
	json
	Copy code
	{
	  "username": "newuser",
	  "password": "password123"
	}
	POST /login: Log in to the platform.
	
	Example body:
	json
	Copy code
	{
	  "username": "newuser",
	  "password": "password123"
	}
	POST /messages: Create a new message.
	
	Example body:
	json
	Copy code
	{
	  "posted_by": 1,
	  "message_text": "This is a new message!"
	}
	GET /messages: Retrieve all messages.
	
	GET /messages/{message_id}: Retrieve a specific message by ID.
	
	DELETE /messages/{message_id}: Delete a message by ID.
	
	PATCH /messages/{message_id}: Update a message.
	
	GET /accounts/{account_id}/messages: Retrieve messages posted by a specific user.

## Contributors
Jae Ma 

## License
This project uses the following license: MIT License.
