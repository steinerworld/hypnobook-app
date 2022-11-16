package net.steinerworld.hypnobook.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity @Table(name = "app_user")
@Getter @Setter @Accessors(chain = true)
@ToString
public class AppUser {
   @Id @GeneratedValue
   @Column(name = "id") private UUID id;
   @Column(name = "username") private String username;
   @Column(name = "name") private String name;
   @ToString.Exclude
   @Column(name = "pass") private String password;
   @Column(name = "roles") private String roles;
   @Column(name = "theme") private String theme;
   @ToString.Exclude
   @Lob @Type(type = "org.hibernate.type.ImageType")
   @Column(name = "profile_picture")
   private byte[] profilePicture;
}