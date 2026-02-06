package org.misir.springproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Email(message = "Invalid email!")
    @NotEmpty(message = "Email missing!")
    @Column(name = "EMAIL")
    private String email;

    @NotEmpty(message = "Password missing!")
    @Column(name = "PASSWORD")
    private String password;

    @NotEmpty(message = "First name missing!")
    @Column(name = "FIRST_NAME")
    private String firstName;

    @NotEmpty(message = "Last name missing!")
    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "GENDER")
    private String gender;

    @Min(value = 18)
    @Max(value = 99)
    @Column(name = "AGE")
    private Integer age;

    @Column(name = "BIRTH_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String photo;

    @Column(name = "ROLE")
    private String role;

    @OneToMany(mappedBy = "account")
    private List<Post> posts;

    @ManyToMany
    @JoinTable(
            name = "ACCOUNT_AUTHORITY",
            joinColumns = {@JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
    private Set<Authority> authorities = new HashSet<>();
}