package com.elmaguiri.backend;

import com.elmaguiri.backend.Service.dtos.EtapePrestationDTO;
import com.elmaguiri.backend.Service.dtos.RoleDto;
import com.elmaguiri.backend.Service.dtos.UserDto;
import com.elmaguiri.backend.Service.services.AccountService;
import com.elmaguiri.backend.Service.services.RoleService;
import com.elmaguiri.backend.Service.services.UserService;
import com.elmaguiri.backend.dao.entities.Prestation;
import com.elmaguiri.backend.dao.entities.User;
import com.elmaguiri.backend.dao.repositories.PrestationRepository;
import com.elmaguiri.backend.dao.repositories.UserRepository;
import com.elmaguiri.backend.serviceImp.EtapePrestationServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
//@CrossOrigin(origins = "https://musical-blini-c37a39.netlify.app/")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println("hello world!!!!");
	}

	@Bean
	CommandLineRunner commandLineRunnerUserDetails (AccountService accountService, UserService userService, RoleService roleService, UserRepository userRepository, PrestationRepository prestationRepository, EtapePrestationServiceImpl etapePrestationServiceImpl) {
		return args -> {
			// Check if the admin user exists
			if (userRepository.findByUsername("admin.user") == null) {
				// Create the admin user
				UserDto user1 = new UserDto();
				user1.setName("user");
				user1.setSurname("admin");
				user1.setPassword("a");
				user1.setConfirmPassword("a");
				user1.setJobPosition("Developer");
				user1.setStatus(true);
				user1.setMail("pfe16072024@gmail.com");

				// Create the admin role
				RoleDto role = new RoleDto();
				role.setRoleName("ADMIN");

				// Persist user and role
				accountService.addNewUser(user1);
				roleService.createRole(role);

				// Assign role to the user
				UserDto user2 = userService.getUserByUsername(user1.getSurname() + "." + user1.getName());
				accountService.addRoleToUser(user2, 1L); // Assuming 1L is the role ID for ADMIN
				System.out.println("Admin user and role created successfully!");
			} else {
				System.out.println("Admin user already exists.");
			}

			// Create Prestation
			if (prestationRepository.findByName("Création d'entreprise").isEmpty()) {
				Prestation prestation = new Prestation();
				prestation.setName("Création d'entreprise");
				prestation=prestationRepository.save(prestation);
				System.out.println("Prestation created successfully!");


				// Create the list to hold EtapePrestationDTO objects
				List<EtapePrestationDTO> list = new ArrayList<>();

				// Dénomination
				EtapePrestationDTO denomination = new EtapePrestationDTO();
				denomination.setId(null); // Assuming ID is set later
				denomination.setName("Dénomination");
				denomination.setPrestationId(prestation.getId());
				denomination.setStepOrder(1);
				denomination.setColor("#34d399");

				Map<String, Object> denominationAttributes = new HashMap<>();
				Map<String, Object> attribute1 = new HashMap<>();
				attribute1.put("type", "text");
				attribute1.put("value", "");
				attribute1.put("order", 1);
				denominationAttributes.put("PREMIÈRE PROPOSITION", attribute1);

				Map<String, Object> attribute2 = new HashMap<>();
				attribute2.put("type", "text");
				attribute2.put("value", "");
				attribute2.put("order", 2);
				denominationAttributes.put("DEUXIÈME PROPOSITION", attribute2);

				Map<String, Object> attribute3 = new HashMap<>();
				attribute3.put("type", "text");
				attribute3.put("value", "");
				attribute3.put("order", 3);
				denominationAttributes.put("TROISIÈME PROPOSITION", attribute3);

				Map<String, Object> attribute4 = new HashMap<>();
				attribute4.put("type", "text");
				attribute4.put("value", "");
				attribute4.put("order", 4);
				denominationAttributes.put("QUATRIÈME PROPOSITION", attribute4);

				Map<String, Object> attribute5 = new HashMap<>();
				attribute5.put("type", "text");
				attribute5.put("value", "");
				attribute5.put("order", 5);
				denominationAttributes.put("CINQUIÈME PROPOSITION", attribute5);

				denomination.setAttributesTemplate(denominationAttributes);
				list.add(denomination);

				// Documentation
				EtapePrestationDTO documentation = new EtapePrestationDTO();
				documentation.setId(null); // Assuming ID is set later
				documentation.setName("Documentation");
				documentation.setPrestationId(prestation.getId());
				documentation.setStepOrder(2);
				documentation.setColor("#60a5fa");

				Map<String, Object> documentationAttributes = new HashMap<>();
				documentationAttributes.put("Ouverture d’un compte bancaire professionnel provisoire", createAttribute("checkbox", false,1));
				documentationAttributes.put("Rédaction des statuts de la société", createAttribute("checkbox", false,2));
				documentationAttributes.put("Envoie au notaire", createAttribute("checkbox", false,3));
				documentationAttributes.put("RDV avec le notaire", createAttribute("datetime", "",4));
				documentation.setAttributesTemplate(documentationAttributes);
				list.add(documentation);

				// Enregistrement
				EtapePrestationDTO enregistrement = new EtapePrestationDTO();
				enregistrement.setId(null); // Assuming ID is set later
				enregistrement.setName("Enregistrement");
				enregistrement.setPrestationId(prestation.getId());
				enregistrement.setStepOrder(3);
				enregistrement.setColor("#fbbf24");

				Map<String, Object> enregistrementAttributes = new HashMap<>();
				enregistrementAttributes.put("Tenue comptable", createAttribute("radio", "",1));
				enregistrementAttributes.put("Frais notaire inclut", createAttribute("checkbox", "",2));
				enregistrementAttributes.put("Etablir la facture", createAttribute("checkbox", "",3));
				enregistrementAttributes.put("Facturé", createAttribute("radio", "",4));
				enregistrement.setAttributesTemplate(enregistrementAttributes);
				list.add(enregistrement);

				for (EtapePrestationDTO etapePrestationDTO : list) {
					etapePrestationServiceImpl.createEtapePrestation(etapePrestationDTO);
				}
			} else {
				System.out.println("Prestation already exists.");
			}
		};
	}

	private static Map<String, Object> createAttribute(String type, Object value,int order) {
		Map<String, Object> attribute = new HashMap<>();
		attribute.put("type", type);
		attribute.put("value", value);
		attribute.put("order", order);
		return attribute;
	}
