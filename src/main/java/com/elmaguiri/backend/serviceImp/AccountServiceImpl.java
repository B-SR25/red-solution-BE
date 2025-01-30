package com.elmaguiri.backend.serviceImp;

import com.elmaguiri.backend.Service.dtos.UserDto;
import com.elmaguiri.backend.Service.services.AccountService;
import com.elmaguiri.backend.dao.entities.Role;
import com.elmaguiri.backend.dao.entities.User;
import com.elmaguiri.backend.Service.mappers.ModelMapperConfig;
import com.elmaguiri.backend.dao.repositories.RoleRepository;
import com.elmaguiri.backend.dao.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private final ModelMapperConfig modelMapperConfig;
    private SendEmailServiceImpl sendEmailService;
    @Override
    public UserDto addNewUser(UserDto userDto) {
        User user = userRepository.findByUsername((userDto.getSurname()+"."+userDto.getName()));
        if (user != null) throw new RuntimeException("User already exists");
        if(!userDto.getPassword().equals(userDto.getConfirmPassword())) throw new RuntimeException("Passwords do not match");
       UserDto userDto1 = UserDto.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .status(userDto.getStatus())
                .username(userDto.getSurname()+"."+userDto.getName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .confirmPassword(passwordEncoder.encode(userDto.getConfirmPassword()))
                .mail(userDto.getMail())
                .jobPosition(userDto.getJobPosition())
                .build();
        user = modelMapperConfig.fromUserDto(userDto1);
        System.out.println("userDto: " + user);
        User savedUser = userRepository.save(user);
        String body="Bonjour ,\n" +
                "\n" +
                "Bienvenue chez le cabinet d’experts comptables El Maguiri & Associés ! Nous sommes ravis de vous accueillir parmi nous et nous nous réjouissons de vous accompagner sur notre plateforme de gestion des opérations juridiques. Vous trouverez ci-dessous les informations de votre nouveau compte :\n" +
                "\n" +
                "Nom d'utilisateur : "+ user.getUsername()+ "\n" +
                "Mot de passe : "+ userDto.getPassword()+ "\n" +
                "\n" +
                "Pour commencer, veuillez suivre les étapes suivantes :\n" +
                "\n" +
                "1-Connexion : Rendez-vous sur notre site web à l'adresse [https://musical-blini-c37a39.netlify.app//signin].\n" +
                "2-Entrez vos identifiants : Utilisez le nom d'utilisateur et le mot de passe fournis ci-dessus.\n" +
                "3-Configurez votre compte : Pour des raisons de sécurité, nous vous recommandons vivement de changer votre mot de passe lors de votre première connexion.\n\n" +
                "\n" +
                "Si vous avez des questions ou besoin d'aide, n'hésitez pas à contacter notre équipe de support à [Insérer l'email/le numéro de téléphone du support].\n" +
                "\n" +
                "Cordialement,";
//       // sendEmailService.sendEmail(user.getMail(),"Bienvenue chez Cabinet d’experts comptables El Maguiri & Associés! Vos informations de compte",body);
        return modelMapperConfig.fromUser(savedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(modelMapperConfig::fromUser)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto archive(Long id, UserDto userDto){
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User userToUpdate = userOptional.get();
            userToUpdate.setStatus(userDto.getStatus());
            User updatedUser = userRepository.save(userToUpdate);
            return modelMapperConfig.fromUser(updatedUser);
        }
        else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User userToUpdate = userOptional.get();
            if (this.passwordEncoder.matches(userDto.getOldPassword(), userToUpdate.getPassword()) || Objects.equals(userDto.getEditedBy(), "ROLE_ADMIN")) {
                if(!userDto.getPassword().equals(userDto.getConfirmPassword())) throw new RuntimeException("Passwords do not match");

                userDto = UserDto.builder()
                        .name(userDto.getName())
                        .surname(userDto.getSurname())
                        .username(userDto.getSurname()+"."+userDto.getName())
                        .password(Objects.equals(userDto. getPassword(), "")?userToUpdate.getPassword():passwordEncoder.encode(userDto.getPassword()))
                        .confirmPassword(passwordEncoder.encode(userDto.getConfirmPassword()))
                        .status(userDto.getStatus())
                        .mail(userDto.getMail())
                        .roleId(userDto.getRoleId())
                        .build();
                 modelMapperConfig.updateUserFromDto(userToUpdate, userDto);
                User updatedUser = userRepository.save(userToUpdate);
                return modelMapperConfig.fromUser(updatedUser);
            } else {
                throw new RuntimeException("The current password is incorrect" + id);
            }
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    public Role addNewRole(String role) {
        Role role1=roleRepository.findByRoleName(role);
        if(role1!=null) throw new RuntimeException("Role already exist");
        role1=Role.builder()
                .roleName(role)
                .build();
        return roleRepository.save(role1);
    }

    @Override
    public void addRoleToUser(UserDto userDto,Long id) {
        System.out.println(userDto.getUsername());
        User user = userRepository.findByUsername(userDto.getUsername());
        System.out.println(user);
        if(user==null) throw new RuntimeException("User ! exists");
        Role role1=roleRepository.findById(id).get();
        user.getRoles().add(role1);
    }
/*
    @Override
    public void addRoleToUser(UserDto userDto,String roleName) {
        System.out.println(userDto.getUsername());
        User user = userRepository.findByUsername(userDto.getUsername());
        System.out.println(user);
        if(user==null) throw new RuntimeException("User ! exists");
        Role role1=roleRepository.findByRoleName(roleName);
        user.getRoles().add(role1);
    }
*/
    @Override
    public void removeRoleFromUser(String username, String role) {
        User user = userRepository.findByUsername(username);
        Role role1=roleRepository.findByRoleName(role);
        user.getRoles().remove(role1);
    }
    @Override
    public User loadUserByUsername(String username) {
        System.out.println("here is the problem ");
         User user=userRepository.findByUsername(username);
        if(user==null){
            System.out.println(username + " not found");
        }else{
            System.out.println("user");
        }
         return user;
    }
}
