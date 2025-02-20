package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.PurchaseDTO;
import in.co.rays.project_3.dto.PurchaseDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.exception.RecordNotFoundException;

/**
 * Interface of User model
 * @author Madhumita Rajarwal
 *
 */
public interface PurchaseModelInt {
public long add(PurchaseDTO dto)throws ApplicationException,DuplicateRecordException;
public void delete(PurchaseDTO dto)throws ApplicationException;
public void update(PurchaseDTO dto)throws ApplicationException,DuplicateRecordException;
public PurchaseDTO findByPK(long pk)throws ApplicationException;
public List list()throws ApplicationException;
public List list(int pageNo,int pageSize)throws ApplicationException;
public List search(PurchaseDTO dto,int pageNo,int pageSize)throws ApplicationException;
public List search(PurchaseDTO dto)throws ApplicationException;


}
