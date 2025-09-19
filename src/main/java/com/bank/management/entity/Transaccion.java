package com.bank.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor que 0")
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal monto;

    @NotBlank(message = "El tipo de transaccion es obligatorio")
    @Pattern(regexp = "DEPOSITO|RETIRO", message = "El tipo debe ser DEPOSITO o RETIRO")
    @Column(nullable = false, length = 10)
    private String tipo;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private CuentaBancaria cuenta;
}
