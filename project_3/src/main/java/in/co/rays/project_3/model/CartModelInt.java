package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.CartDTO;

import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface CartModelInt {
public long add(CartDTO dto)throws ApplicationException,DuplicateRecordException;
public void delete(CartDTO dto)throws ApplicationException;
public void update(CartDTO dto)throws ApplicationException,DuplicateRecordException;
public CartDTO findByPK(long pk)throws ApplicationException;
public List list()throws ApplicationException;
public List list(int pageNo,int pageSize)throws ApplicationException;
public List search(CartDTO dto,int pageNo,int pageSize)throws ApplicationException;
public List search(CartDTO dto)throws ApplicationException;


}
