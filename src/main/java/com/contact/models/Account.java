package com.contact.models;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.contact.enums.AccountType;

import lombok.Data;


/**
 *  A sample entity class for our use case, that follows java-bean validation constraints.
 *  It also uses the lombok project's @Data to reduce the number of boiler plate code in this application
 * @author emmanuel
 */
@Data
@Entity
@Table(name="accounts")
public class Account extends Model {

    @Size(min=3, max=30)
    private String name;

    @Pattern(regexp=".*@.*", message="Must be an email address")
    @Column(unique=true)
    private String email;

    @Size(min=5)
    private String password;

    @Pattern(regexp="\\d{11}", message="Must be a valid phone number")
    @Column(unique=true)
    private String phone;

    private Boolean active;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

}
