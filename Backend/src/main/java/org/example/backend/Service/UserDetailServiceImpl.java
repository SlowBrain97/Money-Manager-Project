package org.example.backend.Service;

import lombok.RequiredArgsConstructor;
import org.example.backend.Entity.CustomUserDetails;
import org.example.backend.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username).map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("usename not found"));
  }
}
