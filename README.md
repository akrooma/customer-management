# customer-management
Demo web app project for trying out Java and related technologies.
Basic REST API CRUD operations for a customer entity.

## Technology stack basics

* Java 17
* Spring Boot
* Gradle
* H2 
* Liquibase

## Web app folder structure
As it has been a while since I did anything in Java then the folder structure probably looks like a mishmash of Java and C# conventions.

	main
	├── java
	│   └── com.mysite.customers
	│		├── config				# Configuration files for different components used by the web app (e.g. OpenAPI swagger config).
	│		├── controller			# REST API controllers.
	│		├── domain				# Domain (model?) entities, their factories, validators and listeners.
	│		├── dto					# DTO-s.
	│		├── exception			# Custom exceptions.
	│		├── middleware			# Global handlers (e.g. ControllerAdvice exception handler)
	│		├── repository			# Repository interfaces (and their implementations).
	│		├── service				# Service interfactes and their implementations.
	├── resources 
	│   └── db						# Contains database schema snapshot and changelog.
    ...
	
## Web app tests folder structure

	test
	├── java
	│   └── com.mysite.customers
	│		├── controller.customer	# REST API controller endpoint tests.
	│		├── domain				# Customer entity listener tests.
	│		├── mapper				# Customer entity to DTO mapper tests.
	│		├── service.customer	# Customer service methods' tests.
	...
			
## Comments about some of the classes
DTO-s as records instead of classes, self-implemented **CustomerMapper** instead of something pre-existing like **ModelMapper**.

> I opted for records for their immutability and because the domain (model?) of the web application is rather simple.
>
> I implemented a CustomerMapper myself because again, the domain (model?) is rather simple. Pre-existing tools like **ModelMapper** seem to have experimental support for records. I would immagine it is possible to create immutable class-based DTO-s and use **ModelMapper** to handle the converting.

Completely self-implemented **CustomerFactory** and **CustomerValidator** instead of using things like the **Spring Validator interface** or **@Valdated** and **@Valid** annotations.

> Again, one of the reasons is that currently the domain (model?) is rather simple.
> Taking a little inspiration from DDD (domain-driven design) I wanted to keep the domain entities in an "always valid" state, meaning that the domain entity properties can never have invalid values.
> The domain entity constructor is protected so that all new entities are created using the factory, which in turn ensures the validity of the entity by using the validator before constructing a new entity.
> Entity methods for updating property values also use the same validator to validate new values.
> This means the entity is always valid whenever we need to create it or update it in the service layer. Issues may rise, when the factory and update methods are not implemented correctly, but the developer cannot create any data anomalies when using property implemented entity manipulation methods in the service layer.
> This type of approach would definitely need a revision when entities become more complex (more properties, related entities) and / or the valid state depends on other data from the database.

## Alternative web app implementation
Due to the simplicity of the domain of the web app, I could have:
* used classes for DTO-s so I could use something like **ModelMapper** for entity <-> DTO converions;
* **Spring Validator interface** or **@Validated** annotations for data integrity checks.
