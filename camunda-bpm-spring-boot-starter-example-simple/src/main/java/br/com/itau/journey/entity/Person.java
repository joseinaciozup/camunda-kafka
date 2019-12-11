package br.com.itau.journey.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Person {

  @NotEmpty(message = "must not be null")
  @Column(nullable = false)
  private String name;

  @NotEmpty(message = "must not be null")
  @Column(nullable = false)
  private String cpf;

  @NotNull(message = "must not be null")
  @Column(name = "born_date")
  private LocalDate bornDate;

  @NotEmpty(message = "must not be null")
  @Column
  private String address;
}
