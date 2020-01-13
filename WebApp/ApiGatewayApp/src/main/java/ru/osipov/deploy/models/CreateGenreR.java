package ru.osipov.deploy.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateGenreR {
	@NotEmpty
	@NotBlank//on null returns true but notEmpty returns false
	@Size(min = 6)
	@Pattern(regexp = "^[A-Z][a-z_]+$")
	private String gname;


	@Pattern(regexp =  "^$|^[A-Za-z,\\s]+[.]$")
    private String remarks;
}
