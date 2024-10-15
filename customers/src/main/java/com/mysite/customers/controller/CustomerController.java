package com.mysite.customers.controller;

import com.mysite.customers.dto.CustomerDataDto;
import com.mysite.customers.dto.CustomerDto;
import com.mysite.customers.dto.validation.ValidationErrorsDto;
import com.mysite.customers.exception.NotFoundException;
import com.mysite.customers.exception.ValidationException;
import com.mysite.customers.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {
    private final ICustomerService customerService;

    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(
            summary = "Get a specific customer.",
            description = "Tries to find a customer by the given ID value."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer was found.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CustomerDto.class)) }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer was not found.",
                    content = { @Content(mediaType = MediaType.TEXT_PLAIN_VALUE) })
    })
    @GetMapping("/v1/customers/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto getCustomer(@PathVariable long id) throws NotFoundException {
        return customerService.findById(id);
    }

    @Operation(
            summary = "Create a new customer.",
            description = "Tries to create a new customer with the given data."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "New customer was created.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CustomerDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Customer data did not pass validation rules.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ValidationErrorsDto.class)) })
    })
    @PostMapping("/v1/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto createCustomer(@RequestBody CustomerDataDto newCustomerDataDto) throws ValidationException {
        return customerService.create(newCustomerDataDto);
    }

    @Operation(
            summary = "Update an existing customer.",
            description = "Tries to update an existing customer with the given data."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Existing customer was updated.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CustomerDto.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Customer data did not pass validation rules.",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ValidationErrorsDto.class)) }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer was not found.",
                    content = { @Content(mediaType = MediaType.TEXT_PLAIN_VALUE) })
    })
    @PutMapping("/v1/customers/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto updateCustomer(@PathVariable long id, @RequestBody CustomerDataDto updatedCustomerDataDto) throws ValidationException, NotFoundException {
        return customerService.update(id, updatedCustomerDataDto);
    }

    @Operation(
            summary = "Delete a specific customer.",
            description = "Tries to delete a customer by the given ID value."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Customer was deleted.",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer was not found.",
                    content = { @Content(mediaType = MediaType.TEXT_PLAIN_VALUE) })
    })
    @DeleteMapping("/v1/customers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable long id) throws NotFoundException {
        customerService.deleteById(id);
    }

}
