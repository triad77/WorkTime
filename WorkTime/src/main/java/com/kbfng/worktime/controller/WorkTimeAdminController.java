package com.kbfng.worktime.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kbfng.worktime.dto.EmpCreateForm;
import com.kbfng.worktime.dto.EmployeeDto;
import com.kbfng.worktime.dto.EmployeeWorkTimeDto;
import com.kbfng.worktime.dto.ProjectDto;
import com.kbfng.worktime.dto.WorkTimeDto;
import com.kbfng.worktime.dto.WorkTimeForCorpDto;
import com.kbfng.worktime.dto.WorkTimeForProjectDto;
import com.kbfng.worktime.entity.CorpEnum;
import com.kbfng.worktime.entity.StatEnum;
import com.kbfng.worktime.service.WorkTimeAdminService;
import com.kbfng.worktime.util.EmployeeMapper;
import com.kbfng.worktime.util.ProjectMapper;
import com.kbfng.worktime.util.WorkTimeUtils;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class WorkTimeAdminController {

	private final WorkTimeAdminService adminService;

	@GetMapping("")
	public String admin() {
		return "redirect:/admin/search/byProject";
	}

	@GetMapping("/search/byProject")
	public String searchByProject(Model model, 
			@RequestParam(value = "plodrCorp", defaultValue = "00") String plodrCorp, 
			String srchYmd,
			@RequestParam(value = "project", defaultValue = "0") int prjNo,
			@RequestParam(value = "page", defaultValue = "0") int page) {
		
		// 발주회사 콤보
		model.addAttribute("pCorpSelBox", WorkTimeUtils.getListForPlodrCorp(CorpEnum.class));
		model.addAttribute("pCorpSel", plodrCorp);
		
		// 프로젝트 콤보
		if(plodrCorp != null && !plodrCorp.equals("00")) {
			
			model.addAttribute("prjSelBox", adminService.getPrjList(plodrCorp));
			
			if(prjNo != 0) {
				model.addAttribute("prjSel", prjNo);
			}
		}
		
		if(srchYmd == null || srchYmd.equals("")) {
			srchYmd = getTodayStr();
		}
		model.addAttribute("srchYmd", srchYmd);
		
		Page<WorkTimeForProjectDto> paging = adminService.getWorkTimeListForProject(plodrCorp, prjNo, srchYmd, page);
		model.addAttribute("paging", paging);
		
		return "/admin/searchListByProject";
	}

	@GetMapping("/search/byCorp")
	public String searchByCorp(Model model, 
			@RequestParam(value = "belngCorp", defaultValue = "00") String belngCorp,
			String srchYmd,
			@RequestParam(value = "page", defaultValue = "0") int page) {
		
		// 소속회사 콤보
		model.addAttribute("bCorpSelBox", WorkTimeUtils.getListForBelngCorp(CorpEnum.class));
		model.addAttribute("bCorpSel", belngCorp);
		
		if(srchYmd == null || srchYmd.equals("")) {
			srchYmd = getTodayStr();
		}
		model.addAttribute("srchYmd", srchYmd);
		
		Page<WorkTimeForCorpDto> paging = adminService.getWorkTimeListForBelngCorp(belngCorp, srchYmd, page);
		model.addAttribute("paging", paging);
		
		return "/admin/searchListByCorp";
	}

	@GetMapping("/search/byEmp")
	public String searchByEmp(Model model, 
			String srchEmpNm, String srchStYmd, String srchEdYmd,
			@RequestParam(value = "page", defaultValue = "0") int page) {
		
		model.addAttribute("srchEmpNm", srchEmpNm);
		
		if(srchStYmd == null || srchStYmd.equals("")) {
			srchStYmd = getTodayStr();
		}
		model.addAttribute("srchStYmd", srchStYmd);
		
		if(srchEdYmd == null || srchEdYmd.equals("")) {
			srchEdYmd = getTodayStr();
		}
		model.addAttribute("srchEdYmd", srchEdYmd);
		
		Page<WorkTimeDto> paging = adminService.getWorkTimeList(srchEmpNm, srchStYmd, srchEdYmd, page);
		
		model.addAttribute("paging", paging);
		
		return "/admin/searchListByEmp";
	}

	@GetMapping("/manage/corp")
	public String manageCorp() {
		return "/admin/manageCorp";
	}

	@GetMapping("/manage/prj")
	public String managePrj(Model model, String plodrCorp, String statCd,
			@RequestParam(value = "page", defaultValue = "0") int page) {

		model.addAttribute("pCorpSelBox", WorkTimeUtils.getListForPlodrCorp(CorpEnum.class));
		model.addAttribute("pCorpSel", plodrCorp);

		model.addAttribute("statSelBox", WorkTimeUtils.getStatList(StatEnum.class));
		model.addAttribute("selectedStat", statCd);

		Page<ProjectDto> paging = adminService.getPrjList(plodrCorp, statCd, page);
		model.addAttribute("paging", paging);
		
		return "/admin/managePrj";
	}

	/**
	 * 시스템관리 > 직원관리 > 직원목록조회
	 * @param model
	 * @param empNo:String - 조회조건 직원번호
	 * @param empNm:String - 조회조건 직원명
	 * @param plodrCorp:String - 조회조건 프로젝트 발주회사
	 * @param prjId:int - 조회조건 프로젝트번호
	 * @param belngCorp:String - 조회조건 소속회사
	 * @param page:int - 조회 페이지
	 * @return
	 */
	@GetMapping("/manage/emp")
	public String manageEmpList(Model model, 
			String empNo, String empNm, String plodrCorp, 
			@RequestParam(value = "project", defaultValue = "0") int prjId, 
			String belngCorp,
			@RequestParam(value = "page", defaultValue = "0") int page) {

		model.addAttribute("searchedEmpNo", StringUtils.isBlank(empNo)?"":empNo);
		model.addAttribute("searchedEmpNm", StringUtils.isBlank(empNm)?"":empNm);
		
		// 발주회사 콤보(프로젝트 선택을 위한)
		model.addAttribute("pCorpSelBox", WorkTimeUtils.getListForPlodrCorp(CorpEnum.class));
		model.addAttribute("pCorpSel", plodrCorp);
		
		// 프로젝트 콤보
		if(plodrCorp != null && !plodrCorp.equals("00")) {
			
			model.addAttribute("prjSelBox", adminService.getPrjList(plodrCorp));
			
			if(prjId != 0) {
				model.addAttribute("prjSel", prjId);
			}
		}
		
		// 소속회사 콤보
		model.addAttribute("bCorpSelBox", WorkTimeUtils.getListForBelngCorp(CorpEnum.class));
		model.addAttribute("bCorpSel", belngCorp);
		
		Page<EmployeeDto> paging = adminService.getEmpList(
				empNo, empNm, plodrCorp, prjId, belngCorp, page);
		model.addAttribute("paging", paging);
		
		return "/admin/manageEmp";
	}
	
	/**
	 * 직원정보 상세조회
	 * @param model
	 * @param empNo
	 * @return
	 */
	@GetMapping("/manage/empDetail/{id}")
	public String manageEmpDtail(Model model, @PathVariable int id) {
		
		EmployeeDto empDto = adminService.getEmployee(id);
		model.addAttribute("emp", empDto);
		return "/admin/manageEmpDetail";
	}
	
	/**
	 * 직원등록 페이지 이동
	 * @param model
	 * @param form
	 * @return
	 */
    @GetMapping("/manage/empRegist")
    public String empRegist(Model model, EmpCreateForm form) {
    	
    	// 소속회사 콤보
    	model.addAttribute("bCorpSelBox", WorkTimeUtils.getListForBelngCorp(CorpEnum.class));
    	// 발주회사 콤보(프로젝트 선택을 위한)
    	model.addAttribute("pCorpSelBox", WorkTimeUtils.getListForPlodrCorp(CorpEnum.class));
    	
        return "/admin/manageEmpRegist";
    }
    
    /**
     * 직원등록 로직수행
     * @param model
     * @param form
     * @param bindingResult
     * @return
     */
    @PostMapping("/manage/empRegist")
    public String empRegist(Model model, @Valid EmpCreateForm form, BindingResult bindingResult) {
    	
    	String rtnUrl = "/admin/manageEmpRegist";
    	
    	if (bindingResult.hasErrors()) {
            return rtnUrl;
        }

        try {
        	
        	EmployeeDto dto = EmployeeDto.builder()
        			.empNo(form.getEmpNo())
        			.empNm(form.getEmpNm())
        			.ipAddr(form.getIpAddr())
        			.contract(form.getContract())
        			.corp(CorpEnum.find(form.getBelngCorp()))
        			.project(ProjectMapper.INSTANCE.toEntity(adminService.getProject(form.getPrjId())))
        			.ctrcStDate(form.getCtrcStDate().replaceAll("-", ""))
        			.ctrcEdDate("99991231")
        			.build();
        			
        	dto = adminService.createEmp(dto);
        	rtnUrl = "redirect:/admin/manage/empDetail/"+dto.getId();
        	
        }catch(IllegalArgumentException e) {
        	bindingResult.reject("activeEmpNoError", new Object[]{form.getEmpNo()}, null);
        }catch(DataIntegrityViolationException e) {
        	bindingResult.reject("DIV_Error", new Object[]{form.getEmpNo()}, null);
        }catch(Exception e) {
            bindingResult.reject("registFailed", null, e.getMessage());
        }finally {
        	if(bindingResult.getErrorCount() > 0) {
        		// 소속회사 콤보
            	model.addAttribute("bCorpSelBox", WorkTimeUtils.getListForBelngCorp(CorpEnum.class));
            	// 발주회사 콤보(프로젝트 선택을 위한)
            	model.addAttribute("pCorpSelBox", WorkTimeUtils.getListForPlodrCorp(CorpEnum.class));
        	}
		}
        
    	return rtnUrl;
    }
    
    /**
	 * 직원수정 페이지 이동
	 * @param model
	 * @param form
	 * @return
	 */
    @GetMapping("/manage/empUpdate/{id}")
    public String empUpdate(Model model, @PathVariable int id) {
    	
    	EmployeeDto empDto = adminService.getEmployee(id);
    	EmpCreateForm form = EmployeeMapper.INSTANCE.toForm(empDto);
    	
		model.addAttribute("form", form);
		model.addAttribute("bCorpSel", empDto.getCorp().getCode());
		model.addAttribute("pCorpSel", empDto.getProject().getPlodrCorp().getCode());
		model.addAttribute("prjSel", empDto.getProject().getPrjId());
		
    	// 소속회사 콤보
    	model.addAttribute("bCorpSelBox", WorkTimeUtils.getListForBelngCorp(CorpEnum.class));
    	// 발주회사 콤보(프로젝트 선택을 위한)
    	model.addAttribute("pCorpSelBox", WorkTimeUtils.getListForPlodrCorp(CorpEnum.class));
    	// 프로젝트 콤보
    	model.addAttribute("prjSelBox", adminService.getPrjList(model.getAttribute("pCorpSel").toString()));
    	
        return "/admin/manageEmpUpdate";
    }
    
    /**
     * 직원수정 로직수행
     * @param model
     * @param form
     * @param bindingResult
     * @return
     */
    @PostMapping("/manage/empUpdate")
    public String empUpdate(Model model, @Valid @ModelAttribute("form") EmpCreateForm form, BindingResult bindingResult) {
    	
    	String rtnUrl = "/admin/manageEmpUpdate";
    	
    	if (bindingResult.hasErrors()) {
            return rtnUrl;
        }
    	
    	log.debug("@@@ form id {}", form.getId());

        try {
        	
        	EmployeeDto dto = EmployeeDto.builder()
        			.id(form.getId())
        			.empNo(form.getEmpNo())
        			.empNm(form.getEmpNm())
        			.ipAddr(form.getIpAddr())
        			.contract(form.getContract())
        			.corp(CorpEnum.find(form.getBelngCorp()))
        			.project(ProjectMapper.INSTANCE.toEntity(adminService.getProject(form.getPrjId())))
        			.ctrcStDate(form.getCtrcStDate().replaceAll("-", ""))
        			.ctrcEdDate("99991231")
        			.build();
        			
        	adminService.updateEmp(dto);
        	rtnUrl = "redirect:/admin/manage/empDetail/"+form.getId();
        			
        }catch(IllegalArgumentException e) {
        	bindingResult.reject("activeEmpNoError", new Object[]{form.getEmpNo()}, null);
        }catch(DataIntegrityViolationException e) {
        	bindingResult.reject("DIV_Error", new Object[]{form.getEmpNo()}, null);
        }catch(Exception e) {
            bindingResult.reject("registFailed", null, e.getMessage());
        }finally {
        	if(bindingResult.getErrorCount() > 0) {
        		// 소속회사 콤보
            	//model.addAttribute("bCorpSelBox", getListForBelngCorp(CorpEnum.class));
            	// 발주회사 콤보(프로젝트 선택을 위한)
            	//model.addAttribute("pCorpSelBox", getListForPlodrCorp(CorpEnum.class));
        	}
		}
        
    	return rtnUrl;
    }
	
	@PutMapping("/manage/emp/endingCtrc/{id}/{ctrcEndYmd}")
	@ResponseBody
	public void endingCtrcEmp(@PathVariable int id, @PathVariable String ctrcEndYmd) {
		adminService.regCtrcEndYmd(id, ctrcEndYmd);
	}
	
	@GetMapping("/manage/empDelete/{id}")
    public String empDelete(@PathVariable int id) {
		
		adminService.deleteEmp(id);
		
		return "redirect:/admin/manage/emp";
	}

	@GetMapping(value = "/getProject/{plodrCorp}", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public void getProjectByPlodrCorp(HttpServletResponse res, @PathVariable String plodrCorp) throws IOException {

		List<ProjectDto> options = adminService.getPrjList(plodrCorp);

		JSONArray jsonArray = new JSONArray();
		options.forEach(v -> {
			jsonArray.put(makeJsonForCombo(v));
		});

		res.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		pw.print(jsonArray.toString());
		pw.flush();
		pw.close();
	}
	
	@PostMapping(value = "/getEmpWorkTimeList", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public void getEmpWorkTimeList(HttpServletResponse res, 
			@RequestParam String listGbn,
			@RequestParam String srchYmd,
			@RequestParam int prjId, 
			@RequestParam String corpCd) throws IOException {

		List<EmployeeWorkTimeDto> list = adminService.getEmpWorkTimeList(listGbn, srchYmd, prjId, corpCd);

		JSONObject jobj = new JSONObject();
		
		if("T".equals(listGbn)) {
			jobj.put("title", "전체직원명단");
		}
		else if("R".equals(listGbn)) {
			jobj.put("title", "등록직원명단");
		}
		else if("L".equals(listGbn)) {
			jobj.put("title", "지각자명단");
		}
		else {
			jobj.put("title", "미등록 직원명단");
		}
		
		JSONArray jsonArray = new JSONArray();
		list.forEach(v -> {
			jsonArray.put(makeJsonForEmpWorkTime(v));
		});
		
		jobj.put("list", jsonArray);
		
		res.setCharacterEncoding("UTF-8");
		PrintWriter pw = res.getWriter();
		pw.print(jobj.toString());
		pw.flush();
		pw.close();
	}

	private JSONObject makeJsonForCombo(ProjectDto dto) {
		JSONObject json = new JSONObject();
		json.put("code", dto.getPrjId());
		json.put("name", dto.getPrjNm());
		return json;
	}
	
	private JSONObject makeJsonForEmpWorkTime(EmployeeWorkTimeDto dto) {
		
		DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		JSONObject json = new JSONObject();
		json.put("prjNm", dto.getPrjNm());
		json.put("corpNm", dto.getCorp().getValue());
		json.put("empNo", dto.getEmpNo());
		json.put("empNm", dto.getEmpNm());
		json.put("regTime", dto.getRegTime() == null ? "" : dto.getRegTime().format(formatDateTime));
		json.put("reason", dto.getReason() == null ? "" : dto.getReason());
		return json;
	}

	private String getTodayStr() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date();
		String todayStr = format.format(now);
		
		return todayStr;
	}
}
