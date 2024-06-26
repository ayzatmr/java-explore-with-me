package ru.practicum.admin.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserDto {
    @NotBlank
    @Size(min = 2, max = 250)
    String name;

    @Email
    @NotBlank
    @Size(min = 6, max = 254)
    String email;
}

