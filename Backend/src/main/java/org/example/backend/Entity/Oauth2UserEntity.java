package org.example.backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Oauth2UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private  long id;
  private String provider;
  private Long providerId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  public Oauth2UserEntity(String provider, Long providerId, User user) {
    this.provider = provider;
    this.providerId = providerId;
    this.user = user;
  }


}
