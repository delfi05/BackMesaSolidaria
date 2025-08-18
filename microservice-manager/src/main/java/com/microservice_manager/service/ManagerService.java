package com.microservice_manager.service;

import com.microservice_manager.Security.jwt.TokenProvider;
import com.microservice_manager.dto.ManagerDTO;
import com.microservice_manager.entity.Authority;
import com.microservice_manager.entity.BlacklistToken;
import com.microservice_manager.entity.Manager;
import com.microservice_manager.exceptions.ManagerValidator;
import com.microservice_manager.feignClients.NewsFeignClients;
import com.microservice_manager.feignClients.ProjectFeignClients;
import com.microservice_manager.feignClients.VoluntaryFeignClients;
import com.microservice_manager.feignClients.model.NewsDTO;
import com.microservice_manager.feignClients.model.ProjectDTO;
import com.microservice_manager.feignClients.model.VoluntaryDTO;
import com.microservice_manager.repository.BlacklistTokenRepository;
import com.microservice_manager.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ManagerService {

  @Autowired
  private ManagerRepository managerRepository;
  @Autowired
  private BlacklistTokenRepository blacklistTokenRepository;
  @Autowired
  private ManagerValidator managerValidator;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private AuthenticationManagerBuilder authenticationManagerBuilder;
  @Autowired
  private TokenProvider tokenProvider;
  @Autowired
  private EmailService emailService;

  @Autowired
  private NewsFeignClients newsFeignClients;
  @Autowired
  private ProjectFeignClients projectFeignClients;
  @Autowired
  private VoluntaryFeignClients voluntaryFeignClients;

  @Transactional(readOnly = true)
  public List<ManagerDTO> findAll() throws Exception {
    try {
      List<Manager> managers = managerRepository.findAll();
      List<ManagerDTO> managersDTO = new ArrayList<>();
      for (Manager m : managers) {
        ManagerDTO managerDTO = new ManagerDTO(m.getName(), m.getLastName(), m.getEmail());
        managerDTO.setId_manager(m.getId_manager());
        managersDTO.add(managerDTO);
      }
      return managersDTO;
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public ManagerDTO findById(Long id) throws Exception {
    managerValidator.findById(id);
    try {
      Manager manager = managerRepository.findById(id).get();
      return new ManagerDTO(manager.getName(), manager.getLastName(), manager.getEmail());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public ManagerDTO findByEmail(String email) throws Exception {
    managerValidator.validateEmail(email);
    managerValidator.findByEmail(email);
    try {
      Optional<Manager> optionalManager = managerRepository.findByEmail(email);
      Manager manager = optionalManager.get();
      return new ManagerDTO(manager.getId_manager(), manager.getName(), manager.getLastName(), manager.getEmail());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public ManagerDTO saveManager(ManagerDTO managerDTO) throws Exception {
    managerValidator.saveManager(managerDTO);
    try {
      Manager manager = mapToManager(managerDTO, null);
      manager.setAuthority(new Authority("MANAGER"));
      managerRepository.save(manager);
      return new ManagerDTO(manager.getName(), manager.getLastName(), manager.getEmail());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public String loginManager(ManagerDTO managerDTO) throws Exception {
    managerValidator.findByEmail(managerDTO.getEmail());
    managerValidator.validateEmail(managerDTO.getEmail());
    try {
      cleanExpiredTokens();

      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          managerDTO.getEmail(),
          managerDTO.getPassword()
      );

      Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      // Obtener el email del manager autenticado
      String email = authentication.getName();

      // Obtener tu entidad Manager personalizada de la base de datos
      ManagerDTO manager = this.findByEmail(email);
      // Obtener el id_manager del usuario
      Long id_manager = manager.getId_manager();
      String name = manager.getName();
      String lastName = manager.getLastName();

      return tokenProvider.createToken(authentication, id_manager, name, lastName);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public Boolean validateToken(String authorizationHeader) {
    String token = authorizationHeader.substring(7);
    return tokenProvider.validateToken(token);
  }

  public ManagerDTO updateManager(Long id_manager, ManagerDTO managerDTO) throws Exception {
    managerValidator.updateManager(id_manager, managerDTO);
    try {
      Manager manager = mapToManager(managerDTO, id_manager);
      manager.setId_manager(id_manager);
      manager.setAuthority(new Authority("MANAGER"));
      managerRepository.updateManager(id_manager, manager.getName(), manager.getLastName(), manager.getEmail(), manager.getPassword());
      return new ManagerDTO(manager.getName(), manager.getLastName(), manager.getEmail());
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public String forgotPassword(String email) throws Exception {
    managerValidator.findByEmail(email);
    // Generar contraseña aleatoria
    String newPassword = PasswordGenerator.generateRandomPassword();
    // Cifrar la nueva contraseña
    String encodedPassword = passwordEncoder.encode(newPassword);
    managerRepository.updateManagerPassword(email, encodedPassword);

    // Enviar correo electrónico con la nueva contraseña
    emailService.sendEmail(email, newPassword);

    return "Contraseña restablecida con éxito";
  }

  public ManagerDTO deleteManager(Long id_manager) throws Exception {
    managerValidator.deleteManager(id_manager);
    try {
      if (managerRepository.existsById(id_manager)) {
        Manager manager = managerRepository.findById(id_manager).get();
        managerRepository.deleteById(id_manager);

        return new ManagerDTO(manager.getName(), manager.getLastName(), manager.getEmail());
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
    return null;
  }

  private Manager mapToManager(ManagerDTO managerDTO, Long id_manager) throws Exception {
    Manager manager = new Manager();
    manager.setName(managerDTO.getName().toLowerCase());
    manager.setLastName(managerDTO.getLastName().toLowerCase());
    manager.setEmail(managerDTO.getEmail().toLowerCase());
    if (id_manager != null) {
      Manager existingManager = managerRepository.findById(id_manager)
          .orElseThrow(() -> new Exception("Manager no encontrado"));
      if (managerDTO.getPassword() != null &&
          !passwordEncoder.matches(managerDTO.getPassword(), existingManager.getPassword())) {
        manager.setPassword(passwordEncoder.encode(managerDTO.getPassword()));
      } else {
        manager.setPassword(existingManager.getPassword());
      }
    } else
      manager.setPassword(passwordEncoder.encode(managerDTO.getPassword())); // Siempre rehashear
    return manager;
  }

  public String logoutManager(String token) throws Exception {
    String cleanToken = token.replace("Bearer ", "");
    if (tokenProvider.validateToken(cleanToken)) {
      blacklistTokenRepository.save(new BlacklistToken(cleanToken));
      return "Logout exitoso";
    } else {
      throw new Exception("Token inválido durante el logout: " + cleanToken);
    }
  }

  public boolean isTokenBlacklisted(String token) {
    return blacklistTokenRepository.existsById(token);
  }

  private void cleanExpiredTokens() {
    LocalDateTime expirationTime = LocalDateTime.now().minusDays(1); // Eliminar tokens de hace 1 día o más
    blacklistTokenRepository.deleteExpiredTokens(expirationTime);
  }

  public List<NewsDTO> getAllNews() throws Exception {
    try {
      return newsFeignClients.findAll();
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public NewsDTO findNewsById(Long id_news) throws Exception {
    try {
      return newsFeignClients.findById(id_news);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public NewsDTO saveNews(NewsDTO newsDTO) throws Exception {
    try {
      return newsFeignClients.saveNews(newsDTO);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public NewsDTO updateNews(Long id_news, NewsDTO newsDTO) throws Exception {
    try {
      return newsFeignClients.updateNews(id_news, newsDTO);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public NewsDTO deleteNews(Long id_news) throws Exception {
    try {
      return newsFeignClients.deleteNews(id_news);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public List<ProjectDTO> findAllProjects() throws Exception {
    try {
      return projectFeignClients.getAll();
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public ProjectDTO findProjectsById(Long id_project) throws Exception {
    try {
      return projectFeignClients.getProjectByID(id_project);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public ProjectDTO findAvailableProjectsById(Long id_project) throws Exception {
    try {
      return projectFeignClients.getAvailableProjectByID(id_project);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public ProjectDTO saveProject(ProjectDTO projectDTO) throws Exception {
    try {
      return projectFeignClients.saveProject(projectDTO);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public ProjectDTO updateProject(Long id_project, ProjectDTO projectDTO) throws Exception {
    try {
      return projectFeignClients.updateProject(id_project, projectDTO);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public ProjectDTO deleteProject(Long id_project) throws Exception {
    try {
      return projectFeignClients.deleteProject(id_project);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public List<VoluntaryDTO> findAllVoluntaries() throws Exception {
    try {
      return voluntaryFeignClients.getAll();
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public VoluntaryDTO findVoluntaryById(Long id_voluntary) throws Exception {
    try {
      return voluntaryFeignClients.getVoluntaryByID(id_voluntary);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public VoluntaryDTO findVoluntaryByEmail(String email) throws Exception {
    try {
      return voluntaryFeignClients.getVoluntaryByEmail(email);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public VoluntaryDTO saveVoluntary(VoluntaryDTO voluntaryDTO) throws Exception {
    try {
      return voluntaryFeignClients.saveVoluntary(voluntaryDTO);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public VoluntaryDTO updateVoluntary(Long id_voluntary, VoluntaryDTO voluntaryDTO) throws Exception {
    try {
      return voluntaryFeignClients.updateVoluntary(id_voluntary, voluntaryDTO);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public VoluntaryDTO deleteVoluntary(Long id_voluntary) throws Exception {
    try {
      return voluntaryFeignClients.deleteVoluntary(id_voluntary);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }
}