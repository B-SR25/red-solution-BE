package com.elmaguiri.backend.security;

import com.elmaguiri.backend.serviceImp.UserDetailServiceImpl;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secretKey;

   // @Bean
    public InMemoryUserDetailsManager  inMemoryUserDetailsManager(){
        PasswordEncoder passwordEncoder=passwordEncoder();
        return new InMemoryUserDetailsManager(
                User.withUsername("ADMIN").password(passwordEncoder.encode("ADMIN")).authorities("USER","ADMIN").build(),
                User.withUsername("USER").password(passwordEncoder.encode("USER")).authorities("USER").build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity

                .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//veut dire on va pas gerer l'authentication cote serveur (JWT)
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(ar->ar.requestMatchers("/auth/login/**","/ws/**").permitAll())
                .authorizeHttpRequests(ar->ar.requestMatchers("/actuator/**").permitAll())
                .authorizeHttpRequests(ar->ar.requestMatchers("/operations/search/**").permitAll())
                .authorizeHttpRequests(ar->ar.anyRequest().authenticated())
                .oauth2ResourceServer(oa->oa.jwt(Customizer.withDefaults()))
                .build();
    }



    @Bean
    JwtEncoder jwtEncoder(){
        //String secretKey="azrhlgrojhgbhshfurgkhgbnkhgtoLohgbOU7845vgtjbpejtGRYkehtei785MLo";
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
    }

    @Bean
    JwtDecoder jwtDecoder(){
        //String secretKey="azrhlgrojhgbhshfurgkhgbnkhgtoLohgbOU7845vgtjbpejtGRYkehtei785MLo";
        SecretKeySpec secretKeySpec=new SecretKeySpec(secretKey.getBytes(),"RSA");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailServiceImpl userDetailsService){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(daoAuthenticationProvider);
    }

}
