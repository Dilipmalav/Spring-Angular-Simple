package com.rays.ctl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rays.common.ORSResponse;
import com.rays.dto.DepartmentDTO;
import com.rays.form.DepartmentForm;
import com.rays.service.DepartmentService;

@RestController
@RequestMapping(value = "Department")
public class DepartmentCtl {

	@Autowired
	public DepartmentService departmentService;

	@PostMapping("save")
	public ORSResponse save(@RequestBody @Valid DepartmentForm form, BindingResult bindingResult) {

	    ORSResponse res = validate(bindingResult);

	    if (!res.isSuccess()) {
	        return res;
	    }

	    DepartmentDTO dto = new DepartmentDTO();
	    
	    dto.setId(form.getId());
	    dto.setName(form.getName());
	    dto.setLocation(form.getLocation());
	    dto.setHeadOfDepartment(form.getHeadOfDepartment());

	    if (dto.getId() != null && dto.getId() > 0) {
	        departmentService.update(dto);
	        res.addData(dto.getId());
	        res.addMessage("Data Updated Successfully..!!");
	    } else {
	    	
	        long pk = departmentService.add(dto);
	        res.addData(pk);
	        res.addMessage("Data added Successfully..!!");
	    }

	    return res;
	}


	@GetMapping("delete/{ids}")
	public ORSResponse delete(@PathVariable long[] ids) {

		ORSResponse res = new ORSResponse();

		for (long id : ids) {
			departmentService.delete(id);
		}

		res.addMessage("data deleted successfully");

		return res;
	}

	@PostMapping("search/{pageNo}")
	public ORSResponse search(@RequestBody DepartmentForm form, @PathVariable int pageNo) {
	    ORSResponse res = new ORSResponse();

	    DepartmentDTO dto = new DepartmentDTO();

	    // 🔥 Ye 3 lines add kar filtering ke liye
	    dto.setName(form.getName());
	    dto.setLocation(form.getLocation());
	    dto.setHeadOfDepartment(form.getHeadOfDepartment());

	    List list = departmentService.search(dto, pageNo, 5);

	    if (list.size() == 0) {
	        res.addMessage("Result not found...!!!");
	    } else {
	        res.addData(list);
	    }

	    return res;
	}


	public ORSResponse validate(BindingResult bindingResult) {

		ORSResponse res = new ORSResponse(true);

		if (bindingResult.hasErrors()) {

			res.setSuccess(false);

			Map<String, String> errors = new HashMap<String, String>();

			List<FieldError> list = bindingResult.getFieldErrors();

			list.forEach(e -> {
				errors.put(e.getField(), e.getDefaultMessage());
			});
			res.addInputError(errors);
		}
		return res;
	}
}
