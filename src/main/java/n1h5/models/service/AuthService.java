package n1h5.models.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import n1h5.models.domain.auth.Role;
import n1h5.models.domain.auth.Users;
import n1h5.models.domain.request.RegisterRequest;
import n1h5.models.domain.response.UserResponse;
import n1h5.models.repository.AuthRepository;
import n1h5.models.repository.RoleRepository;
import n1h5.models.repository.UsersRepository;

@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEndcoder;
    private final RoleRepository roleRepository;
    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEndcoder,UsersRepository usersRepository,RoleRepository roleRepository){
        this.authRepository=authRepository;
        this.passwordEndcoder=passwordEndcoder;
        this.usersRepository=usersRepository;
        this.roleRepository=roleRepository;
    }
    public Users registerAccount(RegisterRequest registerRequest){
            Users newAccount = new Users();
            newAccount.setEmail(registerRequest.getEmail());
            newAccount.setFirstName(registerRequest.getFirstName());
            newAccount.setLastName(registerRequest.getLastName());
            newAccount.setUsername(registerRequest.getUsername());
            newAccount.setPhone(registerRequest.getPhone());
            String rawPassword=registerRequest.getPassword();
            String hashPassword =passwordEndcoder.encode(rawPassword);
            newAccount.setPassword(hashPassword);
            Role defaultRole = this.roleRepository.findByName("ROLE_USER").get();
            Set<Role> roles = new HashSet<>();
            roles.add(defaultRole);
            newAccount.setRoles(roles);
            this.usersRepository.save(newAccount);
        return newAccount;
    
    }
    public UserResponse userDTO(Users account) {
        UserResponse usersDTO=new UserResponse();
        usersDTO.setEmail(account.getEmail());
        usersDTO.setUsername(account.getUsername());
        usersDTO.setFirstName(account.getFirstName());
        usersDTO.setLastName(account.getLastName());
        usersDTO.setPhone(account.getPhone());
        return usersDTO;
    }
    

}
