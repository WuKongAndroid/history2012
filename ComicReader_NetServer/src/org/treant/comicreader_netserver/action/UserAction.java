package org.treant.comicreader_netserver.action;

import org.treant.comicreader_netserver.dao.UserInfoDao;
import org.treant.comicreader_netserver.pojo.UserInfo;

import com.opensymphony.xwork2.ActionSupport;

public class UserAction extends ActionSupport {
	private UserInfo userInfo;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String toAddUserInfo() {
		System.out.println(userInfo.getUname()+"-111->"+userInfo.getUadvise()
		+"->"+userInfo.getUbirthday()+"->"+userInfo.getUemail()+"->"
		+userInfo.getUhobby()+"->"+userInfo.getUlocation()
		+"->"+userInfo.getUsex()
		
		);
		UserInfoDao.getInstance().addUserInfo(userInfo);
		return null;
	}
}
