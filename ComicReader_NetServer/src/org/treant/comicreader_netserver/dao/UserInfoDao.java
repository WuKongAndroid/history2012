package org.treant.comicreader_netserver.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.treant.comicreader_netserver.pojo.UserInfo;
import org.treant.comicreader_netserver.util.HibernateSessionFactory;

public class UserInfoDao {
	private static UserInfoDao mUserInfoDao=null;

	private UserInfoDao() {

	}

	synchronized public static UserInfoDao getInstance() {
		if (mUserInfoDao == null) {
			mUserInfoDao = new UserInfoDao();
		}
		return mUserInfoDao;
	}

	public static Session getSession() {
		return HibernateSessionFactory.getSession();
	}

	public void addUserInfo(UserInfo userInfo) {
		System.out.println(userInfo.getUname()+"-22222->"+userInfo.getUadvise()
				+"->"+userInfo.getUbirthday()+"->"+userInfo.getUemail()+"->"
				+userInfo.getUhobby()+"->"+userInfo.getUlocation()
				+"->"+userInfo.getUsex()
				
				);
		Session session = getSession();
		Transaction tran = null;
		try {
			tran = session.beginTransaction();
			session.save(userInfo);
			tran.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
		} finally {
			session.close();
		}
	}
	
	
}
