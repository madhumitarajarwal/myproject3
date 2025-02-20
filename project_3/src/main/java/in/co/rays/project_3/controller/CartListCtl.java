package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.CartDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.CartModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * Staff List functionality controller to perform Search and List operations.
 */
@WebServlet(name = "CartListCtl", urlPatterns = { "/ctl/CartListCtl" })
public class CartListCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CartListCtl.class);

	protected void preload(HttpServletRequest request) {
		CartModelInt umodel = ModelFactory.getInstance().getCartModel();
		Map<Integer, String> map = new HashMap();

		map.put(1, "software");
		map.put(2, "Electronic");
		map.put(3, "Hardware");

		request.setAttribute("Cart", map);
	}

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		CartDTO dto = new CartDTO();

		dto.setCustomerName(DataUtility.getString(request.getParameter("customerName")));
		dto.setProduct(DataUtility.getInt(request.getParameter("product")));
		dto.setTransactionDate(DataUtility.getDate(request.getParameter("transactionDate")));
		dto.setQuantityOrder(DataUtility.getInt(request.getParameter("quantityOrder")));


		log.debug("CartListCtl Method populateDTO Ended");
		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CartListCtl doGet Start");

		List<CartDTO> list = null;
		List<CartDTO> next = null;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		CartDTO dto = (CartDTO) populateDTO(request);

		CartModelInt model = ModelFactory.getInstance().getCartModel();
		try {
			list = model.search(dto, pageNo, pageSize);
			next = model.search(dto, pageNo + 1, pageSize);
			
			System.out.println("list === " + list.size());

			System.out.println("list === " + list.size());

			if (list == null || list.isEmpty()) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			request.setAttribute("nextListSize", next == null ? 0 : next.size());

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
		}
		log.debug("CartListCtl doGet End");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CartListCtl doPost Start");

		List<CartDTO> list = null;
		List<CartDTO> next = null;
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		CartDTO dto = (CartDTO) populateDTO(request);
		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");
		CartModelInt model = ModelFactory.getInstance().getCartModel();
		try {
			if (OP_SEARCH.equalsIgnoreCase(op)) {
				pageNo = 1;
			} else if (OP_NEXT.equalsIgnoreCase(op)) {
				pageNo++;
			} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
				pageNo--;
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.CART_CTL, request, response);
				return;
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.CART_LIST_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					CartDTO deletedto = new CartDTO();
					for (String id : ids) {
						deletedto.setId(DataUtility.getLong(id));
						model.delete(deletedto);
						ServletUtility.setSuccessMessage("Data Deleted Successfully", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			}
			list = model.search(dto, pageNo, pageSize);
			next = model.search(dto, pageNo + 1, pageSize);

			

			if (list == null || list.isEmpty()) {
				if (!OP_DELETE.equalsIgnoreCase(op)) {
					ServletUtility.setErrorMessage("No record found ", request);
				}
			}
			request.setAttribute("nextListSize", next == null ? 0 : next.size());

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
		}
		log.debug("CartListCtl doPost End");
	}

	@Override
	protected String getView() {
		return ORSView.CART_LIST_VIEW;
	}
}
