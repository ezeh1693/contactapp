package com.contact.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * A mapped super class of all entity classes to be used in this project
 */
@Data
@MappedSuperclass
public class Model implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date created = new Date();

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Account createdBy;

}




