package org.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;

import java.security.Timestamp;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(unique = true,nullable = false)
  @NotNull
  private String email;
  @NotNull
  private String password;
  @NotNull
  private String firstName;
  @NotNull
  private String lastName;
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Enumerated(EnumType.STRING)
  private Set<Role> roles;
  private String imageUrl;
  @CreatedDate
  private Timestamp createdAt;

  public User(String email, String password, String firstName, String lastName, String imageUrl) {
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.imageUrl = imageUrl;
  }
}
