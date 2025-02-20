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
import in.co.rays.project_3.dto.PurchaseDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.PurchaseModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * PURCHASE functionality controller to perform add, delete, and update operations.
 * 
 * @author Madhumita Rajarwal
 */
@WebServlet(urlPatterns = { "/ctl/PurchaseCtl" })
public class PurchaseCtl extends BaseCtl {

    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(PurchaseCtl.class);

    @Override
    protected void preload(HttpServletRequest request) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "Electronic");
        map.put(2, "Cosmetic");
        map.put(3, "Grocery");
        request.setAttribute("purchase", map);
    }

    @Override
    protected boolean validate(HttpServletRequest request) {
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("totalCost"))) {
            request.setAttribute("totalCost", PropertyReader.getValue("error.require", "totalCost"));
            pass = false;
        } else if (!DataValidator.isLong(request.getParameter("totalCost"))) {
            request.setAttribute("totalCost", "Total cost must contain numbers only.");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("orderDate"))) {
            request.setAttribute("orderDate", PropertyReader.getValue("error.require", "Order Date"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("quantity"))) {
            request.setAttribute("quantity", PropertyReader.getValue("error.require", "Quantity"));
            pass = false;
        } else if (!DataValidator.isLong(request.getParameter("quantity"))) {
            request.setAttribute("quantity", "Quantity must contain numbers only.");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("product"))) {
            request.setAttribute("product", PropertyReader.getValue("error.require", "Product"));
            pass = false;
        }

        return pass;
    }

    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {
        PurchaseDTO dto = new PurchaseDTO();

        dto.setId(DataUtility.getLong(request.getParameter("id")));
        dto.setTotalCost(DataUtility.getLong(request.getParameter("totalCost")));
        dto.setOrderDate(DataUtility.getDate(request.getParameter("orderDate")));
        dto.setQuantity(DataUtility.getLong(request.getParameter("quantity")));
        dto.setProduct(DataUtility.getInt(request.getParameter("product")));

        populateBean(dto, request);

        log.debug("PurchaseCtl populateDTO method ended");
        return dto;
    }

    /**
     * Contains Display Logic.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        log.debug("PurchaseCtl doGet method started");

        String op = DataUtility.getString(request.getParameter("operation"));
        PurchaseModelInt model = ModelFactory.getInstance().getPurchaseModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (id > 0) {
            try {
                PurchaseDTO dto = model.findByPK(id);
                ServletUtility.setDto(dto, request);
            } catch (ApplicationException e) {
                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        }
        ServletUtility.forward(getView(), request, response);
    }

    /**
     * Contains Submit Logic.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        log.debug("PurchaseCtl doPost method started");

        String op = DataUtility.getString(request.getParameter("operation"));
        PurchaseModelInt model = ModelFactory.getInstance().getPurchaseModel();
        long id = DataUtility.getLong(request.getParameter("id"));

        if (BaseCtl.OP_SAVE.equalsIgnoreCase(op) || BaseCtl.OP_UPDATE.equalsIgnoreCase(op)) {
            PurchaseDTO dto = (PurchaseDTO) populateDTO(request);
            try {
                if (id > 0) {
                    model.update(dto);
                    ServletUtility.setSuccessMessage("Data successfully updated", request);
                } else {
                    model.add(dto);
                    ServletUtility.setSuccessMessage("Data successfully saved", request);
                }
                ServletUtility.setDto(dto, request);
            } catch (ApplicationException e) {
                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            } catch (DuplicateRecordException e) {
                ServletUtility.setDto(dto, request);
                ServletUtility.setErrorMessage("Duplicate record found", request);
            }
        } else if (BaseCtl.OP_DELETE.equalsIgnoreCase(op)) {
            PurchaseDTO dto = (PurchaseDTO) populateDTO(request);
            try {
                model.delete(dto);
                ServletUtility.redirect(ORSView.PURCHASE_LIST_CTL, request, response);
                return;
            } catch (ApplicationException e) {
                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
            }
        } else if (BaseCtl.OP_CANCEL.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.PURCHASE_LIST_CTL, request, response);
            return;
        } else if (BaseCtl.OP_RESET.equalsIgnoreCase(op)) {
            ServletUtility.redirect(ORSView.PURCHASE_CTL, request, response);
            return;
        }
        ServletUtility.forward(getView(), request, response);
        log.debug("PurchaseCtl doPost method ended");
    }

    @Override
    protected String getView() {
        return ORSView.PURCHASE_VIEW;
    }
}
