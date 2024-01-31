package com.sanhil.service;
import jakarta.persistence.*;

@Entity
@Table(name = "tblUser",uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class userService {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer userId;
  @Column(name = "firstName")
  private String firstName;
  @Column(name = "lastName")
  private  String lastName;
  @Column(name = "email")

  private String email;
  @Column(name = "password")

  private String password;
  @Column(name = "role")
  private String role;

  userService(){

  }
  userService(String firstName,String lastName,String email,String password,String role){
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  userService(Integer userId,String firstName,String lastName,String email,String password,String role){
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public Integer getId(){
    return userId;
  }
  public void setId(Integer userId){
    this.userId = userId;
  }
	public String getFirstName(){
    return firstName;
	}
	public void setFirstName(String firstName){
    this.firstName = firstName;
	}
  public String getLastName(){
    return lastName;
  }
  public void setLastName(String lastName){
    this.lastName = lastName;
  }

  public String getEmail(){
    return email;
  }
  public void setEmail(String email){
    this.email = email;
  }

  public String getPassword(){
    return password;
  }

  public void setPassword(String password){
    this.password = password;
  }

  public String getRole(){
    return role;
  }
  public void setRole(String role){
    this.role = role;
  }

}
