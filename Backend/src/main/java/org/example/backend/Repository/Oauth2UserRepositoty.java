package org.example.backend.Repository;

import org.example.backend.Entity.Oauth2UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Oauth2UserRepositoty extends JpaRepository<Oauth2UserEntity,Long> {


}
