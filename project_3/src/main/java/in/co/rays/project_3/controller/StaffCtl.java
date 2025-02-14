package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.StaffDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.StaffModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * STAFF functionality controller.to perform add,delete and update operation
 * 
 * @author Madhumita Rajarwal
 *
 */
@WebServlet(urlPatterns = { "/ctl/StaffCtl" })
public class StaffCtl extends BaseCtl {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/** The log. */
	private static Logger log = Logger.getLogger(StaffCtl.class);

	protected void preload(HttpServletRequest request) {
		Map<Integer, String> map = new HashMap();

		map.put(1, "Management");
		map.put(2, "Human Resource");
		map.put(3, "Lead");
		
		request.setAttribute("staff", map);
	





	}

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;
		System.out.println("-------------validate started-------------");
         
		if (DataValidator.isNull(request.getParameter("division"))) {
			request.setAttribute("division", PropertyReader.getValue("error.require", "division"));
			System.out.println(pass);
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("date"))) {
			request.setAttribute("date", PropertyReader.getValue("error.require", "date"));
			System.out.println(pass);
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("fullName"))) {
			request.setAttribute("fullName", PropertyReader.getValue("error.require", "fullName"));
			System.out.println(pass);
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("fullName"))) {
			request.setAttribute("fullName", "fullName must contains alphabets only");
			System.out.println(pass);
			pass = false;

		}
		if (DataValidator.isNull(request.getParameter("previousEmployer"))) {
			request.setAttribute("previousEmployer", PropertyReader.getValue("error.require", "previousEmployer"));
			System.out.println(pass);
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("previousEmployer"))) {
			request.setAttribute("previousEmployer", "previousEmployer must contains alphabets only");
			System.out.println(pass);
			pass = false;

		}
		
		return pass;

	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		StaffDTO dto = new StaffDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setFullName(DataUtility.getString(request.getParameter("fullName")));
		dto.setPreviousEmployer(DataUtility.getString(request.getParameter("previousEmployer")));
		dto.setDivision(DataUtility.getInt(request.getParameter("division")));
		dto.setJoiningDate(DataUtility.getDate(request.getParameter("date")));

		populateBean(dto, request);

		log.debug("STAFFRegistrationCtl Method populatedto Ended");

		return dto;

	}

	/**
	 * Contain Display Logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("StaffCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		StaffModelInt model = ModelFactory.getInstance().getStaffModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {
			System.out.println("in id > 0  condition");
			StaffDTO dto;
			try {
				dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Contain Submit Logics.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String op = DataUtility.getString(request.getParameter("operation"));
		System.out
				.println("-------------------------------------------------------------------------dopost run-------");
		// get model
		StaffModelInt model = ModelFactory.getInstance().getStaffModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			StaffDTO dto = (StaffDTO) populateDTO(request);
			System.out.println(" in do post method jkjjkjk++++++++" + dto.getId());
			try {
				if (id > 0) {
					model.update(dto);

					ServletUtility.setSuccessMessage("Data is successfully Update", request);
				} else {

					try {
						model.add(dto);
						ServletUtility.setDto(dto, request);
						ServletUtility.setSuccessMessage("Data is successfully saved", request);
					} catch (ApplicationException e) {
						log.error(e);
						ServletUtility.handleException(e, request, response);
						return;
					} catch (DuplicateRecordException e) {
						ServletUtility.setDto(dto, request);
						ServletUtility.setErrorMessage("Login id already exists", request);
					}

				}
				ServletUtility.setDto(dto, request);

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			StaffDTO dto = (StaffDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.STAFF_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.STAFF_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.STAFF_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("StaffCtl Method doPostEnded");
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.STAFF_VIEW;
	}

}
