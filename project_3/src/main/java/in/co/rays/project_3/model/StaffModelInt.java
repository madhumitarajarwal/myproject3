package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.StaffDTO;
import in.co.rays.project_3.dto.UserDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;


/**
 * Interface of User model
 * @author Madhumita Rajarwal
 *
 */
public interface StaffModelInt {
public long add(StaffDTO dto)throws ApplicationException,DuplicateRecordException;
public void delete(StaffDTO dto)throws ApplicationException;
public void update(StaffDTO dto)throws ApplicationException,DuplicateRecordException;
public StaffDTO findByPK(long pk)throws ApplicationException;
public List list()throws ApplicationException;
public List list(int pageNo,int pageSize)throws ApplicationException;
public List search(StaffDTO dto,int pageNo,int pageSize)throws ApplicationException;
public List search(StaffDTO dto)throws ApplicationException;


}
