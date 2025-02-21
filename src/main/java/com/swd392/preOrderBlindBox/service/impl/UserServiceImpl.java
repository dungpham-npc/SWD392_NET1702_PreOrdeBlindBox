package com.swd392.preOrderBlindBox.service.impl;

import com.swd392.preOrderBlindBox.entity.User;
import com.swd392.preOrderBlindBox.enums.ErrorCode;
import com.swd392.preOrderBlindBox.exception.UserException;
import com.swd392.preOrderBlindBox.repository.UserRepository;
import com.swd392.preOrderBlindBox.security.SecurityUserDetails;
import com.swd392.preOrderBlindBox.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public User findByEmail(String mail) {
    return userRepository
        .findByEmail(mail)
        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
  }

  @Override
  public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByEmail(mail)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

    List<GrantedAuthority> authorityList =
        List.of(new SimpleGrantedAuthority(user.getRoleName().toString()));

    return SecurityUserDetails.build(user, authorityList);
  }
}
