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
import in.co.rays.project_3.dto.ItemDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ItemModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * Item List functionality controller to perform Search and List operations.
 */
@WebServlet(name = "ItemListCtl", urlPatterns = { "/ctl/ItemListCtl" })
public class ItemListCtl extends BaseCtl {

    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(ItemListCtl.class);

    protected void preload(HttpServletRequest request) {
        ItemModelInt umodel = ModelFactory.getInstance().getItemModel();
        Map<Integer, String> map = new HashMap<>();

        map.put(1, "High");
        map.put(2, "Medium");
        map.put(3, "Low");

        request.setAttribute("item", map);
    }

    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {
        ItemDTO dto = new ItemDTO();

        
        dto.setTitle(DataUtility.getString(request.getParameter("title")));
        dto.setOverView(DataUtility.getString(request.getParameter("overView")));
        dto.setCategory(DataUtility.getInt(request.getParameter("category")));
        dto.setCost(DataUtility.getInt(request.getParameter("cost")));

        // Fixing date parsing issue
        String dateStr = request.getParameter("date");
        if (dateStr != null && !dateStr.isEmpty()) {
            dto.setPurchaseDate(DataUtility.getDate(dateStr));
        }

       

        log.debug("ItemListCtl Method populateDTO Ended");
        return dto;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("ItemListCtl doGet Start");

        List<ItemDTO> list = null;
        List<ItemDTO> next = null;
        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
        ItemDTO dto = (ItemDTO) populateDTO(request);

        ItemModelInt model = ModelFactory.getInstance().getItemModel();
        try {
            list = model.search(dto, pageNo, pageSize);
            next = model.search(dto, pageNo + 1, pageSize);

            ServletUtility.setList(list, request);
            if (list == null || list.isEmpty()) {
                ServletUtility.setErrorMessage("No record found", request);
            }
            request.setAttribute("nextListSize", (next == null) ? 0 : next.size());

            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);
        } catch (ApplicationException e) {
            log.error(e);
            ServletUtility.handleException(e, request, response);
        }
        log.debug("ItemListCtl doGet End");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("ItemListCtl doPost Start");

        List<ItemDTO> list = null;
        List<ItemDTO> next = null;
        int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        ItemDTO dto = (ItemDTO) populateDTO(request);
        String op = DataUtility.getString(request.getParameter("operation"));

        String[] ids = request.getParameterValues("ids");
        ItemModelInt model = ModelFactory.getInstance().getItemModel();
        try {
            if ("Search".equalsIgnoreCase(op)) {
                pageNo = 1;
            } else if ("Next".equalsIgnoreCase(op)) {
                pageNo++;
            } else if ("Previous".equalsIgnoreCase(op) && pageNo > 1) {
                pageNo--;
            } else if ("New".equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.ITEM_CTL, request, response);
                return;
            } else if ("Reset".equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.ITEM_LIST_CTL, request, response);
                return;
            } else if ("Delete".equalsIgnoreCase(op)) {
                pageNo = 1;
                if (ids != null && ids.length > 0) {
                    for (String id : ids) {
                        ItemDTO deletedto = new ItemDTO();
                        deletedto.setId(DataUtility.getLong(id));
                        model.delete(deletedto);
                    }
                    ServletUtility.setSuccessMessage("Data Deleted Successfully", request);
                } else {
                    ServletUtility.setErrorMessage("Select at least one record", request);
                }
            }
            list = model.search(dto, pageNo, pageSize);
            next = model.search(dto, pageNo + 1, pageSize);

            ServletUtility.setList(list, request);
            if (list == null || list.isEmpty() && !"Delete".equalsIgnoreCase(op)) {
                ServletUtility.setErrorMessage("No record found", request);
            }
            request.setAttribute("nextListSize", (next == null) ? 0 : next.size());

            ServletUtility.setPageNo(pageNo, request);
            ServletUtility.setPageSize(pageSize, request);
            ServletUtility.forward(getView(), request, response);
        } catch (ApplicationException e) {
            log.error(e);
            ServletUtility.handleException(e, request, response);
        }
        log.debug("ItemListCtl doPost End");
    }

    @Override
    protected String getView() {
        return ORSView.ITEM_LIST_VIEW;
    }
}