package com.swd392.preOrderBlindBox.service.serviceimpl;

import com.swd392.preOrderBlindBox.common.enums.ErrorCode;
import com.swd392.preOrderBlindBox.common.exception.SignUpException;
import com.swd392.preOrderBlindBox.common.exception.UserException;
import com.swd392.preOrderBlindBox.entity.User;
import com.swd392.preOrderBlindBox.infrastructure.security.SecurityUserDetails;
import com.swd392.preOrderBlindBox.repository.repository.UserRepository;
import com.swd392.preOrderBlindBox.restcontroller.request.RegisterRequest;
import com.swd392.preOrderBlindBox.service.service.UserService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
  public void validateSignUp(RegisterRequest request) {
    boolean isExistsByPhoneNumber = userRepository.existsByPhone(request.getPhone());
    boolean isExistsByEmail = userRepository.existsByEmail(request.getEmail());

    boolean isPhoneAndEmailExisted = isExistsByPhoneNumber && isExistsByEmail;

    if (isPhoneAndEmailExisted) throw new SignUpException(ErrorCode.PHONE_AND_MAIL_EXIST);
    if (isExistsByEmail) throw new SignUpException(ErrorCode.EMAIL_EXIST);
    if (isExistsByPhoneNumber) throw new SignUpException(ErrorCode.PHONE_EXIST);
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

  @Override
  public Optional<User> getCurrentUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof UserDetails userDetails) {
      return userRepository.findByEmail(userDetails.getUsername());
    }
    return Optional.empty();
  }

  @Override
  @Transactional
  public User createUser(User user) {
    return userRepository.save(user);
  }
}
