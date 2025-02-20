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
import in.co.rays.project_3.dto.ItemDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.ItemModelInt;
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
@WebServlet(urlPatterns = { "/ctl/ItemCtl" })
public class ItemCtl extends BaseCtl {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/** The log. */
	private static Logger log = Logger.getLogger(ItemCtl.class);

	protected void preload(HttpServletRequest request) {
		Map<Integer, String> map = new HashMap();

		map.put(1, "High");
		map.put(2, "Medium");
		map.put(3, "Low");
		
		request.setAttribute("item", map);
	
}

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;
		System.out.println("-------------validate started-------------");
         
		if (DataValidator.isNull(request.getParameter("category"))) {
			request.setAttribute("category", PropertyReader.getValue("error.require", "category"));
			System.out.println(pass);
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("cost"))) {
			request.setAttribute("cost", PropertyReader.getValue("error.require", "cost"));
			System.out.println(pass);
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("date"))) {
			request.setAttribute("date", PropertyReader.getValue("error.require", "date"));
			System.out.println(pass);
			pass = false;
		}


		if (DataValidator.isNull(request.getParameter("title"))) {
			request.setAttribute("title", PropertyReader.getValue("error.require", "title"));
			System.out.println(pass);
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("title"))) {
			request.setAttribute("title", "title must contains alphabets only");
			System.out.println(pass);
			pass = false;

		}
		if (DataValidator.isNull(request.getParameter("overView"))) {
			request.setAttribute("overView", PropertyReader.getValue("error.require", "overView"));
			System.out.println(pass);
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("overView"))) {
			request.setAttribute("overView", "overView must contains alphabets only");
			System.out.println(pass);
			pass = false;

		}
		
		return pass;

	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		ItemDTO dto = new ItemDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setTitle(DataUtility.getString(request.getParameter("title")));
		dto.setOverView(DataUtility.getString(request.getParameter("overView")));
		dto.setCategory(DataUtility.getInt(request.getParameter("category")));
		dto.setCost(DataUtility.getInt(request.getParameter("cost")));
		dto.setPurchaseDate(DataUtility.getDate(request.getParameter("date")));

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
		log.debug("ItemCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		ItemModelInt model = ModelFactory.getInstance().getItemModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {
			System.out.println("in id > 0  condition");
			ItemDTO dto;
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
		ItemModelInt model = ModelFactory.getInstance().getItemModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			ItemDTO dto = (ItemDTO) populateDTO(request);
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

			ItemDTO dto = (ItemDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.ITEM_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.ITEM_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.ITEM_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("ItemCtl Method doPostEnded");
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.ITEM_VIEW;
	}

}
