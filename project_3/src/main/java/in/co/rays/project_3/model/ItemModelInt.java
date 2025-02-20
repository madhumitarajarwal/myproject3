package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.ItemDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface ItemModelInt {
	public long add(ItemDTO dto)throws ApplicationException,DuplicateRecordException;
	public void delete(ItemDTO dto)throws ApplicationException;
	public void update(ItemDTO dto)throws ApplicationException,DuplicateRecordException;
	public ItemDTO findByPK(long pk)throws ApplicationException;
	public List list()throws ApplicationException;
	public List list(int pageNo,int pageSize)throws ApplicationException;
	public List search(ItemDTO dto,int pageNo,int pageSize)throws ApplicationException;
	public List search(ItemDTO dto)throws ApplicationException;
}
