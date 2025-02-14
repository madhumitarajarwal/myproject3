package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import in.co.rays.project_3.dto.PrescriptionDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class PrescriptionModelHibImp implements PrescriptionModelInt{

	@Override
	public long add(PrescriptionDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = HibDataSource.getSession();
		Transaction tx = null;
		try {
           tx = session.beginTransaction();
           session.save(dto);
			tx.commit();
		} catch (HibernateException e){
			e.printStackTrace();
			// TODO: handle exception
			if (tx != null) {
				tx.rollback();
				}
		throw new ApplicationException("Exception in User Add " + e.getMessage());
		} finally {
			session.close();
		}
		/* log.debug("Model add End"); */
		return dto.getId();
	}

	@Override
	public void delete(PrescriptionDTO dto) throws ApplicationException {
		Session session=null;
		Transaction tx=null;
		try {
		   session= HibDataSource.getSession();
		   tx=session.beginTransaction();
		   session.delete(dto);
		   tx.commit();
		}catch(HibernateException ex) {
			if(tx!=null) {
				tx.rollback();
			}throw new ApplicationException("Exception in user delete"+ex.getMessage());
			
		}finally {
			session.close();
		}
		
	}

	@Override
	public void update(PrescriptionDTO dto) throws ApplicationException, DuplicateRecordException {
		// TODO Auto-generated method stub
		Session session=null;
		Transaction tx=null;
		try {
		    session=HibDataSource.getSession();
		    tx=session.beginTransaction();
		    session.saveOrUpdate(dto);
		    tx.commit();
		}catch(HibernateException e) {
			   if(tx!=null) {
				   tx.rollback();
			   }throw new ApplicationException("Exception in user updated "+e.getMessage());
		   } finally{
			   session.close();
		   }
		
		
	}

	@Override
	public PrescriptionDTO findByPk(long pk)throws ApplicationException  {
		Session session=null;
		Transaction tx=null;
		PrescriptionDTO dto=null;
		try {
			session=HibDataSource.getSession();
			tx=session.beginTransaction();
			dto=(PrescriptionDTO) session.get(PrescriptionDTO.class, pk);
			tx.commit();
		}catch(HibernateException e) {
			if(dto!=null) {
				tx.rollback();
			}throw new ApplicationException(" Exception in getting Bank by pk"+e.getMessage());
		}finally {
			session.close();
		}
		
		return dto;
	}

	@Override
	public List list() throws ApplicationException {
		// TODO Auto-generated method stub
		return list(0,0);
	}

	@Override
public List list(int pageNo, int pageSize) throws ApplicationException {
		
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(PrescriptionDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);

			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in  Banks list");
		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public List search(PrescriptionDTO dto, int pageNo, int pageSize) throws ApplicationException {
		// TODO Auto-generated method stub
		Session session=null;
		ArrayList<PrescriptionDTO> list=null;
		try {
			session=HibDataSource.getSession();
		Criteria criteria=session.createCriteria(PrescriptionDTO.class);
			if(dto!=null) {
				if(dto.getId()!=null && dto.getId()>0) {
					criteria.add(Restrictions.eq("id", dto.getId()));
			}
				if(dto.getName()!=null && dto.getName().length()>0) {
					criteria.add(Restrictions.like("NAME", dto.getName()+"%"));
				}
				if(dto.getDecease()>0) {
					criteria.add(Restrictions.eq("DECEASE",dto.getDecease()));
				}
				if(dto.getCapacity()>0) {
					criteria.add(Restrictions.eq("CAPACITY",dto.getCapacity()));	
				}
				if (dto.getDob() != null && dto.getDob().getDate() > 0) {
					criteria.add(Restrictions.like("dob", dto.getDob()));
				}
			}
			if(pageSize>0) {
				pageNo=(pageNo-1)*pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list=(ArrayList<PrescriptionDTO>) criteria.list();
		
	}catch(HibernateException e) {
		throw new ApplicationException("Exception in PortfolioManagement search");
		}finally {
			session.close();
		}
		return list;
	}

	@Override
	public List search(PrescriptionDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return search(dto,0,0);
	}

}
