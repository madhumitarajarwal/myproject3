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
import in.co.rays.project_3.dto.CartDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.CartModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * Cart functionality controller.to perform add,delete and update operation
 * 
 * @author Madhumita Rajarwal
 *
 */
@WebServlet(urlPatterns = { "/ctl/CartCtl" })
public class CartCtl extends BaseCtl {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/** The log. */
	private static Logger log = Logger.getLogger(CartCtl.class);

	protected void preload(HttpServletRequest request) {
		Map<Integer, String> map = new HashMap();

		map.put(1, "software");
		map.put(2, "Electronic");
		map.put(3, "Hardware");
		
		request.setAttribute("Cart", map);
	}

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;
		System.out.println("-------------validate started-------------");
         
		if (DataValidator.isNull(request.getParameter("product"))) {
			request.setAttribute("product", PropertyReader.getValue("error.require", "product"));
			System.out.println(pass);
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("transactionDate"))) {
			request.setAttribute("transactionDate", PropertyReader.getValue("error.require", "transactionDate"));
			System.out.println(pass);
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("customerName"))) {
			request.setAttribute("customerName", PropertyReader.getValue("error.require", "customerName"));
			System.out.println(pass);
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("customerName"))) {
			request.setAttribute("customerName", "customerName must contains alphabets only");
			System.out.println(pass);
			pass = false;

		}
		if (DataValidator.isNull(request.getParameter("quantityOrder"))) {
			request.setAttribute("quantityOrder", PropertyReader.getValue("error.require", "quantityOrder"));
			System.out.println(pass);
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("quantityOrder"))) {
			request.setAttribute("quantityOrder", "quantityOrder must contains integer only");
			System.out.println(pass);
			pass = false;

		}
		
		return pass;

	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		CartDTO dto = new CartDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setCustomerName(DataUtility.getString(request.getParameter("customerName")));
		dto.setProduct(DataUtility.getInt(request.getParameter("product")));
		dto.setTransactionDate(DataUtility.getDate(request.getParameter("transactionDate")));
		dto.setQuantityOrder(DataUtility.getInt(request.getParameter("quantityOrder")));

		populateBean(dto, request);

		log.debug("CartRegistrationCtl Method populatedto Ended");

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
		log.debug("CartCtl Method doGet Started");
		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		CartModelInt model = ModelFactory.getInstance().getCartModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {
			System.out.println("in id > 0  condition");
			CartDTO dto;
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
		CartModelInt model = ModelFactory.getInstance().getCartModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			CartDTO dto = (CartDTO) populateDTO(request);
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

			CartDTO dto = (CartDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.CART_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CART_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CART_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("CartCtl Method doPostEnded");
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.CART_VIEW;
	}

}
