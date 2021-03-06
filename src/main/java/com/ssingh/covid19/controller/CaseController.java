package com.ssingh.covid19.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ssingh.covid19.annotation.ApiRestEndpoint;
import com.ssingh.covid19.annotation.ValidStateCode;
import com.ssingh.covid19.dto.CaseDTO;
import com.ssingh.covid19.service.CaseService;

/**
 * API Controller for managing Covid Cases.
 * 
 * @author Saurabh Singh
 */
@ApiRestEndpoint("/case")
@Validated
public class CaseController {
	private CaseService caseService;

	@Autowired
	public CaseController(CaseService caseService) {
		this.caseService = caseService;
	}
	
	@PostMapping(value = { "/add", "/add/" }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Void> updateStateCase(
			@Valid @RequestBody CaseDTO caseDto) {
		boolean status = caseService.addNewStateUpdate(caseDto);
		if (status)
			return ResponseEntity.ok().build();
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.build();
	}

	@GetMapping(value = { "/{code}", "/{code}/" })
	public ResponseEntity<CaseDTO> fetchStateCase(
			@ValidStateCode @PathVariable("code") String code) {
		CaseDTO caseDto = caseService.fetchStateCase(code);
		ResponseEntity<CaseDTO> response = ResponseEntity.ok(caseDto);
		return response;
	}
}
