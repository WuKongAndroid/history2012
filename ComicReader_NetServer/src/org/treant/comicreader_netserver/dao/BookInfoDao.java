package org.treant.comicreader_netserver.dao;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.treant.comicreader_netserver.pojo.BookInfo;
import org.treant.comicreader_netserver.util.HibernateSessionFactory;

public class BookInfoDao {
	private static BookInfoDao mBookInfoDao = null;

	// 构造方法私有化,外界无法直接实例化
	private BookInfoDao() {

	}

	/**
	 * 懒汉单例模式 需要时才生成唯一实例
	 * 
	 * @return
	 */
	synchronized public static BookInfoDao getInstance() {
		if (mBookInfoDao == null) {
			mBookInfoDao = new BookInfoDao();
		}
		return mBookInfoDao;
	}

	public static Session getSession() {
		return HibernateSessionFactory.getSession();
	}

	// Get All BookInfo
	public List<BookInfo> getAllBookInfo() {
		String hql = "from BookInfo";
		return getBookInfoAsHqlVaries(hql);
	}

	// setFirstResult()【设置从哪条记录开始取】setMaxResults()【最多取多少条记录】
	/**
	 * 本期主打按更新时间降序desc取5个
	 */
	public List<BookInfo> getSpecialBookInfo() {
		Session session = getSession();
		String hql = "from BookInfo as bookInfo order by bookInfo.updateTime desc";
		Query query = session.createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(5);
		List<BookInfo> all = query.list();
		for (BookInfo bookInfo : all) {
			bookInfo.getBookCategory().getCname();
		}
		session.close();
		return all;
	}

	/**
	 * 推荐榜按评分率升序asc取5个
	 */
	public List<BookInfo> getRecommendBookInfo() {
		Session session = getSession();
		String hql = "from BookInfo as bookInfo order by bookInfo.bookGradeNums asc ";
		Query query = session.createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(5);
		List<BookInfo> all = query.list();
		for (BookInfo bookInfo : all) {
			bookInfo.getBookCategory().getCname();
		}
		session.close();
		return all;
	}

	/**
	 * 排行榜按点击量降序desc取全部
	 */
	public List<BookInfo> getRankListBookInfo() {
		String hql = "from BookInfo as bookInfo order by bookInfo.bookGradeRater desc ";
		return getBookInfoAsHqlVaries(hql);
	}

	// 根据bookCategory.cname查询BookInfo集合
	public List<BookInfo> getBookInfoByCategoryName(String cname) {
		Session session = getSession();
		String hql = "from BookInfo as bookInfo where bookInfo.bookCategory.cname=?";
		Query query = session.createQuery(hql);
		query.setString(0, cname);
		List<BookInfo> all = query.list();
		for (BookInfo bookInfo : all) {
			bookInfo.getBookCategory().getCname();
		}
		session.close();
		return all;
	}

	/**
	 * 根据不同hql语句(不含?)查询BookInfo集合
	 * 
	 * @param hql
	 * @return
	 */
	public List<BookInfo> getBookInfoAsHqlVaries(String hql) {
		Session session = getSession();
		Query query = session.createQuery(hql);
		List<BookInfo> all = query.list();
		for (BookInfo bookInfo : all) {
			bookInfo.getBookCategory().getCname();
		}
		session.close();
		return all;
	}

	// 根据Id查询BookInfo对象
	public BookInfo getUniqueBookInfoById(String bookId) {
		// BookInfo bookInfo = null;
		// Session session = getSession();
		// Transaction transaction = null;
		// try {
		// transaction = session.beginTransaction();
		// bookInfo = (BookInfo) session.get(BookInfo.class, bookId);
		// transaction.commit();
		// } catch (Exception e) {
		// if (transaction != null) {
		// transaction.rollback();
		// }
		// e.printStackTrace();
		// } finally {
		// session.close();
		// }
		Session session = getSession();
		String hql = "from BookInfo bookInfo where bookInfo.bookId=?";
		Query query = session.createQuery(hql);
		query.setString(0, bookId);
		BookInfo bookInfo = (BookInfo) query.uniqueResult();
		return bookInfo;
	}

	// 根据Id修改BookInfo的Rate相关
	public void updateBookInfoById(String bookId, float bookGradeNums,
			int bookGradeRater) {
		// boolean flag = true;
		// BookInfo bookInfo = null;
		// Session session = getSession();
		// Transaction transaction = null;
		// try {
		// transaction = session.beginTransaction();
		// bookInfo = (BookInfo) session.get(BookInfo.class, bookId);
		// bookInfo.setBookGradeNums(bookGradeNums);
		// bookInfo.setBookGradeRater(bookGradeRater);
		// session.update(bookInfo);
		// transaction.commit();
		// } catch (Exception e) {
		// if (transaction != null) {
		// transaction.rollback();
		// }
		// e.printStackTrace();
		// flag = false;
		// } finally {
		// session.close();
		// }
		// return flag;
		// 出现异常java.lang.IllegalArgumentException: node to traverse cannot be
		// null!是hql语句错误
		// "update InsertGyl as i set i.gyh=?, i.lc=?, i.lcrs=?, i.gygly=? where i.gyh=? and i.lc=?"
		Session session = getSession();
		String hql = "update BookInfo b set b.bookGradeNums=?, b.bookGradeRater=? where b.bookId=?";
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			query.setFloat(0, bookGradeNums);
			query.setInteger(1, bookGradeRater);
			query.setString(2, bookId);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public List<BookInfo> getBookInfoByName(String keyWords) {
		String hqlStr = "from BookInfo as bookInfo where bookInfo.bookName like ?";
		return getBookInfoByKeyWords(hqlStr, keyWords);
	}

	public List<BookInfo> getBookInfoByWriter(String keyWords) {
		String hqlStr = "from BookInfo as bookInfo where bookInfo.bookWriter like ?";
		return getBookInfoByKeyWords(hqlStr, keyWords);
	}

	public List<BookInfo> getBookInfoByContent(String keyWords) {
		String hqlStr = "from BookInfo as bookInfo where bookInfo.bookContent like ?";
		return getBookInfoByKeyWords(hqlStr, keyWords);
	}

	/**
	 * 按关键字查询
	 */
	public List<BookInfo> getBookInfoByKeyWords(String hqlStr, String keyWords) {
		System.out.println("Dao中源码是:" + keyWords);
		// 已经是汉字 重编码多此一举
		try {
			System.out.println("Dao中关键字重编码后是:"
					+ new String(keyWords.getBytes("ISO-8859-1"), "gbk"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] str = keyWords.split(" ");
		StringBuilder sb = new StringBuilder();
		for (String temp : str) {
			if (!temp.equals("")) {
				sb.append("%" + temp + "%");
			}
		}
		Session session = getSession();
		Query query = session.createQuery(hqlStr);
		query.setString(0, sb.toString());
		List<BookInfo> all = query.list();
		for (BookInfo bookInfo : all) {
			bookInfo.getBookCategory().getCname();
		}
		session.close();
		return all;
	}
}
