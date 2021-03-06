/*Copyright (c) 2015-2016 gmail.com All Rights Reserved.
 This software is the confidential and proprietary information of gmail.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with gmail.com*/

package com.hrdb.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/


import com.hrdb.service.EmployeeService;
import com.hrdb.service.VacationService;


import java.io.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.TypeMismatchException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.util.WMMultipartUtils;
import com.wavemaker.runtime.util.WMRuntimeUtils;
import com.wavemaker.runtime.file.model.DownloadResponse;
import com.wordnik.swagger.annotations.*;

import com.hrdb.*;
import com.hrdb.service.*;


/**
 * Controller object for domain model class Employee.
 * @see com.hrdb.Employee
 */

@RestController(value = "Hrdb.EmployeeController")
@Api(value = "/hrdb/Employee", description = "Exposes APIs to work with Employee resource.")
@RequestMapping("/hrdb/Employee")
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	@Qualifier("hrdb.EmployeeService")
	private EmployeeService employeeService;


    @Autowired
    @Qualifier("hrdb.VacationService")
    private VacationService vacationService;


	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Returns the list of Employee instances matching the search criteria.")
	public Page<Employee> findEmployees( Pageable pageable, @RequestBody QueryFilter[] queryFilters) {
		LOGGER.debug("Rendering Employees list");
		return employeeService.findAll(queryFilters, pageable);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ApiOperation(value = "Returns the list of Employee instances.")
	public Page<Employee> getEmployees(Pageable pageable) {
		LOGGER.debug("Rendering Employees list");
		return employeeService.findAll(pageable);
	}

	@RequestMapping(value = "/count", method = RequestMethod.GET)
	@ApiOperation(value = "Returns the total count of Employee instances.")
	public Long countAllEmployees() {
		LOGGER.debug("counting Employees");
		Long count = employeeService.countAll();
		return count;
	}


    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @ApiOperation(value = "Returns the Employee instance associated with the given id.")
    public Employee getEmployee(@PathVariable("id") Integer id) throws EntityNotFoundException {
        LOGGER.debug("Getting Employee with id: {}" , id);
        Employee instance = employeeService.findById(id);
        LOGGER.debug("Employee details with id: {}" , instance);
        return instance;
    }
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Deletes the Employee instance associated with the given id.")
    public boolean deleteEmployee(@PathVariable("id") Integer id) throws EntityNotFoundException {
        LOGGER.debug("Deleting Employee with id: {}" , id);
        Employee deleted = employeeService.delete(id);
        return deleted != null;
    }

    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PUT)
    @ApiOperation(value = "Updates the Employee instance associated with the given id.")
    public Employee editEmployee(@PathVariable("id") Integer id, @RequestBody Employee instance) throws EntityNotFoundException {
        LOGGER.debug("Editing Employee with id: {}" , instance.getEid());
        instance.setEid(id);
        instance = employeeService.update(instance);
        LOGGER.debug("Employee details with id: {}" , instance);
        return instance;
    }


    @RequestMapping(value="/{id:.+}/vacations", method=RequestMethod.GET)
    @ApiOperation(value = "Gets the vacations instance associated with the given id.")
     public Page<Vacation> findAssociatedvacations(Pageable pageable, @PathVariable("id") Integer id){
        LOGGER.debug("Fetching all associated vacations");
        return vacationService.findAssociatedValues(id,"employee","eid", pageable);
   }

    @RequestMapping(value="/{id:.+}/employeesForManagerid", method=RequestMethod.GET)
    @ApiOperation(value = "Gets the employeesForManagerid instance associated with the given id.")
     public Page<Employee> findAssociatedemployeesForManagerid(Pageable pageable, @PathVariable("id") Integer id){
        LOGGER.debug("Fetching all associated employeesForManagerid");
        return employeeService.findAssociatedValues(id,"employeeByManagerid","eid", pageable);
   }



	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ApiOperation(value = "Creates a new Employee instance.")
	public Employee createEmployee(@RequestBody Employee instance) {
		LOGGER.debug("Create Employee with information: {}" , instance);
		instance = employeeService.create(instance);
		LOGGER.debug("Created Employee with information: {}" , instance);
	    return instance;
	}

	/**
	 * This setter method should only be used by unit tests
	 * 
	 * @param service
	 */
	protected void setEmployeeService(EmployeeService service) {
		this.employeeService = service;
	}
}

