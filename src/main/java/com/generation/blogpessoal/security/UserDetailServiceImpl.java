package com.generation.blogpessoal.security;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private static final long serialVersionUID = 1L;

    @Autowired
    private UsuarioRepository repo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Usuario> usuario = repo.findByUsuario(userName);

        if(usuario.isPresent()){
            return new UserDatailsImpl(usuario.get());
        }else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
