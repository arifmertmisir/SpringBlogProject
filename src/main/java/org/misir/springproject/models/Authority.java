package org.misir.springproject.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Authority {
    @Id
    private Long id;

    @Column(name = "NAME")
    private String name;
}
