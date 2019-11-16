package ru.osipov.deploy.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateGenreR {
	@NotEmpty
	@NotBlank//on null returns true but notEmpty returns false
	private String gname;


    private String remarks;
}
