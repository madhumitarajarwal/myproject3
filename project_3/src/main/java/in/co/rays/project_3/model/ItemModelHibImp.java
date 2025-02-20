package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.ItemDTO;
import in.co.rays.project_3.dto.ItemDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.exception.RecordNotFoundException;
import in.co.rays.project_3.util.HibDataSource;

public class ItemModelHibImp implements ItemModelInt{

	public long add(ItemDTO dto) throws ApplicationException, DuplicateRecordException {

		System.out.println("in addddddddddddd");
		/* log.debug("usermodel hib start"); */

		ItemDTO existDto = null;
		Session session = HibDataSource.getSession();
		Transaction tx = null;
		try {

			tx = session.beginTransaction();

			session.save(dto);

			tx.commit();
		} catch (HibernateException e) {
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

	/**
	 * Delete a User.
	 *
	 * @param dto the bean
	 * @throws ApplicationException the application exception
	 */
	public void delete(ItemDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in User Delete" + e.getMessage());
		} finally {
			session.close();
		}
	}

	/**
	 * Update a user.
	 *
	 * @param bean the bean
	 * @throws ApplicationException     the application exception
	 * @throws DuplicateRecordException the duplicate record exception
	 * @throws RecordNotFoundException  the record not found exception
	 */
	public void update(ItemDTO dto) throws ApplicationException, DuplicateRecordException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(dto);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in User update" + e.getMessage());
		} finally {
			session.close();
		}
	}

	/**
	 * Find User by PK.
	 *
	 * @param pk : get parameter
	 * @return bean
	 * @throws ApplicationException the application exception
	 */

	public ItemDTO findByPK(long pk) throws ApplicationException {
		// TODO Auto-generated method stub
		Session session = null;
		ItemDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (ItemDTO) session.get(ItemDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting User by pk");

		} finally {
			session.close();
		}

		return dto;
	}

	/**
	 * Find User by Login.
	 *
	 * @param login : get parameter
	 * @return bean
	 * @throws ApplicationException the application exception
	 */

	public List list() throws ApplicationException {
		// TODO Auto-generated method stub
		return list(0, 0);
	}

	/**
	 * Get List of User with pagination.
	 *
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @return list : List of users
	 * @throws ApplicationException the application exception
	 */

	public List list(int pageNo, int pageSize) throws ApplicationException {
		// TODO Auto-generated method stub
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(ItemDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);

			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in  Users list");
		} finally {
			session.close();
		}

		return list;
	}

	/**
	 * Search User.
	 *
	 * @param bean : Search Parameters
	 * @return the list
	 * @throws ApplicationException the application exception
	 */
	public List search(ItemDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return search(dto, 0, 0);
	}

	/**
	 * Search User with pagination.
	 *
	 * @param bean     : Search Parameters
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @return list : List of Users
	 * @throws ApplicationException the application exception
	 */
	public List search(ItemDTO dto, int pageNo, int pageSize) throws ApplicationException {
		// TODO Auto-generated method stub

		Session session = null;
		ArrayList<ItemDTO> list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(ItemDTO.class);
			if (dto != null) {
				if (dto.getId() != null) {
					criteria.add(Restrictions.eq("id", dto.getId()));
				}
				if (dto.getCost() > 0) {
					criteria.add(Restrictions.eq("cost", dto.getCost()));
				}
				if (dto.getCategory() > 0) {
					criteria.add(Restrictions.eq("category", dto.getCategory()));
				}
				if (dto.getTitle() != null && dto.getTitle().length() > 0) {
					criteria.add(Restrictions.like("title", dto.getTitle() + "%"));
				}

				if (dto.getOverView() != null && dto.getOverView().length() > 0) {
					criteria.add(Restrictions.like("overView", dto.getOverView() + "%"));
				}
				if (dto.getPurchaseDate() != null && dto.getPurchaseDate().getDate() > 0) {
					criteria.add(Restrictions.eq("purchaseDate", dto.getPurchaseDate()));
				}

			}
			// if pageSize is greater than 0
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = (ArrayList<ItemDTO>) criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in user search");
		} finally {
			session.close();
		}

		return list;
	}
}
