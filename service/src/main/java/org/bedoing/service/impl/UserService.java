package org.bedoing.service.impl;

import org.bedoing.constant.MapperConstant;

import org.bedoing.entity.LoginAccount;
import org.bedoing.entity.LoginLog;
import org.bedoing.mybatis.MyBatisDAO;
import org.bedoing.repository.LoginLogRepository;
import org.bedoing.repository.UserRepository;
import org.bedoing.security.EndecryptUtil;
import org.bedoing.service.IUserService;
import org.bedoing.util.DateUtils;
import org.bedoing.vo.LoginAccountVO;
import org.bedoing.vo.UserRegVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService implements IUserService{
	@Autowired
	private MyBatisDAO myBatisDAO;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoginLogRepository loginLogRepository;
	
	@Override
	public void saveLoginLog(LoginLog loginLog) {
		loginLog.setOprTime(new Date());
		loginLogRepository.save(loginLog);
	}
	
	@Override
	public void saveLoginAccount(UserRegVO user) {
		LoginAccount loginAccount = new LoginAccount();
		
		loginAccount.setAccountName(user.getLoginAccount());
		loginAccount.setPassword(EndecryptUtil.decrypt(user.getPassword()));
		loginAccount.setRole(user.getRole());
		loginAccount.setMobilePhone(user.getMobilePhone());
		loginAccount.setSex(user.getSex());
		loginAccount.setNickname(user.getNickname());
		loginAccount.setHeadImgUrl(user.getHeadimgurl());
		loginAccount.setCountry(user.getCountry());
		loginAccount.setCity(user.getCity());
		loginAccount.setProvince(user.getProvince());
		loginAccount.setCreateTime(new Date());
		loginAccount.setStatus(1);

		userRepository.save(loginAccount);
	};
	
	@Override
	public int countByLoginAccount(String loginAccount) {
		return myBatisDAO.get(MapperConstant.LOGINACCOUNT_countByLoginAccount, loginAccount);
	};

	@Override
	public List<LoginAccountVO> findUserByCriteria(Object obj) {
		List<LoginAccount> list = myBatisDAO.getList(MapperConstant.LOGINACCOUNT_findUserByCriteria, obj);
		List<LoginAccountVO> result = new ArrayList<LoginAccountVO>();
		for (LoginAccount user : list) {
			LoginAccountVO vo = new LoginAccountVO();
			vo.setId(user.getId());
			vo.setAccountName(user.getAccountName());
			vo.setPassword(user.getPassword());
			vo.setRole(user.getRole());
			vo.setMobilePhone(user.getMobilePhone());
			vo.setSex(user.getSex());
			vo.setNickname(user.getNickname());
			vo.setHeadImgUrl(user.getHeadImgUrl());
			vo.setCountry(user.getCountry());
			vo.setCity(user.getCity());
			vo.setProvince(user.getProvince());
			vo.setCreateTime(user.getCreateTime());
			vo.setStatus(user.getStatus());
			
//			vo.setRoleStr(DictParam.getRole(user.getRole()));
//			vo.setSexStr(DictParam.getSexStr(user.getSex()));
//			vo.setCreateTimeStr(DateUtils.formatDate(user.getCreateTime(), DictParam.getDateFormat(0)));
//			vo.setStatusStr(DictParam.getUserStatus(user.getStatus()));
			
			result.add(vo);
		}
		
		return result;
	};
	
}
