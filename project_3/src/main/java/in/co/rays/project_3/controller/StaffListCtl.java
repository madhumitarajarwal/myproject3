package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.StaffDTO;
import in.co.rays.project_3.dto.UserDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.StaffModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * Staff List functionality controller to perform Search and List operations.
 */
@WebServlet(name = "StaffListCtl", urlPatterns = { "/ctl/StaffListCtl" })
public class StaffListCtl extends BaseCtl {

    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(StaffListCtl.class);

    protected void preload(HttpServletRequest request) {
        StaffModelInt umodel = ModelFactory.getInstance().getStaffModel();
        Map<Integer, String> map = new HashMap();

		map.put(1, "Management");
		map.put(2, "Human Resource");
		map.put(3, "Lead");
		
		request.setAttribute("staff", map);
    }

    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {
        StaffDTO dto = new StaffDTO();

        dto.setFullName(DataUtility.getString(request.getParameter("fullName")));
        dto.setPreviousEmployer(DataUtility.getString(request.getParameter("previousEmployer")));
        dto.setDivision(DataUtility.getInt(request.getParameter("division")));

        // Fixing date parsing issue
        String dateStr = request.getParameter("date");
        if (dateStr != null && !dateStr.isEmpty()) {
            dto.setJoiningDate(DataUtility.getDate(dateStr));
        }

        populateBean(dto, request);

        log.debug("StaffListCtl Method populateDTO Ended");
        return dto;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("StaffListCtl doGet Start");

        List<StaffDTO> list = null;
        List<StaffDTO> next = null;
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        StaffDTO dto = (StaffDTO) populateDTO(request);

        StaffModelInt model = ModelFactory.getInstance().getStaffModel();
        try {
            list = model.search(dto, pageNo, pageSize);
            next = model.search(dto, pageNo + 1, pageSize);

            ServletUtility.setList(list, request);
            if (list == null || list.isEmpty()) {
                ServletUtility.setErrorMessage("No record found ", request);
            }
            request.setAttribute("nextListSize", next == null ? 0 : next.size());

            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);
        } catch (ApplicationException e) {
            log.error(e);
            ServletUtility.handleException(e, request, response);
        }
        log.debug("StaffListCtl doGet End");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("StaffListCtl doPost Start");

        List<StaffDTO> list = null;
        List<StaffDTO> next = null;
        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        StaffDTO dto = (StaffDTO) populateDTO(request);
        String op = DataUtility.getString(request.getParameter("operation"));

        String[] ids = request.getParameterValues("ids");
        StaffModelInt model = ModelFactory.getInstance().getStaffModel();
        try {
            if (OP_SEARCH.equalsIgnoreCase(op)) {
                pageNo = 1;
            } else if (OP_NEXT.equalsIgnoreCase(op)) {
                pageNo++;
            } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
                pageNo--;
            } else if (OP_NEW.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.STAFF_CTL, request, response);
                return;
            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.STAFF_LIST_CTL, request, response);
                return;
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
            	pageNo = 1;
				if (ids != null && ids.length > 0) {
					StaffDTO deletedto = new StaffDTO();
					for (String id : ids) {
						deletedto.setId(DataUtility.getLong(id));
						model.delete(deletedto);
						ServletUtility.setSuccessMessage("Data Deleted Successfully", request);
                }} else {
                    ServletUtility.setErrorMessage("Select at least one record", request);
                }
            }
            list = model.search(dto, pageNo, pageSize);
            next = model.search(dto, pageNo + 1, pageSize);

            ServletUtility.setList(list, request);
            if (list == null || list.isEmpty()) {
                if (!OP_DELETE.equalsIgnoreCase(op)) {
                    ServletUtility.setErrorMessage("No record found ", request);
                }
            }
            request.setAttribute("nextListSize", next == null ? 0 : next.size());

            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);
        } catch (ApplicationException e) {
            log.error(e);
            ServletUtility.handleException(e, request, response);
        }
        log.debug("StaffListCtl doPost End");
    }

    @Override
    protected String getView() {
        return ORSView.STAFF_LIST_VIEW;
    }
}
