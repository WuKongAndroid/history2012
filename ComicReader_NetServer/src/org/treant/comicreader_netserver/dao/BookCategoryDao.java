package org.treant.comicreader_netserver.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.treant.comicreader_netserver.pojo.BookCategory;
import org.treant.comicreader_netserver.util.HibernateSessionFactory;

public class BookCategoryDao {
	private static BookCategoryDao mBookCategoryDao=null;

	private BookCategoryDao() {

	}

	synchronized public static BookCategoryDao getInstance() {
		if (mBookCategoryDao == null) {
			mBookCategoryDao = new BookCategoryDao();
		}
		return mBookCategoryDao;
	}

	public List<BookCategory> getAllBookCategory() {
		Session session = HibernateSessionFactory.getSession();
		Query query = session.createQuery("from BookCategory");
		List<BookCategory> all = query.list();
		// for(BookCategory bookCategory:all){
		// bookCategory.getBookInfos();
		// }
		session.close();
		return all;
	}
}
