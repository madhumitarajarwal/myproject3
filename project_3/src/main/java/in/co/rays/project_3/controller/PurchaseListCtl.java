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
import in.co.rays.project_3.dto.PurchaseDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.PurchaseModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "PurchaseListCtl", urlPatterns = { "/ctl/PurchaseListCtl" })
public class PurchaseListCtl extends BaseCtl {

    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(PurchaseListCtl.class);

    protected void preload(HttpServletRequest request) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "Electronic");
        map.put(2, "Cosmetic");
        map.put(3, "Grocery");
        request.setAttribute("purchase", map);
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
        log.debug("PurchaseListCtl Method populateDTO Ended");
        return dto;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("PurchaseListCtl doGet Start");
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        PurchaseDTO dto = (PurchaseDTO) populateDTO(request);
        PurchaseModelInt model = ModelFactory.getInstance().getPurchaseModel();
        try {
            List<PurchaseDTO> list = model.search(dto, pageNo, pageSize);
            List<PurchaseDTO> next = model.search(dto, pageNo + 1, pageSize);
            ServletUtility.setList(list, request);
            if (list == null || list.isEmpty()) {
                ServletUtility.setErrorMessage("No record found", request);
            }
            request.setAttribute("nextListSize", next == null ? 0 : next.size());
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);
        } catch (ApplicationException e) {
            log.error(e);
            ServletUtility.handleException(e, request, response);
        }
        log.debug("PurchaseListCtl doGet End");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("PurchaseListCtl doPost Start");
        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
        PurchaseDTO dto = (PurchaseDTO) populateDTO(request);
        String op = DataUtility.getString(request.getParameter("operation"));
        String[] ids = request.getParameterValues("ids");
        PurchaseModelInt model = ModelFactory.getInstance().getPurchaseModel();
        try {
            if (OP_SEARCH.equalsIgnoreCase(op)) {
                pageNo = 1;
            } else if (OP_NEXT.equalsIgnoreCase(op)) {
                pageNo++;
            } else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
                pageNo--;
            } else if (OP_NEW.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.PURCHASE_CTL, request, response);
                return;
            } else if (OP_RESET.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.PURCHASE_LIST_CTL, request, response);
                return;
            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                if (ids != null && ids.length > 0) {
                    for (String id : ids) {
                        PurchaseDTO deletedto = new PurchaseDTO();
                        deletedto.setId(DataUtility.getLong(id));
                        model.delete(deletedto);
                    }
                    ServletUtility.setSuccessMessage("Data Deleted Successfully", request);
                } else {
                    ServletUtility.setErrorMessage("Select at least one record", request);
                }
            }
            List<PurchaseDTO> list = model.search(dto, pageNo, pageSize);
            List<PurchaseDTO> next = model.search(dto, pageNo + 1, pageSize);
            ServletUtility.setList(list, request);
            if (list == null || list.isEmpty() && !OP_DELETE.equalsIgnoreCase(op)) {
                ServletUtility.setErrorMessage("No record found", request);
            }
            request.setAttribute("nextListSize", next == null ? 0 : next.size());
            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);
        } catch (ApplicationException e) {
            log.error(e);
            ServletUtility.handleException(e, request, response);
        }
        log.debug("PurchaseListCtl doPost End");
    }

    @Override
    protected String getView() {
        return ORSView.PURCHASE_LIST_VIEW;
    }
}
