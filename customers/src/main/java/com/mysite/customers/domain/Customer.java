package com.mysite.customers.domain;

import com.mysite.customers.domain.config.CustomerPropertyConfiguration;
import com.mysite.customers.domain.listener.CustomerListener;
import com.mysite.customers.exception.ValidationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.Date;

/*
 * Handling data model changes:
 * Create a new changeset based on the data model snapshot.
 * Generate a new data model snapshot file.
 *
 * Depending on the entity changes, the following classes / records need to be updated accordingly:
 * CustomerDto.java
 * CustomerDataDto.java
 * CustomerValidator.java
 */

@Getter
@Setter
@Entity
@Table(name = "customer")
@EntityListeners(CustomerListener.class)
public class Customer extends AbstractPersistable<Long> {

    @Column(name = "first_name", nullable = false)
    @NotNull(message = "First name must be  specified")
    @Size(min = CustomerPropertyConfiguration.FirstName.MIN_SIZE, max = CustomerPropertyConfiguration.FirstName.MAX_SIZE)
    private String firstName;
    public static final String FIRST_NAME_PROP_NAME = "firstName";

    @Column(name = "last_name", nullable = false)
    @NotNull(message = "Last name must be  specified")
    @Size(min = CustomerPropertyConfiguration.LastName.MIN_SIZE, max = CustomerPropertyConfiguration.LastName.MAX_SIZE)
    private String lastName;
    public static final String LAST_NAME_PROP_NAME = "lastName";

    /*
     * E-mail length information:
     * https://datatracker.ietf.org/doc/html/rfc3696#section-3
     */
    @Column(name = "email", nullable = false)
    @NotNull(message = "E-mail must be  specified")
    @Size(min = CustomerPropertyConfiguration.Email.MIN_SIZE, max = CustomerPropertyConfiguration.Email.MAX_SIZE)
    private String email;
    public static final String EMAIL_PROP_NAME = "email";

    /*
     * Regarding props "createdDtime" and "modifiedDtime":
     * Could use data type "Instant" for metadata date+time UTC fields.
     * Use a base entity abstraction for these two props in case of 2 or more domain entities.
     */
    @Column(name = "created_dtime", nullable = false)
    private Date createdDtime;

    @Column(name = "modified_dtime", nullable = false)
    private Date modifiedDtime;

    protected Customer() {}

    /*
     * Constructor is protected so all entities are created through the CustomerFactory,
     * which will validate the property values against business rules and data schema limitation.
     */
    protected Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public void update(String firstName, String lastName, String email) throws ValidationException {
        /*
         * We can get away with using the entity validator here because
         * we do not have to do any checks against data from the database.
         * More complicated actions (service methods) should probably get their own separate validation logic.
         */
        CustomerValidator.ensureValidity(firstName, lastName, email);

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}