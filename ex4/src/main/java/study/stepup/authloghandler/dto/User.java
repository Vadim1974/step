package study.stepup.authloghandler.dto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fio;
    private String login;
}