//	@Bean
	CommandLineRunner commandLineRunnerUserDetail (AccountService accountService, PasswordEncoder passwordEncoder){
		UserDto user1 = new UserDto();
		user1.setId(1);
		user1.setName("John");
		user1.setSurname("Doe");
		user1.setUsername("user3");
		user1.setPassword("123");
		user1.setConfirmPassword("123");
		user1.setJobPosition("Developer");
		user1.setMail("johndoe@example.com");


		UserDto user2 = new UserDto();
		user2.setId(2);
		user2.setName("Jane");
		user2.setSurname("Smith");
		user2.setUsername("user2");
		user2.setPassword("1234");
		user2.setConfirmPassword("1234");
		user2.setJobPosition("Manager");
		user2.setMail("janesmith@example.com");


		UserDto user3 = new UserDto();
		user3.setId(3);
		user3.setName("Alice");
		user3.setSurname("Johnson");
		user3.setUsername("user1");
		user3.setPassword("1234");
		user3.setConfirmPassword("1234");
		user3.setJobPosition("Tester");
		user3.setMail("alicejohnson@example.com");


		return args -> {
			accountService.addNewRole("USER");
			accountService.addNewRole("ADMIN");
			UserDto userDto1=accountService.addNewUser(user1);
			UserDto userDto2=accountService.addNewUser(user2);
			UserDto userDto3=accountService.addNewUser(user3);
		};
	}

}
