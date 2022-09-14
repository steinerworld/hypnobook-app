package net.steinerworld.hypnobook.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "app_user")
@Getter @Setter
public class AppUser {
   @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_seq") @SequenceGenerator(name = "app_user_seq")
   @Column(name = "id", nullable = false) private Long id;
   @Column(name = "username") private String username;
   @Column(name = "pass") private String password;
   @Column(name = "roles") private String roles;


}