package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.PrescriptionDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface PrescriptionModelInt {
	public long add(PrescriptionDTO dto)throws ApplicationException,DuplicateRecordException;
	public void delete(PrescriptionDTO dto)throws ApplicationException;
	public void update(PrescriptionDTO dto)throws ApplicationException,DuplicateRecordException;
	public PrescriptionDTO findByPk(long pk) throws ApplicationException;
	public List list()throws ApplicationException;
	public List list(int pageNo,int pageSize)throws ApplicationException;
	public List search(PrescriptionDTO dto,int pageNo,int pageSize)throws ApplicationException;
	public List search(PrescriptionDTO dto)throws ApplicationException;
	
	

}
