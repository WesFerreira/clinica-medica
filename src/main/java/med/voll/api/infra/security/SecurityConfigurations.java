package med.voll.api.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//anotação para indicar que vamos personalizar configurações de segurança
@EnableWebSecurity
public class SecurityConfigurations {

    //método para devolver para o Spring, anotação para o Spring conseguir ler o método
    @Bean
    //Objeto usado para configurar coisas relacionadas com o processo de autenticação e autorização
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        // desabilitado porq o token que vamos usar já faz esse tratamento
        return http.csrf().disable()
                //método para configurar para ser Stateless
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //como vai ser as autorizações das requisições
                .and().authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated()
                .and().build();
    }

    //método para ensinar o Spring como que injeta objetos.
    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}